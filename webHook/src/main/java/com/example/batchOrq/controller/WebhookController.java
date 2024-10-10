package com.example.batchOrq.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookController {

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveWebhook(@RequestBody String message) {
        // Mostrar mensaje en la consola
        System.out.println("Recibido el mensaje del orquestador: " + message);

        // Retornar una respuesta adecuada
        return new ResponseEntity<>("Mensaje recibido", HttpStatus.OK);
    }
}
