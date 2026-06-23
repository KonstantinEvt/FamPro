package com.example.controllers;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.SecurityDto;
import com.example.enums.Localisation;
import com.example.enums.SecretLevel;
import com.example.service.FamilyMembersValidationService;
import com.ibm.icu.impl.coll.Collation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Collection;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/validation/family_member")
public class ValidationFamilyMember {
    private final FamilyMembersValidationService familyMembersValidationService;

    @GetMapping("/{id}/{localisation}")
    public FamilyMemberDto getFamilyMemberById(@PathVariable("id") Long id, @PathVariable Localisation localisation) throws ParseException {
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

    @PostMapping("/get/extended")
    public FamilyMemberDto getExtendedInfoFamilyMember(@RequestBody SecurityDto securityDto) throws ParseException {
        return familyMembersValidationService.getExtendedInfoFamilyMember(securityDto);
    }

    ;

    @PostMapping("")
    public ResponseEntity<FamilyMemberDto> addFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        return ResponseEntity.ok(familyMembersValidationService.addFamilyMember(familyMemberDto));
    }

    @PostMapping("/edit")
    public ResponseEntity<FamilyMemberDto> editFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        return ResponseEntity.ok(familyMembersValidationService.editFamilyMember(familyMemberDto));
    }

    @GetMapping("/firstCreator/{localisation}")
    public ResponseEntity<Collection<FamilyMemberDto>> getMembersByFirstCreator(@PathVariable Localisation localisation) {
        return ResponseEntity.ok(familyMembersValidationService.getMembersByFirstCreator(localisation));
    }
    @GetMapping("/familyTree/{uuid}/{choice}/{localisation}")
    public ResponseEntity<Collection<FamilyMemberDto>> getFamilyTreeOfMember(@PathVariable("uuid") UUID uuid, @PathVariable("choice") SecretLevel choice, @PathVariable Localisation localisation) throws ParseException {
        return ResponseEntity.ok(familyMembersValidationService.getFamilyTreeOfMember(uuid, choice, localisation));
    }
}