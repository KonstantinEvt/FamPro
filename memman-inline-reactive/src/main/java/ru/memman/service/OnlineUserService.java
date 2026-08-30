package ru.memman.service;

import ru.memman.dtos.OnlineUserDto;
import ru.memman.dtos.TokenUser;
import ru.memman.entity.BaseUser;
import ru.memman.holders.OnlineUserHolder;
import ru.memman.mappers.OnlineUserMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Getter
@Setter
@Log4j2
//@EnableAsync
public class OnlineUserService {
    private final OnlineUserHolder onlineUserHolder;
    private final Map<String, Timestamp> actualTimeMap;
    private final ActualUserService actualUserService;
    private final OnlineUserMapper onlineUserMapper;
    @Value("${application.schedule.delayToOffline}")
    private long delayToOffline;
    @Value("${application.schedule.delayToOnlineRemove}")
    private long delayToOnlineRemove;

    public OnlineUserService(OnlineUserHolder onlineUserHolder,
                             Map<String, Timestamp> actualTimeMap,
                             ActualUserService actualUserService,
                             OnlineUserMapper onlineUserMapper) {
        this.onlineUserHolder = onlineUserHolder;
        this.actualTimeMap = actualTimeMap;
        this.actualUserService = actualUserService;
        this.onlineUserMapper = onlineUserMapper;
    }

    public OnlineUserDto getOnlineUser(TokenUser tokenUser) {
        String externUuid = (String) tokenUser.getClaims().get("sub");
        OnlineUserDto onlineUserDto = onlineUserHolder.getOnlineUser(externUuid);
        if (onlineUserDto == null) {
            onlineUserDto = onlineUserMapper.entityToDto(actualUserService.getActualUser(tokenUser));
        }
        onlineUserDto.setLastOnline(new Timestamp(System.currentTimeMillis()));
        onlineUserDto.setOnline(true);
        System.out.println(onlineUserDto);
        onlineUserHolder.addUser(onlineUserDto);
        return onlineUserDto;
    }

    public OnlineUserDto getOnlineUser(String externUuid) {
        OnlineUserDto onlineUserDto = onlineUserHolder.getOnlineUser(externUuid);
        if (onlineUserDto == null) {
            onlineUserDto = onlineUserMapper.entityToDto(actualUserService.getActualUser(externUuid));
            if (onlineUserDto == null) return null;
        }
        actualTimeMap.put(externUuid, new Timestamp(System.currentTimeMillis()));
        return onlineUserDto;
    }

//    @Async
//    @Scheduled(fixedDelayString = "${application.schedule.onlineToActual}")
//    void dropOnlineToActual() {
//        long now = (new Timestamp(System.currentTimeMillis())).getTime();
//        List<OnlineUserDto> users = onlineUserHolder.getOnlineUsers();
//        for (OnlineUserDto onlineUser : users) {
//            long lastOnline = onlineUser.getLastOnline().getTime() - now;
//            BaseUser baseUser = onlineUserMapper.dtoToEntity(onlineUser);
//            if (lastOnline > delayToOffline) {
//                if (onlineUser.isOnline()) onlineUser.setOnline(false);
//                if (lastOnline > delayToOnlineRemove) {
//                    actualUserService.setToDrop(baseUser);
//                    actualTimeMap.put(baseUser.getExternUuid(), new Timestamp(System.currentTimeMillis()));
//                    onlineUserHolder.removeUser(onlineUser.getExternUuid());
//                }
//            }
//            actualUserService.addActualUser(baseUser);
//        }
//    }
}

