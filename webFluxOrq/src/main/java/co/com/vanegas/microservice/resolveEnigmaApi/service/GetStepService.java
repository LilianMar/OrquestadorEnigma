package co.com.vanegas.microservice.resolveEnigmaApi.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import java.time.Duration;
import co.com.vanegas.microservice.resolveEnigmaApi.model.JsonApiBodyRequest;
import com.jayway.jsonpath.JsonPath;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class GetStepService {

    @Autowired
    private WebClient.Builder webClientBuilder;
    private static final Logger log = LoggerFactory.getLogger(GetStepService.class);
    private static final String CIRCUIT_BREAKER_NAME = "getStepServiceCircuitBreaker";
    private static final String FALLBACK_METHOD = "fallback";
    private int maxRetries = 3; // Número máximo de reintentos
    private long retryDelay = 2000;


    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = FALLBACK_METHOD)
    public Mono<String> getStepOne(@RequestBody JsonApiBodyRequest body) {
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8080/getStep")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class);
    }


    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = FALLBACK_METHOD)
    public Mono<String> getStepTwo(@RequestBody JsonApiBodyRequest body) {
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8081/getStep")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class);
    }


    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = FALLBACK_METHOD)
    public Mono<String> getStepThree(@RequestBody JsonApiBodyRequest body) {
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8082/getStep")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class);
    }

    // Orquestación con Mono.zip
    public Mono<String> orchestrateSteps(@RequestBody JsonApiBodyRequest body) {
        return Mono.zip(getStepOne(body), getStepTwo(body), getStepThree(body))
                .map(tuple -> {
                    String step1Answer = JsonPath.read(tuple.getT1(), "$[0].data[0].answer");
                    String step2Answer = JsonPath.read(tuple.getT2(), "$[0].data[0].answer");
                    String step3Answer = JsonPath.read(tuple.getT3(), "$[0].data[0].answer");

                    callWebhook("Mensaje desde el orquestador");

                    return formatFinalResponse(step1Answer, step2Answer, step3Answer);
                })
                .retryWhen(Retry.fixedDelay(maxRetries, Duration.ofMillis(retryDelay))) // Lógica de reintentos
                .onErrorResume(throwable -> fallback(body, throwable)); // Captura errores
    }

    private void callWebhook(String message) {
        webClientBuilder.build().post()
                .uri("http://localhost:8085/webhook") // Cambia esta URL si es necesario
                .bodyValue(message)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(response -> System.out.println("Respuesta del webhook: " + response),
                        error -> System.err.println("Error al llamar al webhook: " + error.getMessage()));
    }

    // Fallback Method
    public Mono<String> fallback(JsonApiBodyRequest body, Throwable ex) {

        log.warn("Fallback method executed due to error: {}", ex.getMessage());
        return Mono.just("El circuito está abierto. Intenta más tarde.");
    }


    private String formatFinalResponse(String step1Answer, String step2Answer, String step3Answer) {
        return String.format(
                "{\"data\": [{\"header\": {\"id\": \"12345\", \"type\": \"TestGiraffeRefrigerator\"}, \"answer\": \"Step1: %s - Step2: %s - Step3: %s\"}]}",
                step1Answer, step2Answer, step3Answer
        );
    }
}
