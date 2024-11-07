package org.example.controllers;

import com.example.dtos.TokenUser;
import lombok.AllArgsConstructor;
import org.example.services.KeyCloakService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage")
@AllArgsConstructor
public class UserController {
//    private final WebClient webClient;
    private final KeyCloakService keyCloakService;


//    @RolesAllowed("BaseUser")
    @PostMapping("/add")
    public ResponseEntity<TokenUser> addUser(@RequestBody TokenUser tokenUser) {
        keyCloakService.addUser(tokenUser);
        return ResponseEntity.status(201).body(tokenUser);
    }

    @GetMapping("/token")
    public String token() {
        System.out.println("vasya");
        return "vasya";
    }
}
