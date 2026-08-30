package ru.memman.controllers;

import org.springframework.web.bind.annotation.*;
import ru.memman.dtos.TokenUser;
import lombok.AllArgsConstructor;
import ru.memman.services.KeyCloakService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

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

    @GetMapping("/locale/{loc}")
    public void chooseLocalisation(@PathVariable("loc") String loc)  {
        keyCloakService.chooseLocalisation(loc);
    }
}
