package com.example.controllers;

import com.example.service.ServiceOfStorageBD;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Tag(name="Операции со всеми данными")
@RequestMapping("/Storage/")
public class StorageController {
    private final ServiceOfStorageBD serviceOfStorageBD;
    @GetMapping("/save{filename}")
    @Operation(method="Сохранить все данные в файл", description = "Данные в файл", summary = "Сохранить базу в файл")
    public ResponseEntity<String> saveDataToFile(@PathVariable String filename){
        serviceOfStorageBD.saveDataToFile(filename);
        return ResponseEntity.status(222).body("File is saved");
    }
    @GetMapping("/recover{filename}")
    public  ResponseEntity<String> recoverBaseFromFile(@PathVariable String filename){
        serviceOfStorageBD.recoverBaseFromFile(filename);
        return ResponseEntity.status(223).body("base is good");
    }
}
