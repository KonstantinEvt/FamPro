package ru.memman.config;

import ru.memman.dtos.Directive;
import ru.memman.dtos.DirectiveGuards;
import ru.memman.dtos.FamilyDirective;
import ru.memman.dtos.OnlineUserDto;
import ru.memman.enums.Localisation;
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
    LinkedList<OnlineUserDto> inline() {
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
