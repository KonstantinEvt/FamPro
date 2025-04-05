package com.example.holders;

import com.example.services.FileStorageServiceImpl;
import com.example.services.TokenService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
@Setter
@Slf4j
public class PhotoHolder {
    private Map<String, byte[]> frontPictures = new ConcurrentHashMap<>();
    private Map<String, byte[]> birthPictures = new ConcurrentHashMap<>();
    private Map<String, byte[]> burialPictures = new ConcurrentHashMap<>();
    private TokenService tokenService;
    private DirectiveHolder directiveHolder;
    private FileStorageServiceImpl fileStorageService;


    public PhotoHolder(TokenService tokenService, DirectiveHolder directiveHolder, FileStorageServiceImpl fileStorageService) {
        this.tokenService = tokenService;
        this.directiveHolder = directiveHolder;
        this.fileStorageService = fileStorageService;
    }

    public void addFrontPicture(MultipartFile frontPicture, String bucket) {
        String frontUser = (String) tokenService.getTokenUser().getClaims().get("sub");
        try (InputStream file = frontPicture.getInputStream()) {
             frontPictures.put(frontUser, file.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (directiveHolder.getDirectiveMap().containsKey(frontUser)) {
            try {
                fileStorageService.savePhoto(getFrontPictures().get(frontUser),
                        directiveHolder.getDirectiveMap().get(frontUser).getPerson(),bucket);
                log.info("Файл успешно сохранен на сервер MinIO.");
            } catch (Exception e) {
                log.error("Ошибка при сохранении файла на сервер MinIO.", e);
            }
            directiveHolder.getDirectiveMap().remove(frontUser);
            frontPictures.remove(frontUser);
            System.out.println("tyt_tyt");
        }

    }
}

