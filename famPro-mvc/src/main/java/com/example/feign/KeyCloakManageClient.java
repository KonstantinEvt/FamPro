package com.example.feign;

import com.example.dtos.TokenUser;
import com.example.config.FeignRequestIntercepter;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "FAMPRO-KEY", configuration = FeignRequestIntercepter.class,fallbackFactory = KeyCloakManageClient.KeyCloakFallbackFactory.class)
public interface KeyCloakManageClient {

    @PostMapping("/manage/add")
    TokenUser addUser(@RequestBody TokenUser tokenUser);

    @PostMapping("/manage/edit")
    TokenUser editUser(@RequestBody TokenUser tokenUser);

    @PostMapping("/manage")
   void chooseLocalisation(@RequestBody String loc);

@Component
class KeyCloakFallbackFactory implements FallbackFactory<FallKey> {
    @Override
    public FallKey create(Throwable cause) {
        return new FallKey(cause.getMessage());
    }
}

record FallKey(String reason) implements KeyCloakManageClient{

    @Override
    public TokenUser addUser(TokenUser tokenUser) {
        throw new RuntimeException(reason);
    }

    @Override
    public TokenUser editUser(TokenUser tokenUser) {
        throw new RuntimeException(reason);
    }

    @Override
    public void chooseLocalisation(String loc) {
        throw new RuntimeException(reason);
    }
}
}