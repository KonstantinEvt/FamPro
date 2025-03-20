package com.example.feign;

import com.example.config.FeignRequestIntercepter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "FAMPRO-FAMILY", configuration = FeignRequestIntercepter.class, fallbackFactory = FamilyConnectionClient.checkGuardFallbackFactory.class)
public interface FamilyConnectionClient {

    @GetMapping("/guard/check/{uuid}")
    boolean checkRights(@PathVariable("uuid") UUID uuid);


    @Component
    class checkGuardFallbackFactory implements FallbackFactory<FallFamilyConnection> {

        @Override
        public FallFamilyConnection create(Throwable cause) {
            return new FallFamilyConnection(cause.getMessage());
        }
    }

    @Slf4j
    record FallFamilyConnection(String reason) implements FamilyConnectionClient {
        @Override
        public boolean checkRights(UUID uuid) {
            return false;
        }
    }
}