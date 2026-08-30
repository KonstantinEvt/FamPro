package ru.memman.config;

import ru.memman.dtos.DirectiveGuards;
import ru.memman.dtos.OnlineUserDto;
import ru.memman.entity.BaseUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class TaskResource {

//    @Bean("inlineResource")
//    LinkedList<FamilyDirective> inline() {
//        return new LinkedList<>();
//    }
    @Bean("actualUsersMap")
    Map<String, BaseUser> getActualUserMap(){return new ConcurrentHashMap<>();}

    @Bean("actualTimeMap")
    Map<String, Timestamp> getActualTimeMap(){return new ConcurrentHashMap<>();}

    @Bean("onlineUsersMap")
    Map<String, OnlineUserDto> getInlineUserMap(){return new ConcurrentHashMap<>();}

    @Bean("inlineResource")
    LinkedList<DirectiveGuards> inline() {
        return new LinkedList<>();
    }
}
