package ru.memman.controllers;

import ru.memman.dtos.FamilyDto;
import ru.memman.entity.Family;
import ru.memman.service.FacadeService;
import ru.memman.service.FamilyServiceImp;
import ru.memman.service.GuardService;
import ru.memman.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/family/operation")
@AllArgsConstructor
public class FamilyController {
    private FacadeService facadeService;
    private TokenService tokenService;
    private GuardService guardService;
    private FamilyServiceImp familyService;

    @GetMapping("/get/{uuid}")
    public FamilyDto getFamilyByUuid(@PathVariable("uuid") UUID uuid){
        return familyService.getFamilyByUuid(uuid,(String) tokenService.getTokenUser().getClaims().get("sub"));
    }
    @GetMapping("/get/{name}")
    public FamilyDto getFamilyByName(@PathVariable("name") String name){
        return familyService.getFamilyByName(name,(String) tokenService.getTokenUser().getClaims().get("sub"));
    }
}
