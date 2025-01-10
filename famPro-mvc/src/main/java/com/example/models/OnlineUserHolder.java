package com.example.models;

import com.example.dtos.TokenUser;
import com.example.services.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.WeakHashMap;

@Component
@AllArgsConstructor
public class OnlineUserHolder {
    private static final Map<String,SimpleUserInfo> onlineUsers=new WeakHashMap<>();
    private TokenService tokenService;


    public void addUser(SimpleUserInfo simpleUserInfo){
       onlineUsers.put(simpleUserInfo.getUserName(),simpleUserInfo);
    }
    public SimpleUserInfo getSimpleUser(){
        TokenUser tokenUser=tokenService.getTokenUser();
        if (!onlineUsers.containsKey(tokenUser.getUsername())) {
            addUser(new SimpleUserInfo(tokenService));
        }
        return onlineUsers.get(tokenUser.getUsername());
    };
}
