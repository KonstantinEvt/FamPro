package com.example.services;

import com.example.feign.NotificationConnectionClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@Log4j2
public class ContactService {
    private final Map<String, Set<String>> rightsMap;
    private final FileStorageServiceImpl fileStorageService;
    private final TokenService tokenService;
    private final NotificationConnectionClient notificationConnectionClient;

    @Value("${minio.first_photo_bucket}")
    private String firstPhoto;

    public ContactService(Map<String, Set<String>> rightsMap, FileStorageServiceImpl fileStorageService, TokenService tokenService, NotificationConnectionClient notificationConnectionClient) {
        this.rightsMap = rightsMap;
        this.fileStorageService = fileStorageService;
        this.tokenService = tokenService;
        this.notificationConnectionClient = notificationConnectionClient;
    }

    public byte[] getContactPrimePhoto(String uuid) {
        String user = (String) tokenService.getTokenUser().getClaims().get("sub");
        if ((rightsMap.containsKey(user) && rightsMap.get(user).contains(uuid))|| notificationConnectionClient.checkRights(uuid))
            return fileStorageService.getPhoto(firstPhoto, uuid);

        else throw new RuntimeException("rights are absent");
    }
}
