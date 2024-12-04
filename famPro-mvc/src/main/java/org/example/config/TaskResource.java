package org.example.config;

import com.example.dtos.TokenUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;

@Configuration
public class TaskResource {

    @Bean("tokenUserResource")
    LinkedList<TokenUser> sendler() {
        return new LinkedList<TokenUser>();
    }
}
