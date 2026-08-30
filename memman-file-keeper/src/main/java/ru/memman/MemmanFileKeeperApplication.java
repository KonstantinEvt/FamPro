package ru.memman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MemmanFileKeeperApplication {
    public static void main(String[] args) {
        SpringApplication.run(MemmanFileKeeperApplication.class, args);
    }
}