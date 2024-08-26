package com.example.feign;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("famPro-storage")
public interface StorageBaseClient {
    @GetMapping("/save{filename}")
    ResponseEntity<String> saveDataToFile(@PathVariable String filename);

    @GetMapping("/recover{filename}")
    ResponseEntity<String> recoverBaseFromFile(@PathVariable String filename);

}
