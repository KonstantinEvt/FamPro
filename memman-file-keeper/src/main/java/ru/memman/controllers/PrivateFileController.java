package ru.memman.controllers;

import org.springframework.http.MediaType;
//import ru.memman.converter.NameConverter;
import ru.memman.enam.BucketCategory;
import ru.memman.enums.SwitchPosition;
import ru.memman.feign.StorageConnectionClient;
import ru.memman.holders.BarrierInputPhotoHolder;
import ru.memman.holders.NameBucketHolder;
import ru.memman.holders.SystemPhotoHolder;
import ru.memman.services.BarrierGetService;
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
@Tag(name = "Контроллер приватных файлов")
public class PrivateFileController {

    private final FileStorageService fileStorageService;
    private final BarrierInputPhotoHolder barrierInputPhotoHolder;
    private final BarrierGetService barrierGetService;
    private final NameBucketHolder nameBucketHolder;
    private final TokenService tokenService;


    public PrivateFileController(FileStorageService fileStorageService,
                                 NameBucketHolder nameBucketHolder,
                                 BarrierInputPhotoHolder barrierInputPhotoHolder,
                                 BarrierGetService barrierGetService,
                                 TokenService tokenService) {
        this.fileStorageService = fileStorageService;
        this.nameBucketHolder = nameBucketHolder;
        this.barrierInputPhotoHolder = barrierInputPhotoHolder;
        this.barrierGetService = barrierGetService;
        this.tokenService = tokenService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> savePrimePhoto(@RequestPart("place") String place,
                                                 @RequestPart("photo") MultipartFile file,
                                                 @RequestPart("name") String personUuid) {
        log.info("Получен запрос на сохранение в {}", place);
        barrierInputPhotoHolder.addFrontPicture(file, place,personUuid);
        return ResponseEntity.ok("photo perhaps will be saving");
    }


    @GetMapping("/get/{place}/{uuid}")
    public byte[] getPrivatePhoto(@PathVariable("place") String place, @PathVariable("uuid") String uuid) {
        log.info("Выполняется получение {} файла по UUID: {}", place, uuid);
        return (barrierGetService.getAccessPrivatePhoto(place, uuid)) ?
                fileStorageService.getPhoto(nameBucketHolder.getNameOfBucket(BucketCategory.PRIVATE, place, uuid), uuid) : null;
    }

    @GetMapping("/getNews/{uuid}/{person}")
    public byte[] getPrivateNewsPhoto(@PathVariable("uuid") String uuid, @PathVariable("person") String person) {
        log.info("Выполняется получение новостного файла по UUID: {} от пользователя: {}", uuid, person);
        return (barrierGetService.getAccessPrivatePhoto("news", uuid)) ?
                fileStorageService.getPhoto(nameBucketHolder.getNameOfBucket(BucketCategory.PRIVATE, "news", person), uuid) : null;
    }

    @GetMapping(value = "/get/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getRandomPrivatePhoto(@PathVariable("name") String name) {
        String user = (String) tokenService.getTokenUser().getClaims().get("sub");
        log.info("Выполняется получение пользователем {} своего приватного фото : {}", user, name);
        return fileStorageService.getPhoto(user, name);
    }
}