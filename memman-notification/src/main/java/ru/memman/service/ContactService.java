package ru.memman.service;

import ru.memman.dtos.Directive;
import ru.memman.dtos.DirectiveGuards;
import ru.memman.dtos.RecipientDto;
import ru.memman.entity.Contact;
import ru.memman.entity.Recipient;
import ru.memman.enums.KafkaOperation;
import ru.memman.enums.NewsCategory;
import ru.memman.enums.SwitchPosition;
import ru.memman.mappers.RecipientMapper;
import ru.memman.repository.ContactRepo;
import ru.memman.repository.NotificationRepo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ContactService {
    private final ContactRepo contactRepo;
    private final List<Directive> directives;
    private final NotificationRepo notificationRepo;
    private final RecipientMapper recipientMapper;

    public ContactService(ContactRepo contactRepo, @Qualifier("directiveResource") List<Directive> directives, NotificationRepo notificationRepo, RecipientMapper recipientMapper) {
        this.contactRepo = contactRepo;
        this.directives = directives;
        this.notificationRepo = notificationRepo;
        this.recipientMapper = recipientMapper;
    }

    @Transactional
    public Contact addContact(Recipient owner, RecipientDto recipientDto, Recipient person) {
        Contact contact = Contact.builder()
                .owner(owner)
                .person(person)
                .externId(person.getLinkExternId())
                .status(KafkaOperation.ADD)
                .primePhoto(person.isUrlPhoto())
                .build();
        changeContact(contact, recipientDto, person);
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
        changeContact(contact, recipientDto, person);
        return contactRepo.save(contact);
    }

    private void changeContact(Contact contact, RecipientDto recipientDto, Recipient person) {
        if (recipientDto.getName() != null && !recipientDto.getName().isBlank()) {
            contact.setName(recipientDto.getName());
            contact.setStatus(KafkaOperation.RENAME);
        } else contact.setName(person.getNickName());
        if (recipientDto.getUrlPhoto() != null && recipientDto.getUrlPhoto()) {
            contact.setContactPhoto(true);
            directives.add(new Directive(contact.getExternId(), recipientDto.getLinkExternId(), SwitchPosition.MAIN, KafkaOperation.ADD));
        } else contact.setContactPhoto(false);
        if (recipientDto.getInfo() != null && !recipientDto.getInfo().isBlank())
            contact.setInfo(recipientDto.getInfo());
    }

    @Transactional
    public void refreshContactsByGlobalUnion(Set<String> guards) {
        if (guards == null || guards.isEmpty()) {
            log.info("Set of possible contacts is empty");
            return;
        }
        Set<Recipient> recipients = notificationRepo.findGuards(guards);
        if (recipients == null || recipients.isEmpty() || recipients.size() == 1) {
            log.info("Set of possible contacts is empty");
            return;
        }
        Set<String> externs=recipients.stream().map(Recipient::getLinkExternId).collect(Collectors.toSet());
        List<Pair<String, String>> existingContacts = new ArrayList<>();
        Set<Contact> existContact = notificationRepo.getContactsWithAll(recipients, externs);
        for (Contact contact :
                existContact) {
            existingContacts.add(Pair.of(contact.getOwner().getLinkExternId(), contact.getExternId()));
        }
        List<Recipient> people = new ArrayList<>(recipients);
        for (int i = 0; i < people.size(); i++) {
            for (int j = 0; j < people.size(); j++) {
                if (i != j) {
                    Pair<String, String> contact = Pair.of(people.get(i).getLinkExternId(), people.get(j).getLinkExternId());
                    if (!existingContacts.contains(contact)) {
                        RecipientDto dto = recipientMapper.entityToDto(people.get(j));
                        dto.setInfo("Contact added by kin");
                        dto.setUrlPhoto(false);
                        addContact(people.get(i), dto, people.get(j));
                    }
                }
            }
        }
    }
public boolean getAccessContact(String owner, String recipient ){
        return notificationRepo.getContact(owner, recipient).isPresent();
};
}