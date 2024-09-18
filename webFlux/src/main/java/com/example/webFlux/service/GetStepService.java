package com.example.webFlux.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GetStepService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    // Método para obtener Step 1
    public Mono<String> getStepOne(String requestBody) {
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8080/getStep")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);
    }

    // Método para obtener Step 2
    public Mono<String> getStepTwo() {
        String stepTwoRequestBody = "{\"headerId\": \"12345\"}";
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8081/getStep")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(stepTwoRequestBody)
                .retrieve()
                .bodyToMono(String.class);
    }

    // Método para obtener Step 3
    public Mono<String> getStepThree() {
        String stepThreeRequestBody = "{\"headerId\": \"12345\"}";
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8082/getStep")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(stepThreeRequestBody)
                .retrieve()
                .bodyToMono(String.class);
    }

    // Orquestación de los 3 pasos
    public Mono<String> orchestrateSteps(String requestBody) {
        return getStepOne(requestBody)
                .flatMap(stepOneResponse -> {
                    String step1Answer = extractAnswer(stepOneResponse);
                    return getStepTwo()
                            .flatMap(stepTwoResponse -> {
                                String step2Answer = extractAnswer(stepTwoResponse);
                                return getStepThree()
                                        .map(stepThreeResponse -> {
                                            String step3Answer = extractAnswer(stepThreeResponse);
                                            return formatFinalResponse(step1Answer, step2Answer, step3Answer);
                                        });
                            });
                });
    }

    // Extraer la respuesta de cada paso (similar a la función extractAnswer que tenías)
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

    // Formatear la respuesta final
    private String formatFinalResponse(String step1Answer, String step2Answer, String step3Answer) {
        return String.format(
                "{\"data\": [{\"header\": {\"id\": \"12345\", \"type\": \"TestGiraffeRefrigerator\"}, \"answer\": \"Step1: %s - Step2: %s - Step3: %s\"}]}",
                step1Answer, step2Answer, step3Answer
        );
    }
}
