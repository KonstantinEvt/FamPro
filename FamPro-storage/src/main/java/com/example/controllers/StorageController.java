package com.example.controllers;

import com.example.service.ServiceOfStorageBD;
import com.example.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@AllArgsConstructor
@Log4j2
@Tag(name="Операции со всеми данными")
@RequestMapping("/storage/")
public class StorageController {
    private final ServiceOfStorageBD serviceOfStorageBD;
    private final Map<String, String> tempPhotoAccept;
    private final TokenService tokenService;

    @GetMapping("/database/save{filename}")
    @Operation(method="Сохранить все данные в файл", description = "Данные в файл", summary = "Сохранить базу в файл")
    public ResponseEntity<String> saveDataToFile(@PathVariable String filename){
        serviceOfStorageBD.saveDataToFile(filename);
        return ResponseEntity.status(222).body("File is saved");
    }
    @GetMapping("/database/recover{filename}")
    public  ResponseEntity<String> recoverBaseFromFile(@PathVariable String filename){
        serviceOfStorageBD.recoverBaseFromFile(filename);
        return ResponseEntity.status(223).body("base is good");
    }
    @GetMapping("/acceptPhoto/{uuid}")
    public boolean getAccept(@PathVariable("uuid") String uuid) {
        String user=(String)tokenService.getTokenUser().getClaims().get("sub");
        String exist=tempPhotoAccept.get(user);
        if (exist==null) return false;
        tempPhotoAccept.remove(user);
        log.info("Check photo exist{}",uuid);
        return Objects.equals(exist, uuid);
    }
}
