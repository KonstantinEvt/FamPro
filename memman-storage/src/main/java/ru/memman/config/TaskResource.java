package ru.memman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.memman.dtos.Directive;
import ru.memman.dtos.DirectiveGuards;
import ru.memman.dtos.FamilyDirective;
import ru.memman.dtos.FamilyMemberDto;
import ru.memman.entity.MainContact;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class TaskResource {

    @Bean("directiveResource")
    LinkedList<LinkedList<FamilyDirective>> sender() { return new LinkedList<>();
    }
    @Bean("personsCount")
    LinkedList<Long> personsCount() {
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
    Map<String, MainContact> tempMainContact(){return new ConcurrentHashMap<>();}
}
