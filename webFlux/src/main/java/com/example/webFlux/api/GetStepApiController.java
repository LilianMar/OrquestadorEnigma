package com.example.webFlux.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orques/steps")
public class GetStepApiController {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @PostMapping("/step-one")
    public Mono<ResponseEntity<String>> getStepOne(@RequestBody String requestBody) {
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8080/getStep")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> ResponseEntity.ok(response));
    }

    @PostMapping("/step-two")
    public Mono<ResponseEntity<String>> getStepTwo(@RequestBody String requestBody) {
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8081/getStep")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> ResponseEntity.ok(response));
    }

    @PostMapping("/step-three")
    public Mono<ResponseEntity<String>> getStepThree(@RequestBody String requestBody) {
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8082/getStep")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> ResponseEntity.ok(response));
    }

    @PostMapping("/orchestration")
    public Mono<ResponseEntity<String>> startOrchestration(@RequestBody String requestBody) {
        WebClient client = webClientBuilder.build();

        return client.post()
                .uri("http://localhost:8080/getStep")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(stepOneResponse -> {
                    String step1Answer = extractAnswer(stepOneResponse);
                    return client.post()
                            .uri("http://localhost:8081/getStep")
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(requestBody)
                            .retrieve()
                            .bodyToMono(String.class)
                            .flatMap(stepTwoResponse -> {
                                String step2Answer = extractAnswer(stepTwoResponse);
                                return client.post()
                                        .uri("http://localhost:8082/getStep")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(requestBody)
                                        .retrieve()
                                        .bodyToMono(String.class)
                                        .map(stepThreeResponse -> {
                                            String step3Answer = extractAnswer(stepThreeResponse);

                                            String finalAnswer = String.format(
                                                    "{\"data\": [{\"header\": {\"id\": \"12345\", \"type\": \"TestGiraffeRefrigerator\"}, \"answer\": \"Step1: %s - Step2: %s - Step3: %s\"}]}",
                                                    step1Answer, step2Answer, step3Answer
                                            );
                                            return ResponseEntity.ok(finalAnswer);
                                        });
                            });
                });
    }

    // Función para extraer el valor de "answer" del body de la respuesta JSON
    private String extractAnswer(String jsonResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);

            if (rootNode.isArray() && rootNode.size() > 0) {
                JsonNode firstElement = rootNode.get(0);
                JsonNode dataNode = firstElement.path("data");
                if (dataNode.isArray() && dataNode.size() > 0) {
                    JsonNode firstDataElement = dataNode.get(0);
                    JsonNode answerNode = firstDataElement.path("answer");
                    if (!answerNode.isMissingNode()) {
                        return answerNode.asText();
                    }
                }
            }
        } catch (Exception e) {
            // Handle error
        }
        return "";
    }
}
