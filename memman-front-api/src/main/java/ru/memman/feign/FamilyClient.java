package ru.memman.feign;

import ru.memman.config.FeignRequestIntercepter;
import ru.memman.dtos.FamilyMemberDto;
import ru.memman.dtos.TokenUser;
import ru.memman.enums.CheckStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "MEMMAN-FAMILY", configuration = FeignRequestIntercepter.class, fallbackFactory = FamilyClient.LinkedFallbackFactory.class)
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


