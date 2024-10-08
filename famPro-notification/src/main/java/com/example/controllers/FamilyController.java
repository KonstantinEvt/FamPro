package com.example.controllers;

import com.example.service.MailService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
@AllArgsConstructor
public class FamilyController {
    private final MailService service;
@GetMapping("")
    public void getMember() throws MessagingException {
    service.sendingMessage();
    }
}
