package com.example.controllers;

import com.example.dtos.AloneNewDto;
import com.example.dtos.ContactDto;
import com.example.models.StandardInfo;
import com.example.service.MessageService;
import com.example.service.TokenService;
import com.example.service.VotingService;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/message")
@AllArgsConstructor
public class MessageController {
    private TokenService tokenService;
    private MessageService messageService;
    private VotingService votingService;

    @PostMapping("/sendMessage")
    public ResponseEntity<String> sendMessage(@RequestBody AloneNewDto aloneNewDto) {
        messageService.sendMessage(tokenService.getTokenUser(), aloneNewDto);
    return ResponseEntity.status(200).body("Letter is sending");
    }

    @GetMapping("/readMessage/{id}")
    public ResponseEntity<String> readMessage(@PathVariable("id") UUID id){
        messageService.readIndividualMessage((String) tokenService.getTokenUser().getClaims().get("sub"),id);
        return ResponseEntity.status(200).body("Letter is read");
    }
    @GetMapping("/deleteMessage/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable("id") UUID id){
        messageService.removeIndividualMessage((String) tokenService.getTokenUser().getClaims().get("sub"),id);
        return ResponseEntity.status(200).body("Letter is delete");
    }
    @GetMapping("/reject/{id}")
    public ResponseEntity<String> rejectAnswerMessage(@PathVariable("id") String id){

        return ResponseEntity.status(200).body(votingService.getVoting((String) tokenService.getTokenUser().getClaims().get("sub"),id,false));
    }
    @GetMapping("/accept/{id}")
    public ResponseEntity<String> acceptAnswerMessage(@PathVariable("id") String id){
        return ResponseEntity.status(200).body(votingService.getVoting((String) tokenService.getTokenUser().getClaims().get("sub"),id,true));
    }
    @GetMapping("/readResult/{id}")
    public ResponseEntity<String> readResult(@PathVariable("id") String id){
        messageService.removeRecipientFromSendTo((String) tokenService.getTokenUser().getClaims().get("sub"),id);
        return ResponseEntity.ok("Seen");

    }
    @GetMapping("/contact/addRequest/{uuid}")
    public String addRequestToAddContact(@PathVariable("uuid") String uuid){
        return messageService.addRequestToDoContact((String)tokenService.getTokenUser().getClaims().get("sub"), uuid);
    }
}
