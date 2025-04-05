package com.example.controllers;

import com.example.dtos.FamilyMemberDto;
import com.example.enums.CheckStatus;
import com.example.enums.SecretLevel;
import com.example.service.GuardService;
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
    private GuardService guardService;
    @PostMapping("/addLinkGuard")
    public CheckStatus addGuardByLink(@RequestBody FamilyMemberDto familyMemberDto){
        return incomingService.addGuardByLink(familyMemberDto, tokenService.getTokenUser());
    }
    @GetMapping("/getLinkGuard")
    public String getGuardByLink(){
        return guardService.getLinkGuard((String) tokenService.getTokenUser().getClaims().get("sub"));
    }
    @GetMapping("/check/{uuid}")
    public boolean checkRightsToEdit(@PathVariable UUID uuid){
        return incomingService.checkStatusCheckChecked(uuid, (String) tokenService.getTokenUser().getClaims().get("sub"));
    }
    @GetMapping("/checkGuards/{uuid}")
    public SecretLevel getGuardsStatus(@PathVariable UUID uuid){
        return incomingService.getGuardStatus(uuid, (String) tokenService.getTokenUser().getClaims().get("sub"));
    }
}
