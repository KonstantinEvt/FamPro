package ru.memman.service;

import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import ru.memman.dtos.OnlineUserDto;
import ru.memman.dtos.TokenUser;
import ru.memman.entity.BaseUser;
import ru.memman.holders.OnlineUserHolder;
import ru.memman.mappers.OnlineUserMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Getter
@Setter
@Log4j2
@EnableAsync
public class OnlineUserService {
    private final OnlineUserHolder onlineUserHolder;
    private final Map<String, Timestamp> actualTimeMap;
    private final ActualUserService actualUserService;
    private final OnlineUserMapper onlineUserMapper;
    private final TokenService tokenService;
    private final LinkedList<OnlineUserDto> inline;
    @Value("${application.schedule.delayToOffline}")
    private long delayToOffline;
    @Value("${application.schedule.delayToOnlineRemove}")
    private long delayToOnlineRemove;
    @Value("${application.schedule.sagaHeartbeat}")
    private long heartbeat;

    public OnlineUserService(OnlineUserHolder onlineUserHolder,
                             Map<String, Timestamp> actualTimeMap,
                             ActualUserService actualUserService,
                             OnlineUserMapper onlineUserMapper,
                             TokenService tokenService,
                             LinkedList<OnlineUserDto> inline) {
        this.onlineUserHolder = onlineUserHolder;
        this.actualTimeMap = actualTimeMap;
        this.actualUserService = actualUserService;
        this.onlineUserMapper = onlineUserMapper;
        this.tokenService = tokenService;
        this.inline = inline;
    }

    @PostConstruct
    private void setPersonsInBaseCount() {
        inline.add(new OnlineUserDto());
    }

    public OnlineUserDto getOnlineUser() {
        TokenUser tokenUser = tokenService.getTokenUser();
        OnlineUserDto onlineUserDto = onlineUserHolder.getOnlineUser((String) tokenUser.getClaims().get("sub"));
        if (onlineUserDto == null) {
            onlineUserDto = onlineUserMapper.entityToDto(actualUserService.getActualUser(tokenUser));
            onlineUserHolder.addMenToOnlineCount();
            onlineUserDto.setNewsCounts(new int[5]);
            onlineUserDto.setSendOver(true);
            log.info("User: {} enter in portal", onlineUserDto.getLogName());
            inline.add(onlineUserDto);
        }
        onlineUserDto.setLastOnline(new Timestamp(System.currentTimeMillis()));
        onlineUserDto.setOnlinePeopleCount(onlineUserHolder.getOnlinePeopleCount());
        onlineUserDto.setPeopleInBase(onlineUserHolder.getInBasePeopleCount());
        onlineUserDto.setPersonsInBase(onlineUserHolder.getInBasePersonsCount());
        onlineUserDto.setOnline(true);
        onlineUserHolder.addUser(onlineUserDto);
        return onlineUserDto;
    }

    public void setOffline() {
        TokenUser tokenUser = tokenService.getTokenUser();
        OnlineUserDto onlineUserDto = onlineUserHolder.getOnlineUser((String) tokenUser.getClaims().get("sub"));
        onlineUserDto.setLastOnline(new Timestamp(System.currentTimeMillis()));
        onlineUserDto.setOnline(false);
        onlineUserDto.setSendOver(true);
        BaseUser baseUser = onlineUserMapper.dtoToEntity(onlineUserDto);
        actualTimeMap.put(baseUser.getExternUuid(), new Timestamp(System.currentTimeMillis()));
        onlineUserHolder.removeUser(onlineUserDto.getExternUuid());
        onlineUserHolder.removeMenFromOnlineCount();
        actualUserService.addActualUser(baseUser);
        inline.add(onlineUserDto);
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

    public void reloadInline() {
        inline.addAll(onlineUserHolder.getOnlineUsers());
    }

    @Async
    @Scheduled(fixedDelayString = "${application.schedule.onlineToActual}")
    void dropOnlineToActual() {
        long now = (new Timestamp(System.currentTimeMillis())).getTime();
        List<OnlineUserDto> users = onlineUserHolder.getOnlineUsers();
        log.info("Drop online to actual");
        for (OnlineUserDto onlineUser : users) {
            long lastOnline = now - onlineUser.getLastOnline().getTime();
            log.info("Check of {} time:{}", onlineUser.getNickName(), lastOnline);
            BaseUser baseUser = onlineUserMapper.dtoToEntity(onlineUser);
            if (lastOnline > delayToOffline) {
                if (onlineUser.isOnline()) onlineUser.setOnline(false);
                if (lastOnline > delayToOnlineRemove) {
                    actualUserService.setToDrop(baseUser);
                    actualTimeMap.put(baseUser.getExternUuid(), new Timestamp(System.currentTimeMillis()));
                    onlineUserHolder.removeUser(onlineUser.getExternUuid());
                    onlineUser.setSendOver(true);
                    inline.add(onlineUser);
                }
            }
            actualUserService.addActualUser(baseUser);
        }
        onlineUserHolder.setOnlinePeopleCount(onlineUserHolder.getOnlineUsers().size());
    }
    @Async
    @Scheduled(fixedDelayString = "${application.schedule.sagaHeartbeat}")
    void heartbeat(){
        inline.add(OnlineUserDto.builder().build());
    }
}

