package com.example.models;

import com.example.dtos.TokenUser;
import com.example.services.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.WeakHashMap;

@Component
@AllArgsConstructor
@Log4j2
public class OnlineUserHolder {
    private static final Map<String,SimpleUserInfo> onlineUsers=new WeakHashMap<>();
    private TokenService tokenService;


    public void addUser(SimpleUserInfo simpleUserInfo){
       onlineUsers.put(simpleUserInfo.getId(),simpleUserInfo);
    }
    public SimpleUserInfo getSimpleUser(){
        TokenUser tokenUser=tokenService.getTokenUser();
        if (!onlineUsers.containsKey((String)tokenUser.getClaims().get("sub"))) {
            addUser(new SimpleUserInfo(tokenUser));
        log.info("add OnlineUser: ".concat(tokenUser.getUsername()).concat(" to OnlineHolder"));
        }
        return onlineUsers.get((String)tokenUser.getClaims().get("sub"));
    };
}
