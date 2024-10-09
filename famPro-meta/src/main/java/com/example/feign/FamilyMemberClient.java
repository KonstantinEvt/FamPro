package com.example.feign;

import com.example.dtos.FamilyMemberDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@FeignClient(name = "FAMPRO-STORAGE")
public interface FamilyMemberClient {
    @GetMapping("/FamilyMembers/{id}")
    FamilyMemberDto getFamilyMember(@PathVariable Long id);

    @PostMapping("")
    FamilyMemberDto addFamilyMember(@RequestBody FamilyMemberDto familyMemberDto);

    @GetMapping("")
    Collection<FamilyMemberDto> getAllFamilyMembers();

    @DeleteMapping("/{id}")
    String removeFamilyMember(@PathVariable Long id);

    @PatchMapping("")
    FamilyMemberDto updateFamilyMember(@RequestBody FamilyMemberDto familyMemberDto);
    @GetMapping("/save{filename}")
    ResponseEntity<String> saveDataToFile(@PathVariable String filename);

    @GetMapping("/recover{filename}")
    ResponseEntity<String> recoverBaseFromFile(@PathVariable String filename);
}
