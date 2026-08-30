package ru.memman.holders;

import ru.memman.dtos.DirectiveGuards;
import ru.memman.dtos.OnlineUserDto;
import ru.memman.dtos.TokenUser;
import ru.memman.entity.BaseUser;
import ru.memman.service.TokenService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

@Component
@Log4j2
public class OnlineUserHolder {
    private final Map<String, OnlineUserDto> onlineUsers;
    private final LinkedList<DirectiveGuards> inline;


    public OnlineUserHolder(@Qualifier("onlineUsersMap") Map<String, OnlineUserDto> onlineUsers,
                            @Qualifier("inlineResource") LinkedList<DirectiveGuards> inline) {
        this.onlineUsers = onlineUsers;
        this.inline = inline;
    }

    public void addUser(OnlineUserDto onlineUser) {
        onlineUsers.put(onlineUser.getExternUuid(), onlineUser);
    }
    public OnlineUserDto getOnlineUser(String userUuid) {
        return onlineUsers.get(userUuid);
    }
    public ArrayList<OnlineUserDto> getOnlineUsers(){
        return new ArrayList<>(onlineUsers.values());
    }
    public void removeUser(String externUuid) {
        onlineUsers.remove(externUuid);
    }
//    public void changeUserRole(String id, UserRole userRole) {
//        if (onlineUsers.containsKey(id)) {
//            onlineUsers.get(id).setRole(userRole.getNameSSO());
//        }
//    }

//    public SimpleUserInfo getSimpleUser() {
//        TokenUser tokenUser = tokenService.getTokenUser();
//        String inlineUuid = (String) tokenUser.getClaims().get("sub");
//        SimpleUserInfo simpleUserInfo;
//        if (!onlineUsers.containsKey(inlineUuid)) {
//            simpleUserInfo = new SimpleUserInfo(tokenUser);
//            addUser(simpleUserInfo);
//            log.info("add OnlineUser: ".concat(tokenUser.getUsername()).concat(" to OnlineHolder"));
//            String loc = (String) tokenUser.getClaims().get("localisation");
//            if (loc==null||loc.isBlank()) loc="RU";
//            tokenService.setGlobalLocalisation(inlineUuid, loc);
//        } else simpleUserInfo = onlineUsers.get(inlineUuid);
//        inline.add(DirectiveGuards.builder()
//                .tokenUser(inlineUuid)
//                .person(simpleUserInfo.getNickName())
//                .localisation(simpleUserInfo.getLocalisation())
//                .switchPosition(SwitchPosition.MAIN)
//                .operation(KafkaOperation.GET).build());
//        return simpleUserInfo;
//    }

//    public String getLocalisation() {
//        TokenUser tokenUser = tokenService.getTokenUser();
//        String inlineUuid = (String) tokenUser.getClaims().get("sub");
//        return (onlineUsers.containsKey(inlineUuid)) ? onlineUsers.get(inlineUuid).getLocalisation().toString().toLowerCase() : (String) tokenUser.getClaims().get("localisation");
//    }
}
