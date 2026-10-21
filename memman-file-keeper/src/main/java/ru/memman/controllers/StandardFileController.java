package ru.memman.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import ru.memman.converter.NameConverter;
import ru.memman.enam.BucketCategory;
import ru.memman.holders.NameBucketHolder;
import ru.memman.holders.SystemPhotoHolder;
import ru.memman.services.FileStorageService;
import ru.memman.services.TokenService;

import java.util.Map;


@Log4j2
@RestController
@RequestMapping("/file/standard")
@Tag(name = "Контроллер для сохранения и получения файлов")
public class StandardFileController {

    private final FileStorageService fileStorageService;
    private final SystemPhotoHolder systemPhotoHolder;
    private final NameBucketHolder nameBucketHolder;
    private final TokenService tokenService;

    public StandardFileController(FileStorageService fileStorageService,
                                  SystemPhotoHolder systemPhotoHolder,
                                  NameBucketHolder nameBucketHolder,
                                  TokenService tokenService) {
        this.fileStorageService = fileStorageService;
        this.systemPhotoHolder = systemPhotoHolder;
        this.nameBucketHolder = nameBucketHolder;
        this.tokenService = tokenService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveSysPhoto( @RequestPart("name") String imgUrl,
                                               @RequestPart("photo") MultipartFile file,
                                               @RequestPart("bucket") String bucket) {
        log.info("получено системное новостное фото. сатегория: {}, имя: {}",bucket, imgUrl);
        if (bucket.equals("SYSTEM") || bucket.equals("COMMON"))
            systemPhotoHolder.addSystemPicture(imgUrl, file, nameBucketHolder.getNameOfBucket(BucketCategory.STANDARD, bucket.toLowerCase(),null));
        else fileStorageService.savePrivatePhoto(imgUrl, file, nameBucketHolder.getNameOfBucket(BucketCategory.PRIVATE, bucket.toLowerCase(),(String) tokenService.getTokenUser().getClaims().get("sub")));
        return ResponseEntity.ok("File is saving");
    }

    @GetMapping(value = "/get/{category}/{imgUrl}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getStandardPhoto(@PathVariable("category") String category, @PathVariable("imgUrl") String id) {
        log.info("Выполняется получение системного фото категории {} по id: {}", category, id);
        return systemPhotoHolder.getPhoto(nameBucketHolder.getNameOfBucket(BucketCategory.STANDARD, category.toLowerCase(), null), id);
    }

    @PostMapping("/get/defaultPhotoPack")
    public Map<String, byte[]> getDefaultPhotoPack() {
        log.info("Выполняется получение пакета фото по умолчанию");
        return systemPhotoHolder.getDefaultPhotos();
    }

}