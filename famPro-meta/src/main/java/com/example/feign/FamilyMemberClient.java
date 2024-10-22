package com.example.feign;

import com.example.dtos.FamilyMemberDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@FeignClient(name = "FAMPRO-STORAGE")
public interface FamilyMemberClient {
    @GetMapping("/database/FamilyMembers/{id}")
    FamilyMemberDto getFamilyMember(@PathVariable Long id);

    @PostMapping("/database")
    FamilyMemberDto addFamilyMember(@RequestBody FamilyMemberDto familyMemberDto);

    @GetMapping("/database")
    Collection<FamilyMemberDto> getAllFamilyMembers();

    @DeleteMapping("/database/{id}")
    String removeFamilyMember(@PathVariable Long id);

    @PatchMapping("/database")
    FamilyMemberDto updateFamilyMember(@RequestBody FamilyMemberDto familyMemberDto);
    @GetMapping("/database/save{filename}")
    ResponseEntity<String> saveDataToFile(@PathVariable String filename);

    @GetMapping("/database/recover{filename}")
    ResponseEntity<String> recoverBaseFromFile(@PathVariable String filename);
}
