package com.example.config;

import com.example.dtos.*;
import com.example.entity.FamilyMemberInfo;
import com.example.entity.MainContact;
import com.example.enums.SecretLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
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

    @Bean("tempGuardStatus")
    Map<String, FamilyMemberDto> tempGuardStatus() {
        return new ConcurrentHashMap<>();
    }

    @Bean("tempExtendedDto")
    Map<String, FamilyMemberDto> tempExtendedDto() {
        return new ConcurrentHashMap<>();
    }

    @Bean("tempUpdateMap")
    Map<Long, Timestamp> lastUpdateMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean("tempMainContact")
    Map<String, MainContact> tempMainContact(){return new ConcurrentHashMap<>();};
}
