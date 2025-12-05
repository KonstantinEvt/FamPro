package com.example.controllers;

import com.example.dtos.AloneNewDto;
import com.example.enums.Attention;
import com.example.enums.NewsCategory;
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
        String token=(String) tokenService.getTokenUser().getClaims().get("sub");
        if (infoHolder.getOnlineInfo().get(token) == null)
            return null;
        return ResponseEntity.ok(messageService.getSystemMessages(token,false));
    }
    @GetMapping("/systemAll")
    public ResponseEntity<List<AloneNewDto>> getSystemNewsAll() {
        String token=(String) tokenService.getTokenUser().getClaims().get("sub");
        if (infoHolder.getOnlineInfo().get(token) == null)
            return null;
        return ResponseEntity.ok(messageService.getSystemMessages(token,true));
    }
    @GetMapping("/common")
    public ResponseEntity<List<AloneNewDto>> getCommonNews() {
        String token=(String) tokenService.getTokenUser().getClaims().get("sub");
        if (infoHolder.getOnlineInfo().get(token) == null)
            return null;
        return ResponseEntity.ok(messageService.getCommonMessages(token, false));
    }
    @GetMapping("/commonAll")
    public ResponseEntity<List<AloneNewDto>> getCommonNewsAll() {
        String token=(String) tokenService.getTokenUser().getClaims().get("sub");
        if (infoHolder.getOnlineInfo().get(token) == null)
            return null;
        return ResponseEntity.ok(messageService.getCommonMessages(token, true));
    }

    @GetMapping("/family")
    public ResponseEntity<List<AloneNewDto>> getFamilyNews() {
        String token=(String) tokenService.getTokenUser().getClaims().get("sub");
        if (infoHolder.getOnlineInfo().get(token) == null)
            return null;
        return ResponseEntity.ok(infoHolder.getOnlineInfo().get(token).getFamilyNews());
    }
    @GetMapping("/familyAll")
    public ResponseEntity<List<AloneNewDto>> getAllFamilyNews() {
        List<AloneNewDto> messageList = messageService.getAllNewsByCategory((String) tokenService.getTokenUser().getClaims().get("sub"), NewsCategory.FAMILY);

        return ResponseEntity.ok(messageList);
    }
    @GetMapping("/private")
    public ResponseEntity<List<AloneNewDto>> getIndividualNews() {
        String token=(String) tokenService.getTokenUser().getClaims().get("sub");
        if (infoHolder.getOnlineInfo().get(token) == null)
            return ResponseEntity.ok(new ArrayList<>());
        return ResponseEntity.ok(infoHolder.getOnlineInfo().get(token).getIndividualNews());
    }
    @GetMapping("/privateAll")
    public ResponseEntity<List<AloneNewDto>> getAllIndividualNews() {
        List<AloneNewDto> messageList=messageService.getAllNewsByCategory((String) tokenService.getTokenUser().getClaims().get("sub"), NewsCategory.PRIVATE);
        return ResponseEntity.ok(messageList);
    }

    @GetMapping("/counts")
    public int[] getNewsCounts() {
        return messageService.getNewsCounts((String) tokenService.getTokenUser().getClaims().get("sub"));
    }
    @GetMapping("/globalNewsRead/{category}/{id}")
    public void readNews(@PathVariable("category") Attention category, @PathVariable("id") String id) {
        messageService.readOrRemoveGlobalMessage((String) tokenService.getTokenUser().getClaims().get("sub"), category, id, true);
    }
    @GetMapping("/globalNewsRemove/{category}/{id}")
    public void removeNews(@PathVariable("category") Attention category, @PathVariable("id") String id) {
        messageService.readOrRemoveGlobalMessage((String) tokenService.getTokenUser().getClaims().get("sub"), category, id, false);
    }
 }
