package com.example.controllers;

import com.example.dtos.FamilyMemberDto;
import com.example.service.ProbeServ;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
@AllArgsConstructor
public class FamilyController {
    private final ProbeServ probeServ;
@GetMapping("{id}")
    public FamilyMemberDto getMember(@PathVariable Long id){
    return probeServ.getFamilyMem(id);
    }
}