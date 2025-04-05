package com.example.config;

import com.example.dtos.Directive;
import com.example.dtos.DirectiveGuards;
import com.example.dtos.FamilyDirective;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    @Bean("directivePhotos")
    LinkedList<Directive> photoDirective() {
        return new LinkedList<>();
    }

    @Bean("tempPhotoAccept")
    Map<String, String> tempPhotoAccept() {
        return new ConcurrentHashMap<>();
    }
}
