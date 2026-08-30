package ru.memman.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.memman.dtos.TokenUser;
import ru.memman.entity.BaseUser;
import ru.memman.enums.Localisation;
import ru.memman.enums.UserRole;
import ru.memman.holders.ActualUserHolder;
import ru.memman.holders.OnlineUserHolder;
import ru.memman.repository.OnlineUserRepository;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Getter
@Setter
@Log4j2
@EnableAsync
public class ActualUserService {
    private final ActualUserHolder actualUserHolder;
    private final OnlineUserHolder onlineUserHolder;
    private final OnlineUserRepository onlineUserRepository;
    private final Map<String, Timestamp> actualTimeMap;
    private static final Map<String, BaseUser> changeMap = new ConcurrentHashMap<>();
    @Value("${application.schedule.delayToActualRemove}")
    private long delayToActualRemove;

    public ActualUserService(ActualUserHolder actualUserHolder,
                             OnlineUserHolder onlineUserHolder,
                             OnlineUserRepository onlineUserRepository,
                             Map<String, Timestamp> actualTimeMap) {
        this.actualUserHolder = actualUserHolder;
        this.onlineUserHolder = onlineUserHolder;
        this.onlineUserRepository = onlineUserRepository;
        this.actualTimeMap = actualTimeMap;
    }

    @Transactional
    public BaseUser getActualUser(TokenUser tokenUser) {
        String externUuid=(String) tokenUser.getClaims().get("sub");
        BaseUser onlineUser = actualUserHolder.getActualUser(externUuid);
        if (onlineUser == null) {
            Optional<BaseUser> baseUser = onlineUserRepository.getOnlineUserByExternUuid(externUuid);
            if (baseUser.isEmpty()) {
                BaseUser baseUserNew = BaseUser.builder()
                        .externUuid(externUuid)
                        .fullName(getFullName(tokenUser))
                        .logName(getLogName(tokenUser))
                        .localisation(getTokenUserLocalisation(tokenUser))
                        .lastOnline(new Timestamp(System.currentTimeMillis()))
                        .nickName(getTokenUserNickName(tokenUser))
                        .email(getEmail(tokenUser))
                        .priorityRole(getPriorityRole(tokenUser))
                        .build();
                onlineUserRepository.persistNewUser(baseUserNew);
                onlineUserHolder.addMenToBaseCount();
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

    private String getLogName(TokenUser tokenUser) {
        return (String) tokenUser.getClaims().get("preferred_username");
    }
    private String getFullName(TokenUser tokenUser) {
        return (String) tokenUser.getClaims().get("name");
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

    @Async
    @Scheduled(cron = "${application.schedule.clearActual}")
    void clearActual() {
        log.info("cleaning actual map");
        Set<String> removingUsers = new HashSet<>();
        long now = (new Timestamp(System.currentTimeMillis())).getTime();
        for (Map.Entry<String, Timestamp> entry :
                actualTimeMap.entrySet()) {
            if ((entry.getValue().getTime() - now) > delayToActualRemove) {
                actualUserHolder.removeActualUser(entry.getKey());
                removingUsers.add(entry.getKey());
            }
        }
        for (String externUuid :
                removingUsers) {
            actualTimeMap.remove(externUuid);
        }
    }

    @Async
    @Scheduled(cron = "${application.schedule.onlineToBase}")
    void dropActualToBase() {
        log.info("drop actual to base");
        List<BaseUser> toDrop;
        synchronized (changeMap) {
            toDrop = new ArrayList<>(changeMap.values());
            changeMap.clear();
        }
        if (!toDrop.isEmpty()){
        List<String> uuids=toDrop.stream().map(BaseUser::getExternUuid).toList();
        List<BaseUser> usersToUpgrade=onlineUserRepository.getOnlineUsersByExternUuids(uuids);
        updateUsers(usersToUpgrade,toDrop);
        onlineUserRepository.updateAll(usersToUpgrade);
    }}

    private void updateUsers(List<BaseUser> fromBase, List<BaseUser> fromChangeMap){
        for (BaseUser user :
                fromChangeMap) {
            BaseUser fromBaseUser=fromBase.stream().filter(x->Objects.equals(x.getExternUuid(),user.getExternUuid())).findFirst().orElseThrow(()->new RuntimeException("user in base absent"));
            fromBaseUser.setEmail(user.getEmail());
            fromBaseUser.setUrlPhoto(user.isUrlPhoto());
            fromBaseUser.setNickName(user.getNickName());
            fromBaseUser.setLinkExternId(user.getLinkExternId());
            fromBaseUser.setLocalisation(user.getLocalisation());
            fromBaseUser.setPriorityRole(user.getPriorityRole());
            fromBaseUser.setLastOnline(user.getLastOnline());
        }
    }
}

