package com.example.controllers;

import com.example.dtos.ContactDto;
import com.example.dtos.RecipientDto;
import com.example.service.RecipientService;
import com.example.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/recipient")
@AllArgsConstructor
public class RecipientController {
    private TokenService tokenService;
    private RecipientService recipientService;

    @PostMapping("/contact/add")
    public ContactDto addContact(@RequestBody RecipientDto recipientDto){
        return recipientService.addContactToOwner((String)tokenService.getTokenUser().getClaims().get("sub"), recipientDto);
    }
    @GetMapping("/contact/get")
    public Set<ContactDto> getContacts(){
        return recipientService.getContactDtos((String)tokenService.getTokenUser().getClaims().get("sub"));
    }
    @PostMapping("/contact/edit")
    public ContactDto editContact(@RequestBody RecipientDto recipientDto){
        return recipientService.editContact((String)tokenService.getTokenUser().getClaims().get("sub"), recipientDto);
    }
}
