package com.example.config;

import com.example.dtos.DirectiveGuards;
import com.example.dtos.FamilyDirective;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;

@Configuration
public class TaskResource {

    @Bean("directiveResource")
    LinkedList<FamilyDirective> sender() {
        return new LinkedList<>();
    }
    @Bean("directiveGuards")
    LinkedList<DirectiveGuards> letterToGuards() {
        return new LinkedList<>();
    }
}
