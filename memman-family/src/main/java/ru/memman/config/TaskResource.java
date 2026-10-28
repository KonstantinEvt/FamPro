package ru.memman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.memman.dtos.*;

import java.util.LinkedList;
import java.util.List;

@Configuration
public class TaskResource {

    @Bean("directiveGuards")
    LinkedList<DirectiveGuards> letterToGuards() {
        return new LinkedList<>();
    }

    @Bean("inlineResource")
    LinkedList<OnlineUserDto> inline() {
        return new LinkedList<>();
    }
    @Bean("checkRelation")
    LinkedList<List<ContactDto>> checkRelation() {
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


}
