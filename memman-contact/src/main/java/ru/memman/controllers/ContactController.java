package ru.memman.controllers;

import ru.memman.dtos.ContactDto;
import ru.memman.dtos.RecipientDto;
import ru.memman.service.ContactService;
import ru.memman.service.RealManService;
import ru.memman.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/contact/contact")
@AllArgsConstructor
public class ContactController {
    private TokenService tokenService;
    private RealManService realManService;
    private ContactService contactService;

    @PostMapping("/add")
    public void addContact(@RequestBody String externId){
        realManService.addContactToOwner((String)tokenService.getTokenUser().getClaims().get("sub"), externId);
    }
    @GetMapping("/get")
    public List<ContactDto> getContacts(){
        return contactService.getContactsDto((String)tokenService.getTokenUser().getClaims().get("sub"));
    }
    @PostMapping("/edit")
    public void editContact(@RequestBody RecipientDto recipientDto){
      contactService.editContact((String)tokenService.getTokenUser().getClaims().get("sub"), recipientDto);
    }
    @GetMapping("/access/{uuid}")
    public boolean getAccessContact(@PathVariable("uuid") String uuid){
        return contactService.getAccessContact((String)tokenService.getTokenUser().getClaims().get("sub"),uuid);
    }
}
