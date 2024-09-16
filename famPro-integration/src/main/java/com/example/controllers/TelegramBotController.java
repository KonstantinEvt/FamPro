package com.example.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

//@RestController
//    public class TelegramBotController {
//        @Value("${spring.telega}")
//        private String botToken;
//
//
//    @PostMapping("/webhook")
//        public ResponseEntity<String> receiveUpdate(@RequestBody String update) {
//            // Обработка обновлений от Telegram
//            System.out.println("Received update: " + update);
//            return ResponseEntity.ok("Update received");
//        }
//
//        @GetMapping("/setWebhook")
//        public ResponseEntity<String> setWebhook() {
//            String webhookUrl = "https://yourdomain.com/webhook";
//            RestTemplate restTemplate = new RestTemplate();
//            String url = String.format("https://api.telegram.org/bot%s/setWebhook?url=%s", botToken, webhookUrl);
//            String response = restTemplate.getForObject(url, String.class);
//            return ResponseEntity.ok(response);
//        }
//    }
