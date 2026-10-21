package ru.memman.holders;

import ru.memman.enums.UserRole;
import ru.memman.services.FileStorageService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.memman.services.TokenService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Component
@Getter
@Setter
@Slf4j
public class SystemPhotoHolder {
    private final Map<String, byte[]> systemPictures;
    private final Map<String, byte[]> commonPictures;
    private final Map<String, byte[]> defaultPhotos;
    private final FileStorageService fileStorageService;
    private final TokenService tokenService;

    @Value("${minio.buckets.system_news}")
    private String sysNewsBucket;
    @Value("${minio.buckets.common_news}")
    private String commonNewsBucket;
    @Value("${minio.buckets.default_photo}")
    private String defaultPhotoBucket;

    public SystemPhotoHolder(Map<String, byte[]> systemPictures,
                             Map<String, byte[]> commonPictures,
                             Map<String, byte[]> defaultPhotos,
                             FileStorageService fileStorageService,
                             TokenService tokenService) {
        this.systemPictures = systemPictures;
        this.commonPictures = commonPictures;
        this.defaultPhotos = defaultPhotos;
        this.fileStorageService = fileStorageService;
        this.tokenService = tokenService;
    }

    public void addSystemPicture(String name, MultipartFile frontPicture, String bucket) {
        log.info("Добавляем фото в холдер/хранилище. Категория: {}, имя: {}", bucket,name);
        if (tokenService.getPriorityRole(tokenService.getTokenUser()).ordinal()>UserRole.MANAGER.ordinal()) return;
        try (InputStream file = frontPicture.getInputStream()) {
            byte[] bytesOfPicture = file.readAllBytes();
            if (bucket.equals(sysNewsBucket)) systemPictures.put(name, bytesOfPicture);
            else if (bucket.equals(commonNewsBucket)) commonPictures.put(name, bytesOfPicture);
            fileStorageService.savePhoto(bytesOfPicture,
                    name, bucket);
            log.info("Файл успешно сохранен на сервер MinIO.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("Ошибка при сохранении файла на сервер MinIO.", e);
        }
    }

    public byte[] getPhoto(String bucket, String fileName) {
        if (bucket.equals(sysNewsBucket) && systemPictures.containsKey(fileName))
            return systemPictures.get(fileName);
        else if (bucket.equals(commonNewsBucket) && commonPictures.containsKey(fileName))
            return commonPictures.get(fileName);
        else if (bucket.equals(defaultPhotoBucket) && defaultPhotos.containsKey(fileName))
            return defaultPhotos.get(fileName);
        log.warn("Search photo in unknown bucket or Holder haven`t this photo");
        return fileStorageService.getPhoto(bucket, fileName);
    }

}

