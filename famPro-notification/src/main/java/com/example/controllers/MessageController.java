package com.example.controllers;

import com.example.holders.StandardInfoHolder;
import com.example.dtos.AloneNewDto;
import com.example.models.StandardInfo;
import com.example.service.MailService;
import com.example.service.MessageService;
import com.example.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
@AllArgsConstructor
public class MessageController {
    private final MailService service;
    private TokenService tokenService;
    private final StandardInfoHolder infoHolder;
    private MessageService messageService;

    @PostMapping("/sendMessage")
    public ResponseEntity<String> sendMessage(@RequestBody AloneNewDto aloneNewDto) {
        messageService.sendMessage(tokenService.getTokenUser(), aloneNewDto);
        System.out.println("Letter is sending");
    return ResponseEntity.status(200).body("Letter is sending");
    }


    @GetMapping("/counts")
    public ResponseEntity<StandardInfo> getNewsCounts() {
        StandardInfo standardInfo = new StandardInfo();
//                infoHolder.getOnlineInfo().get((String) tokenService.getTokenUser().getClaims().get("sub"));
        System.out.println("hi");
        return ResponseEntity.ok(standardInfo);
    }
}
