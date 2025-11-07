package com.example.holders;

import com.example.services.FileStorageServiceImpl;
import com.example.services.TokenService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    private Map<String, byte[]> primePictures = new ConcurrentHashMap<>();
    private Map<String, byte[]> birthPictures = new ConcurrentHashMap<>();
    private Map<String, byte[]> burialPictures = new ConcurrentHashMap<>();
    private TokenService tokenService;
    private DirectiveHolder directiveHolder;
    private FileStorageServiceImpl fileStorageService;
    @Value("${minio.first_photo_bucket}")
    private String firstPhoto;
    @Value("${minio.photo_burial_bucket}")
    private String burial;
    @Value("${minio.photo_birth_bucket}")
    private String birth;


    public PhotoHolder(TokenService tokenService, DirectiveHolder directiveHolder, FileStorageServiceImpl fileStorageService) {
        this.tokenService = tokenService;
        this.directiveHolder = directiveHolder;
        this.fileStorageService = fileStorageService;
    }

    public void addFrontPicture(MultipartFile frontPicture, String bucket) {
        String frontUser = (String) tokenService.getTokenUser().getClaims().get("sub");
        String directiveKey = frontUser.concat(bucket);
        Map<String, byte[]> placeHolder = switch (bucket) {
            case "prime":
                yield primePictures;
            case "burial":
                yield burialPictures;
            case "birth":
                yield birthPictures;
            default:
                yield frontPictures;
        };
        try (InputStream file = frontPicture.getInputStream()) {
            placeHolder.put(frontUser, file.readAllBytes());
        } catch (IOException e) {
            log.error("cannot get file from front");
            throw new RuntimeException(e);
        }
        if (directiveHolder.getDirectiveMap().containsKey(directiveKey)) {
            fileStorageService.savePhoto(placeHolder.get(frontUser),
                    directiveHolder.getDirectiveMap().get(directiveKey).getPerson(), bucket);
            placeHolder.remove(frontUser);
            log.info("Файл успешно сохранен на сервер MinIO.");
            directiveHolder.getDirectiveMap().remove(directiveKey);
            System.out.println("tyt_tyt");
        }

    }
}

