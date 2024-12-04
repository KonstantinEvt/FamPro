package org.example.feign;

import com.example.dtos.FamilyMemberDto;
import lombok.extern.slf4j.Slf4j;
import org.example.config.FeignRequestIntercepter;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "FAMPRO-STORAGE", configuration = FeignRequestIntercepter.class, fallbackFactory = BaseOverClient.LinkedFallbackFactory.class)
public interface BaseOverClient {

    @GetMapping("/family_members/database/link/{id}")
    FamilyMemberDto linkFamilyMember(@PathVariable("id") Long id);

     @Component
    class LinkedFallbackFactory implements FallbackFactory<FallFamilyMember> {

        @Override
        public FallFamilyMember create(Throwable cause) {
            return new FallFamilyMember(cause.getMessage());
        }
    }

    @Slf4j
    record FallFamilyMember(String reason) implements BaseOverClient {



        @Override
        public FamilyMemberDto linkFamilyMember(Long id) {
            throw new RuntimeException(reason);
        }


}}


