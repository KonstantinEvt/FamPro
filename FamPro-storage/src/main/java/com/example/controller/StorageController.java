package com.example.controller;

import com.example.service.ServiceOfStorageBD;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/Storage/")
public class StorageController {
    ServiceOfStorageBD serviceOfStorageBD;
    @GetMapping("/save{filename}")
    public ResponseEntity<String> saveDataInFile(@PathVariable String filename){
        serviceOfStorageBD.saveDataToFile(filename);
        return ResponseEntity.status(222).body("File is saved");
    }
    @GetMapping("/recover{filename}")
    public  ResponseEntity<String> recoverBasefromFile(@PathVariable String filename){
        serviceOfStorageBD.recoverBaseFromFile(filename);
        return ResponseEntity.status(223).body("base is good");
    }
}
