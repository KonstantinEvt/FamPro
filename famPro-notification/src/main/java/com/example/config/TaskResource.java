package com.example.config;

import com.example.dtos.AloneNewDto;
import com.example.dtos.Directive;
import com.example.dtos.DirectiveGuards;
import com.example.dtos.FamilyDirective;
import com.example.models.StandardInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Configuration
public class TaskResource {

//    @Bean("inlineResource")
//    LinkedList<FamilyDirective> inline() {
//        return new LinkedList<>();
//    }
    @Bean("directiveResource")
    LinkedList<Directive> sender() {
        return new LinkedList<>();
    }
    @Bean("directiveVoting")
    LinkedList<Directive> senderVoting() {
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
}
