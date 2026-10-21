package ru.memman.holders;

import ru.memman.dtos.DirectiveGuards;
import ru.memman.entity.BaseUser;

import ru.memman.service.TokenService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Map;

@Component
@Log4j2
public class ActualUserHolder {
    private final Map<String, BaseUser> userMap;


    public ActualUserHolder(@Qualifier("actualUsersMap") Map<String, BaseUser> userMap) {
        this.userMap = userMap;
    }

    public void addActualUser(BaseUser baseUser) {
        userMap.put(baseUser.getExternUuid(), baseUser);
    }

    public BaseUser getActualUser(String userUuid) {
        return userMap.get(userUuid);
    }
    public void removeActualUser(String userUuid) {
        userMap.remove(userUuid);
    }
}
