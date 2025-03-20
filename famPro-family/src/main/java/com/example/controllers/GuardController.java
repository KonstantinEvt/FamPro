package com.example.controllers;

import com.example.dtos.FamilyMemberDto;
import com.example.service.IncomingService;
import com.example.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/guard")
public class GuardController {
    private IncomingService incomingService;
    private TokenService tokenService;
    @PostMapping("/addLinkGuard")
    public void addGuardByLink(@RequestBody FamilyMemberDto familyMemberDto){
        incomingService.addGuardByLink(familyMemberDto, tokenService.getTokenUser());
    }
    @GetMapping("/check/{uuid}")
    public boolean checkRightsToEdit(@PathVariable UUID uuid){
        return incomingService.checkStatusCheck(uuid, (String) tokenService.getTokenUser().getClaims().get("sub"));
    }
}
