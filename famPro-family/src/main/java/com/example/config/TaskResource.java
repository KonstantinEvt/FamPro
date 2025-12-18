package com.example.config;

import com.example.dtos.Directive;
import com.example.dtos.DirectiveGuards;
import com.example.dtos.FamilyDirective;
import com.example.enums.Localisation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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

    @Bean("checkLevelDirective")
    LinkedList<DirectiveGuards> checkLevelDirective() {
        return new LinkedList<>();
    }
    @Bean("contactDirective")
    LinkedList<DirectiveGuards> contactDirective() {
        return new LinkedList<>();
    }

    @Bean("cloakDirective")
    LinkedList<Directive> cloakDirective() {
        return new LinkedList<>();
    }
    @Bean("tempLocalisation")
    Map<UUID, Localisation> tempLocalisation(){return new ConcurrentHashMap<>();}
}
