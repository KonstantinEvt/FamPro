package com.example.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import com.example.holders.PhotoHolder;
import com.example.services.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/file")
@Tag(name = "Контроллер для сохранения и получения файлов")
public class FileController {
    /**
     * Экземпляр FileStorageService, используемый для сохранения и получения файлов.
     */
    private final FileStorageService fileStorageService;
    private final PhotoHolder photoHolder;

    @PostMapping("/savePrimePhoto")
    public ResponseEntity<String> savePrimePhoto(@RequestPart("primePhoto") MultipartFile file) {
        photoHolder.addFrontPicture(file);
        return ResponseEntity.ok("File is saving");
    }
    @GetMapping("/get/{uuid}")
    public ResponseEntity<Resource> getFirstPhoto(@PathVariable("uuid") String uuid) {
        log.info("Выполняется получение файла с сервера MinIO по UUID: " + uuid);
        ResponseEntity<Resource> response = fileStorageService.getFirstPhoto(uuid);
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Файл с сервера MinIO успешно получен по UUID: " + uuid);
        } else {
            log.warn("Файл на сервере MinIO не найден по UUID: " + uuid);
        }
        return response;
    }
}