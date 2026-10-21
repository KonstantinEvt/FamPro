package ru.memman.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import ru.memman.dtos.TokenUser;
import lombok.AllArgsConstructor;
import ru.memman.services.KeyCloakService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/manage")
@AllArgsConstructor
@Log4j2
public class UserController {
    private final KeyCloakService keyCloakService;


    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping("/add")
    public ResponseEntity<TokenUser> addUser(@RequestBody TokenUser tokenUser) {
        log.info("add new user: {}", tokenUser);
        keyCloakService.addUser(tokenUser);
        return ResponseEntity.status(201).body(tokenUser);
    }

    @PostMapping("/edit")
    public ResponseEntity<TokenUser> editUser(@RequestBody TokenUser tokenUser) {
        log.info("edit user: {}", tokenUser);
        keyCloakService.editUser(tokenUser);
        return ResponseEntity.status(201).body(tokenUser);
    }

    @GetMapping("/locale/{loc}")
    public void chooseLocalisation(@PathVariable("loc") String loc)  {
        log.info("set localisation: {}", loc);
        keyCloakService.chooseLocalisation(loc);
    }
}
