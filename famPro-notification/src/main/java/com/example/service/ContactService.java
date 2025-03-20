package com.example.service;

import com.example.dtos.Directive;
import com.example.dtos.RecipientDto;
import com.example.entity.Contact;
import com.example.entity.Recipient;
import com.example.enums.KafkaOperation;
import com.example.enums.SwitchPosition;
import com.example.repository.ContactRepo;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class ContactService {
    private final ContactRepo contactRepo;
    private final List<Directive> directives;
    private final TokenService tokenService;

    public ContactService(ContactRepo contactRepo, @Qualifier("directiveResource") List<Directive> directives, TokenService tokenService) {
        this.contactRepo = contactRepo;
        this.directives = directives;
        this.tokenService = tokenService;
    }

    @Transactional
    public Contact addContact(Recipient owner, RecipientDto recipientDto,Recipient person) {
        Contact contact = Contact.builder()
                .owner(owner)
                .person(person)
                .externId(person.getExternId())
                .primePhoto(person.getUrlPhoto()!=null)
                .build();
        changeContact(contact,recipientDto,person);
        return contactRepo.save(contact);
    }

    @Transactional
    public void removeContact(Recipient owner, Recipient person) {
        contactRepo.delete(owner.getContacts()
                .stream()
                .filter(x -> x.getPerson().equals(person))
                .findFirst().orElseThrow(() -> new RuntimeException("Contact already absent")));
    }

    @Transactional
    public Contact editContact(Recipient person, RecipientDto recipientDto, Contact contact) {
        changeContact(contact, recipientDto,person);
        return contactRepo.save(contact);
    }

    private void changeContact(Contact contact, RecipientDto recipientDto,Recipient person) {
        if (recipientDto.getName() != null && !recipientDto.getName().isBlank())
            contact.setName(recipientDto.getName());
        else contact.setName(person.getNickName());
        if (recipientDto.getUrlPhoto() != null && !recipientDto.getUrlPhoto().isBlank())
        {contact.setContactPhoto(true);
            directives.add(new Directive((String) tokenService.getTokenUser().getClaims().get("sub"),recipientDto.getExternId(), SwitchPosition.MAIN, KafkaOperation.ADD));
        }
        else contact.setContactPhoto(false);
        if (recipientDto.getInfo() != null && !recipientDto.getInfo().isBlank())
            contact.setInfo(recipientDto.getInfo());
    }
}