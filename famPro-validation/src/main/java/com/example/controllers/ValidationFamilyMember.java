package com.example.controllers;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.SecurityDto;
import com.example.enums.Localisation;
import com.example.service.FamilyMembersValidationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/validation/family_member")
public class ValidationFamilyMember {
    private final FamilyMembersValidationService familyMembersValidationService;

    @GetMapping("/{id}/{localisation}")
    public FamilyMemberDto getFamilyMemberById(@PathVariable("id") Long id,@PathVariable Localisation localisation) throws ParseException {
        return familyMembersValidationService.getFamilyMemberById(id, localisation);
    }
    @GetMapping("/i/{localisation}")
    public FamilyMemberDto getYourself(@PathVariable Localisation localisation) throws ParseException {
        return familyMembersValidationService.getYourself(localisation);
    }
    @PostMapping("/get")
    public FamilyMemberDto getFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) throws ParseException {
        return familyMembersValidationService.getFamilyMember(familyMemberDto);
    }
    @PostMapping("/get/extended/{localisation}")
    public FamilyMemberDto getExtendedInfoFamilyMember(@RequestBody SecurityDto securityDto, @PathVariable("localisation") Localisation localisation) throws ParseException {
        return familyMembersValidationService.getExtendedInfoFamilyMember(securityDto,localisation);
    };
    @PostMapping("")
    public ResponseEntity<FamilyMemberDto> addFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        return ResponseEntity.ok(familyMembersValidationService.addFamilyMember(familyMemberDto));
    }
    @PostMapping("/edit")
    public ResponseEntity<FamilyMemberDto> editFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        return ResponseEntity.ok(familyMembersValidationService.editFamilyMember(familyMemberDto));
}}