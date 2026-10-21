package ru.memman.controllers;

import ru.memman.enam.BucketCategory;
import ru.memman.holders.NameBucketHolder;
import ru.memman.services.BarrierGetService;
import ru.memman.services.FileStorageService;
import ru.memman.services.TokenService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@RestController
@RequestMapping("/file/contact")
public class ContactPhotoController {
    private final FileStorageService fileStorageService;
    private final TokenService tokenService;
    private final NameBucketHolder nameBucketHolder;
    private final BarrierGetService barrierGetService;

    public ContactPhotoController(FileStorageService fileStorageService,
                                  TokenService tokenService,
                                  NameBucketHolder nameBucketHolder,
                                  BarrierGetService barrierGetService) {
        this.fileStorageService = fileStorageService;
        this.tokenService = tokenService;
        this.nameBucketHolder = nameBucketHolder;
        this.barrierGetService = barrierGetService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> addContactPhoto(@RequestPart("photo") MultipartFile photo,
                                                  @RequestPart("name") String personUuid) {
        log.info("Выполняется сохранение фото контакта персоны: {}", personUuid);
        fileStorageService.savePrivatePhoto(personUuid, photo, nameBucketHolder.getNameOfBucket(BucketCategory.CONTACT, "contact", (String) tokenService.getTokenUser().getClaims().get("sub")));
        return ResponseEntity.ok("File is saving");
    }

    @GetMapping("/get/{uuid}")
    public byte[] getContactPhoto(@PathVariable("uuid") String personId) {
        log.info("Выполняется получение фото контакта персоны: {}", personId);
        return fileStorageService.getPhoto(nameBucketHolder.getNameOfBucket(BucketCategory.CONTACT, "contact", (String) tokenService.getTokenUser().getClaims().get("sub")), personId);
    }
    @GetMapping("/getPrivate/{uuid}/{linkUuid}")
    public byte[] getPrivatePhoto( @PathVariable("uuid") String uuid, @PathVariable("linkUuid") String linkUuid) {
        log.info("Выполняется получение little по UUID: {} для персоны {}", uuid, linkUuid);
        return (barrierGetService.getAccessPrivatePhoto("little", uuid)) ?
                fileStorageService.getPhoto(nameBucketHolder.getNameOfBucket(BucketCategory.PRIVATE, "little", linkUuid), linkUuid) : null;
    }
}
