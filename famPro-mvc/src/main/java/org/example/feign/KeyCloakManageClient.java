package org.example.feign;

import com.example.dtos.TokenUser;
import jakarta.websocket.server.PathParam;
import org.example.config.FeignRequestIntercepter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "FAMPRO-KEY", configuration = FeignRequestIntercepter.class)
public interface KeyCloakManageClient {

    @PostMapping("/manage/add")
    TokenUser addUser(@RequestBody TokenUser tokenUser);

    @PostMapping("/manage/edit")
    TokenUser editUser(@RequestBody TokenUser tokenUser);

    @PostMapping("/manage")
   void chooseLocalisation(@RequestBody String loc);
}
