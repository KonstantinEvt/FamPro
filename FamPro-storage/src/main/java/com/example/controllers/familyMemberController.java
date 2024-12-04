package com.example.controllers;

import com.example.dtos.FamilyMemberDto;
import com.example.service.FamilyMemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/family_members/")
public class FamilyMemberController {
    private final FamilyMemberService familyMemberService;

    @GetMapping("/database/{id}")
    public FamilyMemberDto getFamilyMember(@PathVariable Long id) {
        return familyMemberService.getFamilyMemberById(id);
    }
    @GetMapping("/database/link/{id}")
    public FamilyMemberDto linkFamilyMember(@PathVariable Long id) {
        return familyMemberService.linkFamilyMember(id);
    }

    @PostMapping("/database/")
    public ResponseEntity<FamilyMemberDto> addFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        return ResponseEntity.ok(familyMemberService.addFamilyMember(familyMemberDto));
    }

    @GetMapping("/database/all")
    public Collection<FamilyMemberDto> getAllFamilyMember() {
        return familyMemberService.getAllFamilyMembers();
    }

    @PostMapping("/database/get")
    public FamilyMemberDto getFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        FamilyMemberDto dto=familyMemberService.getFamilyMember(familyMemberDto);
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
}
