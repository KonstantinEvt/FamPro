package com.example.controllers;

import com.example.dtos.FamilyMemberDto;
import com.example.service.FamilyMembersValidationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/validation/family_member")
public class ValidationFamilyMember {
    private final FamilyMembersValidationService familyMembersValidationService;

    @GetMapping("/{id}")
    public FamilyMemberDto getFamilyMemberById(@PathVariable("id") Long id) {
        return familyMembersValidationService.getFamilyMemberById(id);
    }

    @PostMapping("/get")
    public FamilyMemberDto getFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        FamilyMemberDto dto=familyMembersValidationService.getFamilyMember(familyMemberDto);
        System.out.println(dto.getFirstName());
        System.out.println(dto.getMiddleName());
        System.out.println(dto.getLastName());
        System.out.println(dto.getBirthday());
        return dto;
    }

    @PostMapping("")
    public ResponseEntity<FamilyMemberDto> addFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        return ResponseEntity.ok(familyMembersValidationService.addFamilyMember(familyMemberDto));
    }
    @PostMapping("/edit")
    public ResponseEntity<FamilyMemberDto> editFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        return ResponseEntity.ok(familyMembersValidationService.editFamilyMember(familyMemberDto));
}}