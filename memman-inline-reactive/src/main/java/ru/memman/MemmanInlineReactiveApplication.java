package ru.memman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableScheduling
public class MemmanInlineReactiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemmanInlineReactiveApplication.class, args);
    }

}
