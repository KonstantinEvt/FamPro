package com.example.models;

import com.example.dtos.Directive;
import com.example.dtos.FamilyDirective;
import com.example.dtos.FamilyMemberDto;
import com.example.dtos.TokenUser;
import com.example.enums.KafkaOperation;
import com.example.enums.SwitchPosition;
import com.example.services.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;

@Component
@AllArgsConstructor
@Log4j2
public class OnlineUserHolder {
    private static final Map<String, SimpleUserInfo> onlineUsers = new WeakHashMap<>();
    private TokenService tokenService;
    private LinkedList<FamilyDirective> inline;


    public void addUser(SimpleUserInfo simpleUserInfo) {
        onlineUsers.put(simpleUserInfo.getId(), simpleUserInfo);
    }

    public SimpleUserInfo getSimpleUser() {
        TokenUser tokenUser = tokenService.getTokenUser();
        String inlineUuid = (String) tokenUser.getClaims().get("sub");
        SimpleUserInfo simpleUserInfo;
        if (!onlineUsers.containsKey(inlineUuid)) {
            simpleUserInfo = new SimpleUserInfo(tokenUser);
            addUser(simpleUserInfo);
            log.info("add OnlineUser: ".concat(tokenUser.getUsername()).concat(" to OnlineHolder"));
        } else simpleUserInfo = onlineUsers.get(inlineUuid);
        inline.add(FamilyDirective.builder()
                .tokenUser(inlineUuid)
                .person(simpleUserInfo.getNickName())
                .switchPosition(SwitchPosition.MAIN)
                .operation(KafkaOperation.ADD).build());
        return simpleUserInfo;
    }

    ;
}
