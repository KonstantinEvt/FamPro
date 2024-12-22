package org.example.holders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.services.FileStorageService;
import org.example.services.TokenService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.WeakHashMap;

@Component
@Getter
@Setter
@Slf4j
public class PhotoHolder {
    private Map<String, byte[]> frontPictures = new WeakHashMap<>();
    private Map<String, byte[]> birthPictures = new WeakHashMap<>();
    private Map<String, byte[]> burialPictures = new WeakHashMap<>();
    private TokenService tokenService;
    private DirectiveHolder directiveHolder;
    private FileStorageService fileStorageService;

    public PhotoHolder(TokenService tokenService, DirectiveHolder directiveHolder, FileStorageService fileStorageService) {
        this.tokenService = tokenService;
        this.directiveHolder = directiveHolder;
        this.fileStorageService = fileStorageService;
    }

    public void addFrontPicture(MultipartFile frontPicture) {
        String frontUser = (String) tokenService.getTokenUser().getClaims().get("sub");
        try (InputStream file = frontPicture.getInputStream()) {
             frontPictures.put(frontUser, file.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (directiveHolder.getDirectiveMap().containsKey(frontUser)) {
            try {
                fileStorageService.saveFirstPhoto(getFrontPictures().get(frontUser),
                        directiveHolder.getDirectiveMap().get(frontUser).getPerson());
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

