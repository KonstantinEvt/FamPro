package com.example.controllers;

import com.example.holders.StandardInfoHolder;
import com.example.dtos.AloneNewDto;
import com.example.models.StandardInfo;
import com.example.service.MailService;
import com.example.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/news")
@AllArgsConstructor
public class NewsController {
    private final MailService service;
    private TokenService tokenService;
    private final StandardInfoHolder infoHolder;

    @GetMapping("/system")
    public ResponseEntity<List<AloneNewDto>> getSystemNews() {
        if (infoHolder.getOnlineInfo().get((String) tokenService.getTokenUser().getClaims().get("sub")) == null)
            return null;
        return ResponseEntity.ok(infoHolder.getOnlineInfo().get((String) tokenService.getTokenUser().getClaims().get("sub")).getSystemNews());
    }

    @GetMapping("/common")
    public ResponseEntity<List<AloneNewDto>> getCommonNews() {
        if (infoHolder.getOnlineInfo().get((String) tokenService.getTokenUser().getClaims().get("sub")) == null)
            return null;
        return ResponseEntity.ok(infoHolder.getOnlineInfo().get((String) tokenService.getTokenUser().getClaims().get("sub")).getCommonNews());
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
//            List<AloneNewDto> empty = new ArrayList<>();
//            AloneNewDto aloneNewDto=new AloneNewDto();
//            aloneNewDto.setId(0L);
//            aloneNewDto.setTextInfo("Приватных сообщений нет");
//            empty.add(aloneNewDto);
            return ResponseEntity.ok(new ArrayList<>());
        }
        return ResponseEntity.ok(infoHolder.getOnlineInfo().get((String) tokenService.getTokenUser().getClaims().get("sub")).getIndividualNews());
    }

    @GetMapping("/counts")
    public int[] getNewsCounts() {
        if (infoHolder.getOnlineInfo().get((String) tokenService.getTokenUser().getClaims().get("sub")) == null)
            return new int[]{0, 0, 0, 0, 0};
        System.out.println("hi");
        return infoHolder.getOnlineInfo().get((String) tokenService.getTokenUser().getClaims().get("sub")).getCounts();
    }
}
