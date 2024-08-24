package com.example.controller;

import com.example.dto.FamilyMemberDto;
import com.example.service.ServiceFM;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/FamilyMembers/")
public class familyMemberController {
    ServiceFM serviceFM;

    @GetMapping("/{id}")
    public FamilyMemberDto getFamilyMember(@PathVariable Long id) {
        return serviceFM.getFamilyMember(id);
    }

    @PostMapping("")
    public ResponseEntity<FamilyMemberDto> addFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        return ResponseEntity.ok(serviceFM.saveNewFamilyMember(familyMemberDto));
    }

    @GetMapping("")
    public Collection<FamilyMemberDto> getAllFamilyMember() {
        return serviceFM.getAllFamilyMembers();
    }

    @DeleteMapping("/{id}")
    public String removeFamilyMember(@PathVariable Long id) {
        return serviceFM.removeFamilyMember(id);
    }
    @PatchMapping("")
    public ResponseEntity<FamilyMemberDto> editFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        return ResponseEntity.ok(serviceFM.updateFamilyMember(familyMemberDto));
    }
}
