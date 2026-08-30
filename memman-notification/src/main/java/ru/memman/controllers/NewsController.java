package ru.memman.controllers;

import ru.memman.dtos.AloneNewDto;
import ru.memman.enums.Attention;
import ru.memman.enums.NewsCategory;
import ru.memman.holders.StandardInfoHolder;
import ru.memman.service.MessageService;
import ru.memman.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/notify/news")
@AllArgsConstructor
public class NewsController {
    private TokenService tokenService;
    private MessageService messageService;
    private final StandardInfoHolder infoHolder;

    @GetMapping("/system/{full}")
    public ResponseEntity<List<AloneNewDto>> getSystemNewsAll(@PathVariable("full") boolean full) {
        String token = (String) tokenService.getTokenUser().getClaims().get("sub");
        if (infoHolder.getOnlineInfo().get(token) == null)
            return ResponseEntity.ok(new ArrayList<>());
        return ResponseEntity.ok(messageService.getSystemMessages(token, full));
    }

    @GetMapping("/common/{full}")
    public ResponseEntity<List<AloneNewDto>> getCommonNewsAll(@PathVariable("full") boolean full) {
        String token = (String) tokenService.getTokenUser().getClaims().get("sub");
        if (infoHolder.getOnlineInfo().get(token) == null)
            return ResponseEntity.ok(new ArrayList<>());
        return ResponseEntity.ok(messageService.getCommonMessages(token, full));
    }

    @GetMapping("/family/{full}")
    public ResponseEntity<List<AloneNewDto>> getFamilyNews(@PathVariable("full") boolean full) {
        String token = (String) tokenService.getTokenUser().getClaims().get("sub");
        List<AloneNewDto> messageList;
        if (full) {
            messageList = messageService.getAllNewsByCategory(token, NewsCategory.FAMILY);
            return ResponseEntity.ok(messageList);
        } else if (infoHolder.getOnlineInfo().get(token) == null)
            return ResponseEntity.ok(new ArrayList<>());
        return ResponseEntity.ok(infoHolder.getOnlineInfo().get(token).getFamilyNews());
    }
    @GetMapping("/private/{full}")
    public ResponseEntity<List<AloneNewDto>> getPrivateNews(@PathVariable("full") boolean full) {
        String token = (String) tokenService.getTokenUser().getClaims().get("sub");
        List<AloneNewDto> messageList;
        if (full) {
            messageList = messageService.getAllNewsByCategory(token, NewsCategory.PRIVATE);
            return ResponseEntity.ok(messageList);
        } else if (infoHolder.getOnlineInfo().get(token) == null)
            return ResponseEntity.ok(new ArrayList<>());
        return ResponseEntity.ok(infoHolder.getOnlineInfo().get(token).getIndividualNews());
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
