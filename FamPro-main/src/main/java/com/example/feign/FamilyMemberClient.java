package com.example.feign;

import com.example.dtos.FamilyMemberDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@FeignClient(name = "famPro-storage")
public interface FamilyMemberClient {
    @GetMapping("/{id}")
    FamilyMemberDto  getFamilyMember(@PathVariable Long id);

    @PostMapping("")
    FamilyMemberDto addFamilyMember(@RequestBody FamilyMemberDto familyMemberDto);

    @GetMapping("")
    Collection<FamilyMemberDto> getAllFamilyMembers();

    @DeleteMapping("/{id}")
    String removeFamilyMember(@PathVariable Long id);

    @PatchMapping("")
    FamilyMemberDto updateFamilyMember(@RequestBody FamilyMemberDto familyMemberDto);
}
