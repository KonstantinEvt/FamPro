package org.example.feign;

import com.example.dtos.FamilyMemberDto;
import org.example.config.FeignRequestIntercepter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "FAMPRO-VALIDATION", configuration = FeignRequestIntercepter.class)
public interface BaseClient {

    @GetMapping("/validation/family_member/{id}")
    FamilyMemberDto getFamilyMemberById(@PathVariable("id") Long id);

    @PostMapping("/validation/family_member/get")
    FamilyMemberDto getFamilyMember(@RequestBody FamilyMemberDto familyMemberDto);

    @PostMapping("/validation/family_member")
    FamilyMemberDto addFamilyMember(@RequestBody FamilyMemberDto familyMemberDto);
    @PostMapping("/validation/family_member/edit")
    FamilyMemberDto editFamilyMember(@RequestBody FamilyMemberDto familyMemberDto);
}

