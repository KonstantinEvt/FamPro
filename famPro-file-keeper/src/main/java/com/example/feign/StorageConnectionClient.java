package com.example.feign;

import com.example.config.FeignRequestIntercepter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "FAMPRO-STORAGE", configuration = FeignRequestIntercepter.class, fallbackFactory = StorageConnectionClient.checkGuardFallbackFactory.class)
public interface StorageConnectionClient {

    @GetMapping("/storage/acceptPhoto/{uuid}")
    boolean checkRights(@PathVariable("uuid") String uuid);

    @Component
    class checkGuardFallbackFactory implements FallbackFactory<FallStorageConnection> {

        @Override
        public FallStorageConnection create(Throwable cause) {
            return new FallStorageConnection(cause.getMessage());
        }
    }

    @Slf4j
    record FallStorageConnection(String reason) implements StorageConnectionClient {
        @Override
        public boolean checkRights(String uuid) {
            return false;
        }
    }
}