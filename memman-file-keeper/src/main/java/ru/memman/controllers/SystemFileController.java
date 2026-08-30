package ru.memman.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.memman.converter.NameConverter;
import ru.memman.holders.SystemPhotoHolder;
import ru.memman.services.FileStorageService;

import java.util.Map;


@Log4j2
@RestController
@RequestMapping("/file/system")
@Tag(name = "Контроллер для сохранения и получения файлов")
public class SystemFileController {

    private final FileStorageService fileStorageService;
    private final NameConverter nameConverter;
    private final SystemPhotoHolder systemPhotoHolder;

    @Value("${minio.system_news_bucket}")
    private String sysNews;
    @Value("${minio.common_news_bucket}")
    private String commonNews;
    @Value("${minio.default_photo_bucket}")
    private String defaultPhoto;


    public SystemFileController(FileStorageService fileStorageService,
                                SystemPhotoHolder systemPhotoHolder,
                                NameConverter nameConverter) {
        this.fileStorageService = fileStorageService;
        this.systemPhotoHolder = systemPhotoHolder;
        this.nameConverter = nameConverter;
    }

    @PostMapping("/saveNewsPhoto")
    public ResponseEntity<String> saveSysPhoto(@RequestPart("newsPhoto") MultipartFile file,
                                               @RequestPart("name") String name,
                                               @RequestPart("bucket") String bucket) {
        if (bucket.equals("SYSTEM") || bucket.equals("COMMON"))
            systemPhotoHolder.addPicture(nameConverter.covertName(name), file, bucket);
        else fileStorageService.saveNewsPhoto(nameConverter.covertName(name), file);
        return ResponseEntity.ok("File is saving");
    }

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
    @PostMapping("/defaultPhotoPack")
    public Map<String,byte[]> getDefaultPhotoPack(){
        log.info("Выполняется получение пакета фото по умолчанию");
    return systemPhotoHolder.getDefaultPhotos();
    }

}