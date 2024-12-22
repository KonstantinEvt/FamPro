package org.example.controllers;

import com.example.dtos.TokenUser;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.example.services.KeyCloakService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/manage")
@AllArgsConstructor
public class UserController {
    private final KeyCloakService keyCloakService;


    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping("/add")
    public ResponseEntity<TokenUser> addUser(@RequestBody TokenUser tokenUser) {
        System.out.println("Oooooo");
        keyCloakService.addUser(tokenUser);
        System.out.println("Ogogo");
        return ResponseEntity.status(201).body(tokenUser);
    }

    @PostMapping("/edit")
    public ResponseEntity<TokenUser> editUser(@RequestBody TokenUser tokenUser) {
        keyCloakService.editUser(tokenUser);
        return ResponseEntity.status(201).body(tokenUser);
    }

    @PostMapping("")
    public void chooseLocalisation(@RequestBody String loc) throws URISyntaxException {
        keyCloakService.chooseLocalisation(loc);
    }
}
