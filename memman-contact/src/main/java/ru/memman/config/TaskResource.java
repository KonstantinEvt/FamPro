package ru.memman.config;

import ru.memman.dtos.*;
import ru.memman.entity.Contact;
import ru.memman.entity.RealMen;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.memman.transcriters.AbstractTranscripter;
import ru.memman.transcriters.EnglishTranscripter;
import ru.memman.transcriters.RusTranscripter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class TaskResource {

    @Bean("checkRelation")
    LinkedList<List<ContactDto>> checkRelation() {
        return new LinkedList<>();
    }

    @Bean("inlineResource")
    LinkedList<OnlineUserDto> inline() {
        return new LinkedList<>();
    }

    @Bean("onlineManMap")
    Map<String, RealMen> onlineManMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean("rusTranscript")
    AbstractTranscripter rusTranscript() {
        return new RusTranscripter();
    }

    @Bean("englishTranscript")
    AbstractTranscripter englishTranscript() {
        return new EnglishTranscripter();
    }

    @Bean("onlineContactsMap")
    Map<String, Collection<Contact>> onlineContactsMap() {
        return new ConcurrentHashMap<>();
    }
    @Bean("directiveRights")
    LinkedList<DirectiveGuards> directiveRights() {
        return new LinkedList<>();
    }
}
