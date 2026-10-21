package ru.memman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.memman.dtos.OnlineUserDto;

import java.util.LinkedList;

@Configuration
public class TaskResource {

    @Bean("inlineResource")
    LinkedList<OnlineUserDto> inline() {
        return new LinkedList<>();
    }

}
