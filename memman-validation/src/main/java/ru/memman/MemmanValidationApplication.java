package ru.memman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MemmanValidationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemmanValidationApplication.class, args);
    }

}
