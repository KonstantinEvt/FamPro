package ru.memman.feign;

import lombok.extern.log4j.Log4j2;
import ru.memman.dtos.TokenUser;
import ru.memman.config.FeignRequestIntercepter;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "MEMMAN-KEY", configuration = FeignRequestIntercepter.class, fallbackFactory = KeyCloakManageClient.KeyCloakFallbackFactory.class)
public interface KeyCloakManageClient {

    @GetMapping("/manage/locale/{loc}")
    void chooseLocalisation(@PathVariable("loc") String loc);

    @Log4j2
    @Component
    class KeyCloakFallbackFactory implements FallbackFactory<FallKey> {
        @Override
        public FallKey create(Throwable cause) {
            return new FallKey(cause.getMessage());
        }
    }

    record FallKey(String reason) implements KeyCloakManageClient {

        @Override
        public void chooseLocalisation(String loc) {
            throw  new RuntimeException (reason);
        }
    }
}