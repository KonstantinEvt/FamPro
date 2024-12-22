package org.example.config;

import com.example.dtos.Directive;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;

@Configuration
public class TaskResource {

    @Bean("directiveResource")
    LinkedList<Directive> sender() {
        return new LinkedList<>();
    }
}
