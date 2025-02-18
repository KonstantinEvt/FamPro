package com.example.controllers;

import com.example.dtos.FamilyMemberDto;
import com.example.service.RecipientService;
import com.example.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/recipient")
@AllArgsConstructor
public class RecipientController {
    private TokenService tokenService;
    private RecipientService recipientService;

    @GetMapping("/podpisota")
    public Set<FamilyMemberDto> getPodpisota(){
        return new HashSet<>();
    }
}
