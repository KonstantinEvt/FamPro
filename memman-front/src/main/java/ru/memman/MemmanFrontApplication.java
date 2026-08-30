package ru.memman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MemmanFrontApplication {
    public static void main(String[] args) {
        SpringApplication.run(MemmanFrontApplication.class, args);
    }
}