package com.example.config;

import com.example.dtos.Directive;
import com.example.dtos.DirectiveGuards;
import com.example.dtos.FamilyDirective;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;

@Configuration
public class TaskResource {

//    @Bean("directiveResource")
//    LinkedList<Directive> sender() {
//        return new LinkedList<>();
//    }
    @Bean("inlineResource")
    LinkedList<DirectiveGuards> inline() {
        return new LinkedList<>();
    }

    @Bean("languishFamily")
    LinkedList<DirectiveGuards> languishFamily() {
        return new LinkedList<>();
    }
    @Bean("languishStorage")
    LinkedList<DirectiveGuards> languishStorage() {
        return new LinkedList<>();
    }
}
