package com.example.feign;

import com.example.dtos.FamilyMemberDto;
import com.example.enums.Localisation;
import lombok.extern.slf4j.Slf4j;
import com.example.config.FeignRequestIntercepter;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "FAMPRO-VALIDATION", configuration = FeignRequestIntercepter.class, fallbackFactory = BaseClient.FamilyMemberFallbackFactory.class)
public interface BaseClient {

    @GetMapping("/validation/family_member/{id}/{localisation}")
    FamilyMemberDto getFamilyMemberById(@PathVariable("id") Long id, @PathVariable("localisation") Localisation localisation);
    @GetMapping("/family_members/database/link/{id}")
    FamilyMemberDto linkFamilyMember(@PathVariable("id") Long id);

    @PostMapping("/validation/family_member/get")
    FamilyMemberDto getFamilyMember(@RequestBody FamilyMemberDto familyMemberDto);

    @PostMapping("/validation/family_member")
    FamilyMemberDto addFamilyMember(@RequestBody FamilyMemberDto familyMemberDto);

    @PostMapping("/validation/family_member/edit")
    FamilyMemberDto editFamilyMember(@RequestBody FamilyMemberDto familyMemberDto);


    @Component
    class FamilyMemberFallbackFactory implements FallbackFactory<FallFamilyMember> {

        @Override
        public FallFamilyMember create(Throwable cause) {
            return new FallFamilyMember(cause.getMessage());
        }
    }

    @Slf4j
    record FallFamilyMember(String reason) implements BaseClient {

        @Override
        public FamilyMemberDto getFamilyMemberById(Long id,Localisation localisation) {
            throw new RuntimeException(reason);
        }

        @Override
        public FamilyMemberDto linkFamilyMember(Long id) {
            throw new RuntimeException(reason);
        }

        @Override
        public FamilyMemberDto getFamilyMember(FamilyMemberDto familyMemberDto) {
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
    }
}


