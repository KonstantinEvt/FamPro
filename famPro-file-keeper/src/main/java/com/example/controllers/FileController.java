package com.example.controllers;

import com.example.converter.NameConverter;
import com.example.holders.NewsHolder;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import com.example.holders.PhotoHolder;
import com.example.services.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Log4j2
@RestController
@RequestMapping("/file")
@Tag(name = "Контроллер для сохранения и получения файлов")
public class FileController {
    /**
     * Экземпляр FileStorageService, используемый для сохранения и получения файлов.
     */
    private final FileStorageService fileStorageService;
    private NameConverter nameConverter;
    private final PhotoHolder photoHolder;
    private final NewsHolder newsHolder;
    @Value("${minio.first_photo_bucket}")
    private String firstPhoto;
    @Value("${minio.photo_events_bucket}")
    private String events;
    @Value("${minio.system_news_bucket}")
    private String sysNews;
    @Value("${minio.common_news_bucket}")
    private String commonNews;

    public FileController(FileStorageService fileStorageService, PhotoHolder photoHolder, NewsHolder newsHolder, NameConverter nameConverter) {
        this.fileStorageService = fileStorageService;
        this.photoHolder = photoHolder;
        this.newsHolder = newsHolder;
        this.nameConverter=nameConverter;
    }

    @PostMapping("/savePrimePhoto")
    public ResponseEntity<String> savePrimePhoto(@RequestPart("primePhoto") MultipartFile file) {
        photoHolder.addFrontPicture(file);
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
        newsHolder.addPicture(nameConverter.covertName(name),file,bucket);
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
        log.info("Выполняется получение файла с сервера MinIO по id: " + id);

        return newsHolder.getPhoto(sysNews, nameConverter.covertName(id)) ;
    }
    @GetMapping("/common/{id}")
    public byte[] getCommonPhoto(@PathVariable("id") String id) {
        log.info("Выполняется получение файла с сервера MinIO по id: " + id);
        return newsHolder.getPhoto(commonNews, nameConverter.covertName(id));
    }
}