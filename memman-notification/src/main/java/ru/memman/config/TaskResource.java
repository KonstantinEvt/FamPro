package ru.memman.config;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.memman.dtos.*;
import ru.memman.entity.Recipient;
import ru.memman.models.StandardInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.memman.transcriters.AbstractTranscripter;
import ru.memman.transcriters.EnglishTranscripter;
import ru.memman.transcriters.RusTranscripter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Configuration
public class TaskResource {

//    @Bean("directiveResource")
//    LinkedList<Directive> sender() {
//        return new LinkedList<>();
//    }
    @Bean("directiveVoting")
    LinkedList<DirectiveGuards> senderVoting() {
        return new LinkedList<>();
    }

    @Bean("onlineInfo")
    ConcurrentHashMap<String, StandardInfo> onlineInfo() {
        return new ConcurrentHashMap<>();
    }
    @Bean("directiveRights")
    LinkedList<DirectiveGuards> directiveRights() {
        return new LinkedList<>();
    }
    @Bean("systemGlobalMask")
    public List<Integer> systemGlobalMask() {
        return new CopyOnWriteArrayList<>();
    }

    @Bean("commonGlobalMask")
    public List<Integer> commonGlobalMask() {
        return new CopyOnWriteArrayList<>();
    }

    @Bean("systemNewsGlobal")
    public List<AloneNewDto> systemNewsGlobal() {
        return new CopyOnWriteArrayList<>();
    }

    @Bean("commonNewsGlobal")
    public List<AloneNewDto> commonNewsGlobal() {
        return new CopyOnWriteArrayList<>();
    }
    @Bean("inlineResource")
    LinkedList<OnlineUserDto> inline(){return new LinkedList<>();}
    @Bean("systemRecipientHard")
    Recipient systemRecipientHard(){return Recipient.builder()
            .nickName("System")
            .systemReading("")
            .build();}
    @Bean("commonRecipientHard")
    Recipient commonRecipientHard(){return Recipient.builder()
            .nickName("Common")
            .commonReading("")
            .build();}
    @Bean("informerRecipientHard")
    Recipient informerRecipientHard(){return Recipient.builder()
            .nickName("Informer")
            .build();}
    @Bean("rusTranscript")
    AbstractTranscripter rusTranscript() {
        return new RusTranscripter();
    }

    @Bean("englishTranscript")
    AbstractTranscripter englishTranscript() {
        return new EnglishTranscripter();
    }
    @Bean
    Map<String, Set<String>> rightsMap() {
        return new ConcurrentHashMap<>();
    }
}
