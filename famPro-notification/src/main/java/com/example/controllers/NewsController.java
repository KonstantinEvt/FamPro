package com.example.controllers;

import com.example.dtos.AloneNewDto;
import com.example.holders.StandardInfoHolder;
import com.example.service.MessageService;
import com.example.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/news")
@AllArgsConstructor
public class NewsController {
    private TokenService tokenService;
    private MessageService messageService;
    private final StandardInfoHolder infoHolder;

    @GetMapping("/system")
    public ResponseEntity<List<AloneNewDto>> getSystemNews() {
        if (infoHolder.getOnlineInfo().get((String) tokenService.getTokenUser().getClaims().get("sub")) == null)
            return null;
        return ResponseEntity.ok(messageService.getSystemMessages((String) tokenService.getTokenUser().getClaims().get("sub")));
    }

    @GetMapping("/common")
    public ResponseEntity<List<AloneNewDto>> getCommonNews() {
        if (infoHolder.getOnlineInfo().get((String) tokenService.getTokenUser().getClaims().get("sub")) == null)
            return null;
        return ResponseEntity.ok(messageService.getCommonMessages((String) tokenService.getTokenUser().getClaims().get("sub")));
    }

    @GetMapping("/family")
    public ResponseEntity<List<AloneNewDto>> getFamilyNews() {
        if (infoHolder.getOnlineInfo().get((String) tokenService.getTokenUser().getClaims().get("sub")) == null)
            return null;
        return ResponseEntity.ok(infoHolder.getOnlineInfo().get((String) tokenService.getTokenUser().getClaims().get("sub")).getFamilyNews());
    }

    @GetMapping("/individual")
    public ResponseEntity<List<AloneNewDto>> getIndividualNews() {
        if (infoHolder.getOnlineInfo().get((String) tokenService.getTokenUser().getClaims().get("sub")) == null) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        return ResponseEntity.ok(infoHolder.getOnlineInfo().get((String) tokenService.getTokenUser().getClaims().get("sub")).getIndividualNews());
    }

    @GetMapping("/counts")
    public int[] getNewsCounts() {
        return messageService.getNewsCounts((String) tokenService.getTokenUser().getClaims().get("sub"));
    }
    @GetMapping("/globalNewsRead/{id}")
    public void readNews(@PathVariable("id") String id) {
        messageService.readGlobalMessage((String) tokenService.getTokenUser().getClaims().get("sub"), id);
    }
}
