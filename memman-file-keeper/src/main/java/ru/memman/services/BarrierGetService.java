package ru.memman.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.memman.enums.SwitchPosition;
import ru.memman.feign.ConnectionClient;
import ru.memman.feign.ContactConnectionClient;
import ru.memman.feign.NotificationConnectionClient;
import ru.memman.feign.StorageConnectionClient;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@Log4j2
public class BarrierGetService {
    private final Map<String, Set<String>> rightsMap;
    private final NotificationConnectionClient notificationConnectionClient;
    private final StorageConnectionClient storageConnectionClient;
    private final ContactConnectionClient contactConnectionClient;
    private final TokenService tokenService;

    public BarrierGetService(Map<String, Set<String>> rightsMap,
                             NotificationConnectionClient notificationConnectionClient,
                             StorageConnectionClient storageConnectionClient, ContactConnectionClient contactConnectionClient, TokenService tokenService) {
        this.rightsMap = rightsMap;
        this.notificationConnectionClient = notificationConnectionClient;
        this.storageConnectionClient = storageConnectionClient;
        this.contactConnectionClient = contactConnectionClient;
        this.tokenService = tokenService;
    }

    public boolean getAccessPrivatePhoto(String place, String uuid) {
        String user = (String) tokenService.getTokenUser().getClaims().get("sub");
        ConnectionClient connectionClient;
        int rightsSemaphore = switch (place) {
            case "prime" -> {
                connectionClient = storageConnectionClient;
                yield SwitchPosition.PRIME.ordinal();
            }
            case "little" -> {
                connectionClient = contactConnectionClient;
                yield SwitchPosition.LITTLE.ordinal();
            }
            case "burial" -> {
                connectionClient = storageConnectionClient;
                yield SwitchPosition.BURIAL.ordinal();
            }
            case "birth" -> {
                connectionClient = storageConnectionClient;
                yield SwitchPosition.BIRTH.ordinal();
            }
            default -> {
                connectionClient = notificationConnectionClient;
                yield SwitchPosition.MAIN.ordinal();
            }
        };
        if (rightsMap.containsKey(user) && rightsMap.get(user).contains(String.valueOf(rightsSemaphore).concat(uuid)))
        {log.info("rightKey:{}",String.valueOf(rightsSemaphore).concat(uuid));return true;}
        if (connectionClient.checkRights(String.valueOf(rightsSemaphore).concat(uuid))) {
            log.info("return check from connection: true");
            rightsMap.putIfAbsent(uuid, new HashSet<>());
            rightsMap.get(uuid).add(String.valueOf(rightsSemaphore).concat(uuid));
            return true;
        } else log.info("return check from connection: false");
        return false;
    }
}
