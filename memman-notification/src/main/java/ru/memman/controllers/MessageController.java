package ru.memman.controllers;

import ru.memman.dtos.AloneNewDto;
import ru.memman.service.MessageService;
import ru.memman.service.TokenService;
import ru.memman.service.VotingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/notify/message")
@AllArgsConstructor
public class MessageController {
    private TokenService tokenService;
    private MessageService messageService;
    private VotingService votingService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody AloneNewDto aloneNewDto) {
    return ResponseEntity.status(200).body(messageService.sendMessage(aloneNewDto, true));
    }

//    @GetMapping("/read/{id}")
//    public ResponseEntity<String> readMessage(@PathVariable("id") UUID id){
//        messageService.readOrRemoveIndividualMessage((String) tokenService.getTokenUser().getClaims().get("sub"),id,true);
//        return ResponseEntity.status(200).body("Letter is read");
//    }
//    @GetMapping("/delete/{id}")
//    public ResponseEntity<String> deleteMessage(@PathVariable("id") UUID id){
//        messageService.readOrRemoveIndividualMessage((String) tokenService.getTokenUser().getClaims().get("sub"),id,false);
//        return ResponseEntity.status(200).body("Letter is delete");
//    }
    @GetMapping("/reject/{id}")
    public ResponseEntity<String> rejectAnswerMessage(@PathVariable("id") String id){

        return ResponseEntity.status(200).body(votingService.getVoting((String) tokenService.getTokenUser().getClaims().get("sub"),id,false));
    }
    @GetMapping("/accept/{id}")
    public ResponseEntity<String> acceptAnswerMessage(@PathVariable("id") String id){
        return ResponseEntity.status(200).body(votingService.getVoting((String) tokenService.getTokenUser().getClaims().get("sub"),id,true));
    }
//    @GetMapping("/readResult/{id}")
//    public ResponseEntity<String> readResult(@PathVariable("id") String id){
//        messageService.removeRecipientFromSendTo((String) tokenService.getTokenUser().getClaims().get("sub"),id);
//        return ResponseEntity.ok("Seen");
//
//    }
//    @GetMapping("/contact/request/{uuid}")
//    public String addRequestToAddContact(@PathVariable("uuid") String uuid){
//        return messageService.addRequestToDoContact((String)tokenService.getTokenUser().getClaims().get("sub"), uuid);
//    }
}
