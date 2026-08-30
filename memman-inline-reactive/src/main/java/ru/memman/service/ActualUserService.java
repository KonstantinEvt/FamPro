package ru.memman.service;

import ru.memman.dtos.*;
import ru.memman.entity.BaseUser;


import ru.memman.enums.Localisation;
import ru.memman.enums.UserRole;
import ru.memman.holders.ActualUserHolder;
import ru.memman.holders.OnlineUserHolder;
import ru.memman.mappers.OnlineUserMapper;
import ru.memman.repository.OnlineUserRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
//import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

@Service
@Getter
@Setter
@Log4j2
//@EnableAsync
public class ActualUserService {
    private final ActualUserHolder actualUserHolder;
    private final OnlineUserRepository onlineUserRepository;
    private final Map<String, Timestamp> actualTimeMap;
    private static final Map<String, BaseUser> changeMap = new ConcurrentHashMap<>();
    @Value("${application.schedule.delayToActualRemove}")
    private long delayToActualRemove;

    public ActualUserService(ActualUserHolder actualUserHolder,
                             OnlineUserRepository onlineUserRepository,
                             Map<String, Timestamp> actualTimeMap) {
        this.actualUserHolder = actualUserHolder;
        this.onlineUserRepository = onlineUserRepository;
        this.actualTimeMap = actualTimeMap;
    }

    @Transactional
    public BaseUser getActualUser(TokenUser tokenUser) {
//        String userUuid = tokenService.getTokenUserUuid(tokenService.getTokenUser());
        String externUuid=(String) tokenUser.getClaims().get("sub");
        BaseUser onlineUser = actualUserHolder.getActualUser(externUuid);
        if (onlineUser == null) {
            Optional<BaseUser> baseUser = onlineUserRepository.getOnlineUserByExternUuid(externUuid);
            if (baseUser.isEmpty()) {
                BaseUser baseUserNew = BaseUser.builder()
                        .externUuid(externUuid)
                        .localisation(getTokenUserLocalisation(tokenUser))
                        .lastOnline(new Timestamp(System.currentTimeMillis()))
                        .nickName(getTokenUserNickName(tokenUser))
                        .email(getEmail(tokenUser))
                        .priorityRole(getPriorityRole(tokenUser))
                        .build();
                onlineUserRepository.persistNewUser(baseUserNew);
                return baseUserNew;
            } else {
                baseUser.get().setLastOnline(new Timestamp(System.currentTimeMillis()));
                return baseUser.get();
            }
        }
        return onlineUser;
    }
    @SuppressWarnings("unchecked")
    private UserRole getPriorityRole(TokenUser tokenUser) {
        final var realmAccess=(Map<String, Object>) tokenUser.getClaims().getOrDefault("realm_access", Map.of());
        final var realmRoles = (Collection<String>) realmAccess.getOrDefault("roles", List.of());
        for (UserRole role :
                UserRole.values()) {
            if (realmRoles.contains(role.getNameSSO())) return role;
        }
        return UserRole.SIMPLE_USER;
    }

    private Localisation getTokenUserLocalisation(TokenUser tokenUser) {
        String locale=(String) tokenUser.getClaims().get("locale");
        for (Localisation loc :
                Localisation.values()) {
            if (Objects.equals(locale, loc.name().toLowerCase())) return loc;
        }
        return Localisation.RU;
    }
    private String getEmail(TokenUser tokenUser) {
          return (String) tokenUser.getClaims().get("email");
    }
    private String getTokenUserNickName(TokenUser tokenUser) {
        return (String) tokenUser.getClaims().get("nickname");
    }
    @Transactional
    public BaseUser getActualUser(String externUuid) {
        BaseUser actualUser = actualUserHolder.getActualUser(externUuid);
        if (actualUser == null) {
            actualUser = onlineUserRepository.getOnlineUserByExternUuid(externUuid).orElse(null);
            actualUserHolder.addActualUser(actualUser);
        }
        return actualUser;
    }

    @Transactional
    public void addActualUser(BaseUser baseUser) {
        actualUserHolder.addActualUser(baseUser);
    }

    public void setToDrop(BaseUser baseUser) {
        changeMap.put(baseUser.getExternUuid(), baseUser);
    }

//    @Async
//    @Scheduled(cron = "${application.schedule.clearActual}")
//    void clearActual() {
//        Set<String> removingUsers = new HashSet<>();
//        long now = (new Timestamp(System.currentTimeMillis())).getTime();
//        for (Map.Entry<String, Timestamp> entry :
//                actualTimeMap.entrySet()) {
//            if ((entry.getValue().getTime() - now) > delayToActualRemove) {
//                actualUserHolder.removeActualUser(entry.getKey());
//                removingUsers.add(entry.getKey());
//            }
//        }
//        for (String externUuid :
//                removingUsers) {
//            actualTimeMap.remove(externUuid);
//        }
//    }

//    @Async
//    @Scheduled(cron = "${application.schedule.onlineToBase}")
//    void dropActualToBase() {
//        List<BaseUser> toDrop;
//        synchronized (changeMap) {
//            toDrop = new ArrayList<>(changeMap.values());
//            changeMap.clear();
//        }
//        onlineUserRepository.updateAll(toDrop);
//    }
}

