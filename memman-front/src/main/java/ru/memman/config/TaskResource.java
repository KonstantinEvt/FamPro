package ru.memman.config;

import ru.memman.dtos.DirectiveGuards;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;

@Configuration
public class TaskResource {

    @Bean("inlineResource")
    LinkedList<DirectiveGuards> inline() {
        return new LinkedList<>();
    }

}
