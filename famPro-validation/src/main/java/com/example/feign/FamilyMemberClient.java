package com.example.feign;

import com.example.config.FeignRequestIntercepter;
import com.example.dtos.FamilyMemberDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "FAMPRO-STORAGE", configuration = FeignRequestIntercepter.class)
public interface FamilyMemberClient {

    @GetMapping("/family_members/database/{id}")
    FamilyMemberDto getFamilyMemberById(@PathVariable("id") Long id);

    @PostMapping("/family_members/database/get")
    FamilyMemberDto getFamilyMember(@RequestBody FamilyMemberDto familyMemberDto);

    @PostMapping("/family_members/database/")
    FamilyMemberDto addFamilyMember(@RequestBody FamilyMemberDto familyMemberDto);

    @PostMapping("/family_members/database/edit")
    FamilyMemberDto editFamilyMember(@RequestBody FamilyMemberDto familyMemberDto);
}

