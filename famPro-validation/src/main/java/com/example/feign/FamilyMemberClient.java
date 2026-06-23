package com.example.feign;

import com.example.config.FeignRequestIntercepter;
import com.example.dtos.FamilyMemberDto;
import com.example.enums.SecretLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@FeignClient(name = "FAMPRO-FAMILY", configuration = FeignRequestIntercepter.class, fallbackFactory = FamilyMemberClient.FamilyMemberFallbackFactory.class)
public interface FamilyMemberClient {

    @PostMapping("/members/firstCreator")
    Collection<FamilyMemberDto> getMembersByFirstCreator();

    @PostMapping("/members/familyTree/{uuid}/{choice}")
    Collection<FamilyMemberDto> getFamilyTreeOfMember(@PathVariable("uuid") UUID uuid, @PathVariable("choice") SecretLevel choice);
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
        public Collection<FamilyMemberDto> getMembersByFirstCreator() {
            throw new RuntimeException(reason);
        }

        @Override
        public Collection<FamilyMemberDto> getFamilyTreeOfMember(UUID uuid, SecretLevel choice) {
            throw new RuntimeException(reason);
        }
    }
}