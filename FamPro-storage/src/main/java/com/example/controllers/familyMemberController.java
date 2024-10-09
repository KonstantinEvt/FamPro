package com.example.controllers;

import com.example.dtos.FamilyMemberDto;
import com.example.service.FamilyMemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/FamilyMembers/")
public class FamilyMemberController {
    private final FamilyMemberService familyMemberService;

    @GetMapping("/{id}")
    public FamilyMemberDto getFamilyMember(@PathVariable Long id) {
        return familyMemberService.getFamilyMember(id);
    }

    @PostMapping("")
    public ResponseEntity<FamilyMemberDto> addFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        return ResponseEntity.ok(familyMemberService.addFamilyMember(familyMemberDto));
    }

    @GetMapping("")
    public Collection<FamilyMemberDto> getAllFamilyMember() {
        return familyMemberService.getAllFamilyMembers();
    }

    @DeleteMapping("/{id}")
    public String removeFamilyMember(@PathVariable Long id) {
        return familyMemberService.removeFamilyMember(id);
    }

    @PatchMapping("")
    public ResponseEntity<FamilyMemberDto> editFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        return ResponseEntity.ok(familyMemberService.updateFamilyMember(familyMemberDto));
    }
}
