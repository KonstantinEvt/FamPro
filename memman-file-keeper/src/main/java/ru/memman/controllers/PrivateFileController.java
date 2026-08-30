package ru.memman.controllers;

import ru.memman.converter.NameConverter;
import ru.memman.enums.SwitchPosition;
import ru.memman.feign.StorageConnectionClient;
import ru.memman.holders.PhotoHolder;
import ru.memman.holders.SystemPhotoHolder;
import ru.memman.services.FileStorageService;
import ru.memman.services.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Objects;
import java.util.Set;


@Log4j2
@RestController
@RequestMapping("/file/private")
@Tag(name = "Контроллер для сохранения и получения файлов")
public class PrivateFileController {

    private final FileStorageService fileStorageService;
    private final PhotoHolder photoHolder;
    private final SystemPhotoHolder systemPhotoHolder;
    private final Map<String, Set<String>> rightsMap;
    private final TokenService tokenService;
    private final StorageConnectionClient storageConnectionClient;
    @Value("${minio.first_photo_bucket}")
    private String firstPhoto;
    @Value("${minio.photo_burial_bucket}")
    private String burial;
    @Value("${minio.photo_birth_bucket}")
    private String birth;
    @Value("${minio.default_photo_bucket}")
    private String defaultPhoto;


    public PrivateFileController(FileStorageService fileStorageService, StorageConnectionClient storageConnectionClient, PhotoHolder photoHolder, SystemPhotoHolder systemPhotoHolder, NameConverter nameConverter, Map<String, Set<String>> rightsMap, TokenService tokenService) {
        this.fileStorageService = fileStorageService;
        this.photoHolder = photoHolder;
        this.systemPhotoHolder = systemPhotoHolder;
        this.storageConnectionClient = storageConnectionClient;
        this.rightsMap = rightsMap;
        this.tokenService = tokenService;
    }

    @PostMapping("/savePrimePhoto")
    public ResponseEntity<String> savePrimePhoto(@RequestPart("PrimePhoto") MultipartFile file) {
        photoHolder.addFrontPicture(file, firstPhoto);
        return ResponseEntity.ok("Prime photo is saving");
    }
    @PostMapping("/saveBurialPhoto")
    public ResponseEntity<String> saveBurialPhoto(@RequestPart("BurialPhoto") MultipartFile file) {
        photoHolder.addFrontPicture(file, burial);
        return ResponseEntity.ok("Burial photo is saving");
    }
    @PostMapping("/saveBirthPhoto")
    public ResponseEntity<String> saveBirthPhoto(@RequestPart("BirthPhoto") MultipartFile file) {
        photoHolder.addFrontPicture(file, birth);
        return ResponseEntity.ok("Birth photo is saving");
    }

    @GetMapping("/get/{uuid}")
    public byte[] getFirstPhoto(@PathVariable("uuid") String uuid) {
        log.info("Выполняется получение файла с сервера MinIO по UUID: " + uuid);
        String user = (String) tokenService.getTokenUser().getClaims().get("sub");
        if (Objects.equals(user, uuid) ||(rightsMap.containsKey(user) && rightsMap.get(user).contains(uuid))
                || storageConnectionClient.checkRights(String.valueOf(SwitchPosition.PRIME.ordinal()).concat(uuid)))
            return fileStorageService.getPhoto(firstPhoto, uuid);
        else return systemPhotoHolder.getPhoto(defaultPhoto, "person.jpg");
    }
    @GetMapping("/get/birth/{uuid}")
    public byte[] getBirthPhoto(@PathVariable("uuid") String uuid) {
        log.info("Выполняется получение файла с сервера MinIO по UUID (birth): " + uuid);
        String user = (String) tokenService.getTokenUser().getClaims().get("sub");
        if (Objects.equals(user, uuid) || storageConnectionClient.checkRights(String.valueOf(SwitchPosition.BIRTH.ordinal()).concat(uuid)))
            return fileStorageService.getPhoto(birth, uuid);
        else return systemPhotoHolder.getPhoto(defaultPhoto, "photono.jpg");
    }
    @GetMapping("/get/burial/{uuid}")
    public byte[] getBurialPhoto(@PathVariable("uuid") String uuid) {
        log.info("Выполняется получение файла с сервера MinIO по UUID (burial): " + uuid);
        String user = (String) tokenService.getTokenUser().getClaims().get("sub");
        if (Objects.equals(user, uuid) || storageConnectionClient.checkRights(String.valueOf(SwitchPosition.BURIAL.ordinal()).concat(uuid)))
            return fileStorageService.getPhoto(burial, uuid);
        else return systemPhotoHolder.getPhoto(defaultPhoto, "photono.jpg");
    }
}