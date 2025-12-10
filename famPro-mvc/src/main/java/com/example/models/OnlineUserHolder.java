package com.example.models;

import com.example.dtos.DirectiveGuards;
import com.example.dtos.TokenUser;
import com.example.enums.KafkaOperation;
import com.example.enums.SwitchPosition;
import com.example.enums.UserRoles;
import com.example.services.TokenService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

@Component
@Log4j2
public class OnlineUserHolder {
    private static final Map<String, SimpleUserInfo> onlineUsers = new WeakHashMap<>();
    private final TokenService tokenService;
    private final LinkedList<DirectiveGuards> inline;


    public OnlineUserHolder(TokenService tokenService,
                            @Qualifier("inlineResource") LinkedList<DirectiveGuards> inline) {
        this.tokenService = tokenService;
        this.inline = inline;
    }

    public void addUser(SimpleUserInfo simpleUserInfo) {
        onlineUsers.put(simpleUserInfo.getId(), simpleUserInfo);
    }

    public void changeUserRole(String id, UserRoles userRoles) {
        if (onlineUsers.containsKey(id)) {
            onlineUsers.get(id).setRole(userRoles.getNameSSO());
        }
    }

    public SimpleUserInfo getSimpleUser() {
        TokenUser tokenUser = tokenService.getTokenUser();
        String inlineUuid = (String) tokenUser.getClaims().get("sub");
        SimpleUserInfo simpleUserInfo;
        if (!onlineUsers.containsKey(inlineUuid)) {
            simpleUserInfo = new SimpleUserInfo(tokenUser);
            addUser(simpleUserInfo);
            log.info("add OnlineUser: ".concat(tokenUser.getUsername()).concat(" to OnlineHolder"));
            String loc = (String) tokenUser.getClaims().get("localisation");
            tokenService.setGlobalLocalisation(inlineUuid, Objects.requireNonNullElse(loc, "RU"));
        } else simpleUserInfo = onlineUsers.get(inlineUuid);
        inline.add(DirectiveGuards.builder()
                .tokenUser(inlineUuid)
                .person(simpleUserInfo.getNickName())
                .localisation(simpleUserInfo.getLocalisation())
                .switchPosition(SwitchPosition.MAIN)
                .operation(KafkaOperation.GET).build());
        return simpleUserInfo;
    }

    public String getLocalisation() {
        TokenUser tokenUser = tokenService.getTokenUser();
        String inlineUuid = (String) tokenUser.getClaims().get("sub");
        return (onlineUsers.containsKey(inlineUuid)) ? onlineUsers.get(inlineUuid).getLocalisation().toString().toLowerCase() : (String) tokenUser.getClaims().get("localisation");
    }
}
