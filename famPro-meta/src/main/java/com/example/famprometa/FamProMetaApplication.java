package com.example.famprometa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FamProMetaApplication {

    public static void main(String[] args) {
        SpringApplication.run(FamProMetaApplication.class, args);
    }

}