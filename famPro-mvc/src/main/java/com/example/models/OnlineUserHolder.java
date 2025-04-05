package com.example.models;

import com.example.dtos.FamilyDirective;
import com.example.dtos.TokenUser;
import com.example.enums.KafkaOperation;
import com.example.enums.Localisation;
import com.example.enums.SwitchPosition;
import com.example.enums.UserRoles;
import com.example.services.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Locale;
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

    public void changeUserRole(String id, UserRoles userRoles) {
        if (onlineUsers.containsKey(id)) {
            onlineUsers.get(id).setRole(userRoles.getNameSSO());
        }
        ;
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

    public String getLocalisation() {
        TokenUser tokenUser = tokenService.getTokenUser();
        String inlineUuid = (String) tokenUser.getClaims().get("sub");
        return (onlineUsers.containsKey(inlineUuid)) ? onlineUsers.get(inlineUuid).getLocalisation().toString().toLowerCase():(String) tokenUser.getClaims().get("localisation");
    }

    ;
}
