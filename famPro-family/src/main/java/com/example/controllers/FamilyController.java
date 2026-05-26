package com.example.controllers;

import com.example.dtos.FamilyDto;
import com.example.entity.Family;
import com.example.service.FacadeService;
import com.example.service.FamilyServiceImp;
import com.example.service.GuardService;
import com.example.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/family")
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
