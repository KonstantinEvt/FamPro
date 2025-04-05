package com.example.feign;

import com.example.config.FeignRequestIntercepter;
import com.example.dtos.FamilyMemberDto;
import com.example.dtos.TokenUser;
import com.example.enums.CheckStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "FAMPRO-FAMILY", configuration = FeignRequestIntercepter.class, fallbackFactory = FamilyClient.LinkedFallbackFactory.class)
public interface FamilyClient {

    @PostMapping("/guard/addLinkGuard")
    CheckStatus addGuard(FamilyMemberDto familyMemberDto);

     @Component
    class LinkedFallbackFactory implements FallbackFactory<FallGuard> {

        @Override
        public FallGuard create(Throwable cause) {
            return new FallGuard(cause.getMessage());
        }
    }

    @Slf4j
    record FallGuard(String reason) implements FamilyClient {



        @Override
        public CheckStatus addGuard(FamilyMemberDto familyMemberDto) {
            throw new RuntimeException(reason);
        }


}}


