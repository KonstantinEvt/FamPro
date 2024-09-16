package com.example.controllers;

import com.example.service.SenderMailService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
@AllArgsConstructor
public class MailController {
    private final SenderMailService service;
@GetMapping("/{sendTo}")
    public void sendMessage(@PathVariable String sendTo) throws MessagingException {
    service.sendingMessage(sendTo);
    }
}
