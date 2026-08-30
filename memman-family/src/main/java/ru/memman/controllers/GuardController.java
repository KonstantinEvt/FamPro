package ru.memman.controllers;

import ru.memman.dtos.FamilyMemberDto;
import ru.memman.enums.CheckStatus;
import ru.memman.enums.SecretLevel;
import ru.memman.service.FacadeService;
import ru.memman.service.GuardService;
import ru.memman.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/family/guard")
public class GuardController {
    private FacadeService facadeService;
    private TokenService tokenService;
    private GuardService guardService;

    @PostMapping("/addLinkGuard")
    public CheckStatus addGuardByLink(@RequestBody FamilyMemberDto familyMemberDto){
        return facadeService.addGuardByLink(familyMemberDto, tokenService.getTokenUser());
    }
    @GetMapping("/getLinkGuard")
    public String getLinkingPersonOfGuard(){
        return guardService.getLinkingPersonOfGuard((String) tokenService.getTokenUser().getClaims().get("sub"));
    }
    @GetMapping("/checkGuards/{uuid}")
    public SecretLevel getGuardsStatus(@PathVariable UUID uuid){
        return facadeService.getGuardStatus(uuid, (String) tokenService.getTokenUser().getClaims().get("sub"));
    }
}
