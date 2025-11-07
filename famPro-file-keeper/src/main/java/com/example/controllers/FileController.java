package com.example.controllers;

import com.example.converter.NameConverter;
import com.example.enums.SwitchPosition;
import com.example.feign.StorageConnectionClient;
import com.example.holders.PhotoHolder;
import com.example.holders.SystemPhotoHolder;
import com.example.services.FileStorageServiceImpl;
import com.example.services.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Objects;
import java.util.Set;


@Log4j2
@RestController
@RequestMapping("/file")
@Tag(name = "Контроллер для сохранения и получения файлов")
public class FileController {

    private final FileStorageServiceImpl fileStorageService;
    private final NameConverter nameConverter;
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
    @Value("${minio.system_news_bucket}")
    private String sysNews;
    @Value("${minio.common_news_bucket}")
    private String commonNews;
    @Value("${minio.default_photo_bucket}")
    private String defaultPhoto;


    public FileController(FileStorageServiceImpl fileStorageService, StorageConnectionClient storageConnectionClient, PhotoHolder photoHolder, SystemPhotoHolder systemPhotoHolder, NameConverter nameConverter, Map<String, Set<String>> rightsMap, TokenService tokenService) {
        this.fileStorageService = fileStorageService;
        this.photoHolder = photoHolder;
        this.systemPhotoHolder = systemPhotoHolder;
        this.nameConverter = nameConverter;
        this.storageConnectionClient = storageConnectionClient;
        this.rightsMap = rightsMap;
        this.tokenService = tokenService;
    }

    @PostMapping("/savePrimePhoto")
    public ResponseEntity<String> savePrimePhoto(@RequestPart("primePhoto") MultipartFile file) {
        photoHolder.addFrontPicture(file, firstPhoto);
        return ResponseEntity.ok("Prime photo is saving");
    }
    @PostMapping("/saveBurialPhoto")
    public ResponseEntity<String> saveBurialPhoto(@RequestPart("burialPhoto") MultipartFile file) {
        photoHolder.addFrontPicture(file, burial);
        return ResponseEntity.ok("Burial photo is saving");
    }
    @PostMapping("/saveBirthPhoto")
    public ResponseEntity<String> saveBirthPhoto(@RequestPart("birthPhoto") MultipartFile file) {
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
    @PostMapping("/saveNewsPhoto")
    public ResponseEntity<String> saveSysPhoto(@RequestPart("newsPhoto") MultipartFile file,
                                               @RequestPart("name") String name,
                                               @RequestPart("bucket") String bucket) {
        if (bucket.equals("SYSTEM") || bucket.equals("COMMON"))
            systemPhotoHolder.addPicture(nameConverter.covertName(name), file, bucket);
        else fileStorageService.saveNewsPhoto(nameConverter.covertName(name), file);
//        newsHolder.addPicture(String.valueOf(newsHolder.getSystemPictures().keySet().size()),file,sysNews);
        return ResponseEntity.ok("File is saving");
    }

    //    @PostMapping("/saveCOMMONPhoto")
//    public ResponseEntity<String> saveCommonPhoto(@RequestPart("newsPhoto") MultipartFile file) {
//        newsHolder.addPicture(String.valueOf(newsHolder.getCommonPictures().keySet().size()),file,sysNews);
//        return ResponseEntity.ok("File is saving");
//    }
//    @GetMapping("/sys/{id}")
//    public ResponseEntity<Resource> getSystem2Photo(@PathVariable("id") String id) {
//        log.info("Выполняется получение файла с сервера MinIO по id: " + id);
//        ResponseEntity<Resource> response = newsHolder.getPhoto(sysNews, id);
//        if (response.getStatusCode().is2xxSuccessful()) {
//            log.info("Файл с сервера MinIO успешно получен по UUID: " + id);
//        } else {
//            log.warn("Файл на сервере MinIO не найден по UUID: " + id);
//        }
//        return response;
//    }
    @GetMapping(value = "/system/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getSystemPhoto(@PathVariable("id") String id) {
        log.info("Выполняется получение системного фото по id: " + id);

        return systemPhotoHolder.getPhoto(sysNews, nameConverter.covertName(id));
    }

    @GetMapping("/common/{id}")
    public byte[] getCommonPhoto(@PathVariable("id") String id) {
        log.info("Выполняется получение общего фото по id: " + id);
        return systemPhotoHolder.getPhoto(commonNews, nameConverter.covertName(id));
    }

    @GetMapping("/defaultPhoto/{id}")
    public byte[] getDefaultPhoto(@PathVariable("id") String id) {
        log.info("Выполняется получение фото по умолчанию по id: " + id);
        return systemPhotoHolder.getPhoto(defaultPhoto, id);
    }
}