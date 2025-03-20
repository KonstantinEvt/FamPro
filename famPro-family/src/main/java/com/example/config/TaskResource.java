package com.example.config;

import com.example.dtos.Directive;
import com.example.dtos.DirectiveGuards;
import com.example.dtos.FamilyDirective;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;

@Configuration
public class TaskResource {

    @Bean("directiveGuards")
    LinkedList<DirectiveGuards> letterToGuards() {
        return new LinkedList<>();
    }
    @Bean("inlineResource")
    LinkedList<FamilyDirective> inline() {
        return new LinkedList<>();
    }
    @Bean("storageDirective")
    LinkedList<FamilyDirective> storageDirective() {
        return new LinkedList<>();
    }
}
