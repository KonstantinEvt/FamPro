package com.example.controllers;

import com.example.holders.PhotoHolder;
import com.example.services.ContactService;
import com.example.services.FileStorageServiceImpl;
import com.example.services.TokenService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Set;

@Log4j2
@RestController
@RequestMapping("/photoContact")
public class ContactPhotoController {
    private final FileStorageServiceImpl fileStorageService;
    private final TokenService tokenService;
    private final PhotoHolder photoHolder;
    private final ContactService contactService;
    private final Map<String, Set<String>> rightsMap;

    @Value("${minio.first_photo_bucket}")
    private String firstPhoto;

    public ContactPhotoController(FileStorageServiceImpl fileStorageService, TokenService tokenService, PhotoHolder photoHolder, ContactService contactService, Map<String, Set<String>> rightsMap) {
        this.fileStorageService = fileStorageService;
        this.tokenService = tokenService;
        this.photoHolder = photoHolder;
        this.contactService = contactService;
        this.rightsMap = rightsMap;
    }

    @PostMapping("/saveContactPhoto")
    public ResponseEntity<String> addContactPhoto(@RequestPart("contactPhoto") MultipartFile photo, @RequestPart("externId") String name) {
        photoHolder.addFrontPicture(photo, name);
        return ResponseEntity.ok("File is saving");
    }

    @GetMapping("/get/{uuid}")
    public byte[] getContactPhoto(@PathVariable("uuid") String uuid) {
        log.info("Выполняется получение файла с сервера MinIO по UUID: " + uuid);
        return fileStorageService.getPhoto((String) tokenService.getTokenUser().getClaims().get("sub"), uuid);
    }
    @GetMapping("/getPrime/{uuid}")
    public byte[] getContactPrimePhoto(@PathVariable("uuid") String uuid) {
        log.info("Выполняется получение файла с сервера MinIO по UUID: " + uuid);
        return contactService.getContactPrimePhoto(uuid);
    }
}
