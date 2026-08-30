package ru.memman.config;

import ru.memman.dtos.Directive;
import ru.memman.dtos.DirectiveGuards;
import ru.memman.dtos.FamilyDirective;
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
