package ru.memman.holders;

import jakarta.annotation.PostConstruct;
import ru.memman.dtos.OnlineUserDto;
import ru.memman.enums.SecretLevel;
import ru.memman.repository.OnlineUserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Log4j2
public class OnlineUserHolder {
    private final Map<String, OnlineUserDto> onlineUsers;
    private final OnlineUserRepository onlineUserRepository;
    private volatile AtomicLong inBasePeopleCount;
    private volatile AtomicLong inBasePersonsCount = new AtomicLong(0);
    private volatile AtomicInteger onlinePeopleCount;

    private final LinkedList<OnlineUserDto> inline;

    public OnlineUserHolder(@Qualifier("onlineUsersMap") Map<String, OnlineUserDto> onlineUsers,
                            OnlineUserRepository onlineUserRepository, LinkedList<OnlineUserDto> inline) {
        this.onlineUsers = onlineUsers;
        this.onlineUserRepository = onlineUserRepository;
        this.inline = inline;
    }

    @PostConstruct
    private void setInBasePeopleCount() {
        this.inBasePeopleCount = new AtomicLong(onlineUserRepository.getUsersCount());
    }

    public void addUser(OnlineUserDto onlineUser) {
        onlineUsers.put(onlineUser.getExternUuid(), onlineUser);
    }



    public void addGlobalMessageCount(String category) {
        if (Objects.equals(category, "system")) {
            onlineUsers.values().forEach(x -> {
                x.getNewsCounts()[0]++;
                x.getNewsCounts()[1]++;
            });
        } else onlineUsers.values().forEach(x -> {
            x.getNewsCounts()[0]++;
            x.getNewsCounts()[2]++;
        });
    }

    public OnlineUserDto getOnlineUser(String userUuid) {
        return onlineUsers.get(userUuid);
    }

    public ArrayList<OnlineUserDto> getOnlineUsers() {
        return new ArrayList<>(onlineUsers.values());
    }

    public void removeUser(String externUuid) {
        onlineUsers.remove(externUuid);
    }

    public long getInBasePeopleCount() {
        return this.inBasePeopleCount.get();
    }

    public int getOnlinePeopleCount() {
        return this.onlinePeopleCount.get();
    }

    public void setOnlinePeopleCount(int count) {
        this.onlinePeopleCount = new AtomicInteger(count);
    }

    public void addMenToBaseCount() {
        this.inBasePeopleCount.incrementAndGet();
    }

    public void addMenToOnlineCount() {
        this.onlinePeopleCount.incrementAndGet();
    }

    public void removeMenFromOnlineCount() {
        this.onlinePeopleCount.decrementAndGet();
    }

    public long getInBasePersonsCount() {
        return this.inBasePersonsCount.get();
    }

    public void setInBasePersonsCount(Long count) {
        this.inBasePersonsCount = new AtomicLong(count);
    }

}
