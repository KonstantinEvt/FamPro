package com.example.feign;

import com.example.config.FeignRequestIntercepter;
import com.example.dtos.FamilyMemberDto;
import com.example.dtos.SecurityDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "FAMPRO-STORAGE", configuration = FeignRequestIntercepter.class, fallbackFactory = FamilyMemberClient.FamilyMemberFallbackFactory.class)
public interface FamilyMemberClient {

    @GetMapping("/family_members/database/{id}")
    FamilyMemberDto getFamilyMemberById(@PathVariable("id") Long id);
    @GetMapping("/family_members/database/i")
    FamilyMemberDto getYourself();
    @PostMapping("/family_members/database/get")
    FamilyMemberDto getFamilyMember(@RequestBody FamilyMemberDto familyMemberDto);
    @PostMapping("/family_members/database/get/extended")
    FamilyMemberDto getExtendedInfoFamilyMember(@RequestBody SecurityDto securityDto);
    @PostMapping("/family_members/database/")
    FamilyMemberDto addFamilyMember(@RequestBody FamilyMemberDto familyMemberDto);

    @PostMapping("/family_members/database/edit")
    FamilyMemberDto editFamilyMember(@RequestBody FamilyMemberDto familyMemberDto);


@Component
class FamilyMemberFallbackFactory implements FallbackFactory<FallFamilyMember> {

    @Override
    public FallFamilyMember create(Throwable cause) {
        return new FallFamilyMember(cause.getMessage());
    }
}

@Slf4j
record FallFamilyMember(String reason) implements FamilyMemberClient {

    @Override
    public FamilyMemberDto getFamilyMemberById(Long id) {
        throw new RuntimeException(reason);
    }

    @Override
    public FamilyMemberDto getYourself() {
        throw new RuntimeException(reason);
    }

    @Override
    public FamilyMemberDto getFamilyMember(FamilyMemberDto familyMemberDto) {
        throw new RuntimeException(reason);
    }

    @Override
    public FamilyMemberDto getExtendedInfoFamilyMember(SecurityDto securityDto) {
        throw new RuntimeException(reason);
    }

    @Override
    public FamilyMemberDto addFamilyMember(FamilyMemberDto familyMemberDto) {
        throw new RuntimeException(reason);
    }

    @Override
    public FamilyMemberDto editFamilyMember(FamilyMemberDto familyMemberDto) {
        throw new RuntimeException(reason);
    }
}}