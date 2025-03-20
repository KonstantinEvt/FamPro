package com.example.controllers;

import com.example.converter.NameConverter;
import com.example.holders.SystemPhotoHolder;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import com.example.holders.PhotoHolder;
import com.example.services.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Log4j2
@RestController
@RequestMapping("/file")
@Tag(name = "Контроллер для сохранения и получения файлов")
public class FileController {

    private final FileStorageService fileStorageService;
    private NameConverter nameConverter;
    private final PhotoHolder photoHolder;
    private final SystemPhotoHolder systemPhotoHolder;
    @Value("${minio.first_photo_bucket}")
    private String firstPhoto;
    @Value("${minio.photo_events_bucket}")
    private String events;
    @Value("${minio.system_news_bucket}")
    private String sysNews;
    @Value("${minio.common_news_bucket}")
    private String commonNews;
    @Value("${minio.default_photo_bucket}")
    private String defaultPhoto;

    public FileController(FileStorageService fileStorageService, PhotoHolder photoHolder, SystemPhotoHolder systemPhotoHolder, NameConverter nameConverter) {
        this.fileStorageService = fileStorageService;
        this.photoHolder = photoHolder;
        this.systemPhotoHolder = systemPhotoHolder;
        this.nameConverter=nameConverter;
    }

    @PostMapping("/savePrimePhoto")
    public ResponseEntity<String> savePrimePhoto(@RequestPart("primePhoto") MultipartFile file) {
        photoHolder.addFrontPicture(file, firstPhoto);
        return ResponseEntity.ok("File is saving");
    }
    @GetMapping("/get/{uuid}")
    public byte[] getFirstPhoto(@PathVariable("uuid") String uuid) {
        log.info("Выполняется получение файла с сервера MinIO по UUID: " + uuid);
        return fileStorageService.getPhoto(firstPhoto, uuid);
    }
    @PostMapping("/saveNewsPhoto")
    public ResponseEntity<String> saveSysPhoto(@RequestPart("newsPhoto") MultipartFile file,
                                               @RequestPart("name") String name,
                                                @RequestPart("bucket") String bucket) {
        systemPhotoHolder.addPicture(nameConverter.covertName(name),file,bucket);
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
    @GetMapping(value="/system/{id}",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getSystemPhoto(@PathVariable("id") String id) {
        log.info("Выполняется получение системного фото по id: " + id);

        return systemPhotoHolder.getPhoto(sysNews, nameConverter.covertName(id)) ;
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