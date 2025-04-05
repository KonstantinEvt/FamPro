package com.example.controllers;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.SecurityDto;
import com.example.service.FamilyMemberService;
import com.example.service.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/family_members")
public class FamilyMemberController {
    private final FamilyMemberService familyMemberService;

    @GetMapping("/database/{id}")
    public FamilyMemberDto getFamilyMember(@PathVariable Long id) {
        return familyMemberService.getFamilyMemberById(id);
    }
//    @GetMapping("/database/link/{id}")
//    public FamilyMemberDto linkFamilyMember(@PathVariable Long id) {
//        return familyMemberService.linkFamilyMember(id);
//    }

    @PostMapping("/database/")
    public ResponseEntity<FamilyMemberDto> addFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        FamilyMemberDto dto = familyMemberService.addFamilyMember(familyMemberDto);
        System.out.println(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/database/all")
    public Collection<FamilyMemberDto> getAllFamilyMember() {
        return familyMemberService.getAllFamilyMembers();
    }

    @PostMapping("/database/get")
    public FamilyMemberDto getFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        FamilyMemberDto dto = familyMemberService.getFamilyMember(familyMemberDto);
        return dto;
    }

    @DeleteMapping("/database/{id}")
    public String removeFamilyMember(@PathVariable Long id) {
        return familyMemberService.removeFamilyMember(id);
    }

    @PostMapping("/database/edit")
    public ResponseEntity<FamilyMemberDto> editFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        return ResponseEntity.ok(familyMemberService.updateFamilyMember(familyMemberDto));
    }
@PostMapping("/database/get/extended")
    public FamilyMemberDto getFullFamilyMember(@RequestBody SecurityDto securityDto) {
    log.info("request for extension {}",securityDto);
    FamilyMemberDto dto=familyMemberService.getFullFamilyMember(securityDto);
    log.info("Result of extension: {}",dto);
        return dto;
}
}
