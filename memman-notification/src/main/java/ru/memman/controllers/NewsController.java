package ru.memman.controllers;

import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class NewsController {
    private TokenService tokenService;
    private MessageService messageService;
    private final StandardInfoHolder infoHolder;

    @GetMapping("/get/system/{full}/{localisation}")
    public ResponseEntity<List<AloneNewDto>> getSystemNews(@PathVariable("full") boolean full, @PathVariable("localisation") String loc) {
        String token = (String) tokenService.getTokenUser().getClaims().get("sub");
        log.info("User: {} getting system news. Localisation: {}", token, loc);
        if (infoHolder.getOnlineInfo().get(token) == null)
            return ResponseEntity.ok(new ArrayList<>());
        return ResponseEntity.ok(messageService.getSystemMessages(token, full,tokenService.getLocalisation(loc)));
    }

    @GetMapping("/get/common/{full}/{localisation}")
    public ResponseEntity<List<AloneNewDto>> getCommonNews(@PathVariable("full") boolean full, @PathVariable("localisation") String loc) {
        String token = (String) tokenService.getTokenUser().getClaims().get("sub");
        log.info("User: {} getting common news. Localisation: {}", token, loc);
        if (infoHolder.getOnlineInfo().get(token) == null)
            return ResponseEntity.ok(new ArrayList<>());
        return ResponseEntity.ok(messageService.getCommonMessages(token, full,tokenService.getLocalisation(loc)));
    }

    @GetMapping("/get/family/{full}/{localisation}")
    public ResponseEntity<List<AloneNewDto>> getFamilyNews(@PathVariable("full") boolean full, @PathVariable("localisation") String loc) {
        String token = (String) tokenService.getTokenUser().getClaims().get("sub");
        log.info("User: {} getting family news. Localisation: {}", token, loc);
        List<AloneNewDto> messageList;
        if (full) {
            messageList = messageService.getAllNewsByCategory(token, NewsCategory.FAMILY,loc);
            return ResponseEntity.ok(messageList);
        } else if (infoHolder.getOnlineInfo().get(token) == null)
            return ResponseEntity.ok(new ArrayList<>());

        return ResponseEntity.ok(messageService.getFreshNewsByCategory(NewsCategory.FAMILY,token,loc));
    }
    @GetMapping("/get/private/{full}/{localisation}")
    public ResponseEntity<List<AloneNewDto>> getPrivateNews(@PathVariable("full") boolean full, @PathVariable("localisation") String loc) {
        String token = (String) tokenService.getTokenUser().getClaims().get("sub");
        log.info("User: {} getting private news. Localisation: {}", token, loc);
        List<AloneNewDto> messageList;
        if (full) {
            messageList = messageService.getAllNewsByCategory(token, NewsCategory.PRIVATE,loc);
            return ResponseEntity.ok(messageList);
        } else if (infoHolder.getOnlineInfo().get(token) == null)
            return ResponseEntity.ok(new ArrayList<>());
        return ResponseEntity.ok(messageService.getFreshNewsByCategory(NewsCategory.PRIVATE,token,loc));
    }
    @GetMapping("/read/{category}/{id}")
    public void readNews(@PathVariable("category") Attention category, @PathVariable("id") String id) {
        messageService.readOrRemoveGlobalMessage((String) tokenService.getTokenUser().getClaims().get("sub"), category, id, true);
    }

    @GetMapping("/remove/{category}/{id}")
    public void removeNews(@PathVariable("category") Attention category, @PathVariable("id") String id) {
        messageService.readOrRemoveGlobalMessage((String) tokenService.getTokenUser().getClaims().get("sub"), category, id, false);
    }
    @GetMapping("/rights/{externUuid}")
    public boolean checkRights(@PathVariable("externUuid") String externUuid) {
        return messageService.checkRights((String) tokenService.getTokenUser().getClaims().get("sub"), externUuid);
    }
}
