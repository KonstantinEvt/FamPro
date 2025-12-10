package com.example.service;

import com.example.dtos.*;
import com.example.entity.AloneNew;
import com.example.entity.Contact;
import com.example.entity.Recipient;
import com.example.enums.KafkaOperation;
import com.example.holders.StandardInfoHolder;
import com.example.mappers.AloneNewMapper;
import com.example.mappers.ContactMapper;
import com.example.models.StandardInfo;
import com.example.repository.NotificationRepo;
import com.example.repository.RecipientRepo;
import com.example.repository.RecipientRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
@AllArgsConstructor
@Log4j2
public class RecipientService {
    private RecipientRepo recipientRepo;
    private StandardInfoHolder standardInfoHolder;
    private AloneNewMapper letterMapper;
    private ContactMapper contactMapper;
    private ContactService contactService;
    private NotificationRepo notificationRepo;
    private LinkedList<DirectiveGuards> directiveRights;
    private RecipientRepository recipientRepository;

    @Transactional(readOnly = true)
    public Recipient findRecipient(String externId) {
        return recipientRepo.findByExternUuid(externId).orElse(null);
    }

    @Transactional(readOnly = true)
    public Optional<Recipient> findRecipient(Long Id) {
        return recipientRepo.findById(Id);
    }

    @Transactional(readOnly = true)
    public Recipient getRecipient(TokenUser tokenUser) {
        Recipient recipient = findRecipient((String) tokenUser.getClaims().get("sub"));
        return (recipient != null) ? recipient : creatRecipient(tokenUser);
    }

    @Transactional
    public void changeRecipient(DirectiveGuards directiveGuards) {
        Recipient requester = findRecipient(directiveGuards.getTokenUser());
        requester.setUrlPhoto(directiveGuards.isPhotoExist());
        requester.setLinkExternId(directiveGuards.getPerson());
        recipientRepository.update(requester);
    }

    @Transactional
    public Recipient creatRecipient(TokenUser tokenUser) {
        Recipient recipient = Recipient.builder()
                .nickName(tokenUser.getNickName())
                .externUuid((String) tokenUser.getClaims().get("sub"))
                .commonReading("")
                .systemReading("")
                .build();
        if (tokenUser.getEmail() != null) recipient.setEmail(tokenUser.getEmail());
        recipientRepo.save(recipient);
        return recipient;
    }

    @Transactional
    public void saveRecipient(Recipient recipient) {
        recipientRepo.save(recipient);
    }

    @Transactional
    public void inlineProcess(DirectiveGuards directive) {
        Optional<Recipient> recipient = recipientRepository.getRecipientWithReceiveLettersByExternId(directive.getTokenUser());
        StandardInfo standardInfo = new StandardInfo();
        standardInfo.setLocalisation(directive.getLocalisation());
        if (recipient.isEmpty()) {
            recipientRepository.persistNewRecipient(Recipient.builder()
                    .nickName(directive.getPerson())
                    .externUuid(directive.getTokenUser())
                    .commonReading("")
                    .systemReading("")
                    .localisation(directive.getLocalisation())
                    .build());
            standardInfoHolder.getOnlineInfo().put(directive.getTokenUser(), standardInfo);
        } else reloadOnlineRecipient(recipient.get(), directive.getPerson(),standardInfo);
    }

    @Transactional
    public void reloadOnlineRecipient(Recipient recipient, String nickName,StandardInfo standardInfo) {
        List<AloneNew> receivedLetters = recipient.getReceivedLetters();
        if (receivedLetters != null && !receivedLetters.isEmpty()) {
            List<AloneNew> listWithSenders = notificationRepo.getNewLettersWithSenders(receivedLetters);
            for (AloneNew letter :
                    listWithSenders) {
                AloneNewDto aloneNewDto = letterMapper.entityToDto(letter);
                if (letter.getSendFrom() != null) {
                    aloneNewDto.setSendingFromAlt(letter.getSendFrom().getNickName());
                } else {
                    aloneNewDto.setSendingFrom(aloneNewDto.getId().toString());
                }
                standardInfo.addNewMessage(aloneNewDto);
            }
        }
        standardInfo.getSystemGlobalRead().addAll(recipient.getSystemReading().chars().map(x -> x - 48).boxed().toList());
        standardInfo.getCommonGlobalRead().addAll(recipient.getCommonReading().chars().map(x -> x - 48).boxed().toList());

        standardInfoHolder.getOnlineInfo().put(recipient.getExternUuid(), standardInfo);
        if (!Objects.equals(recipient.getNickName(), nickName)||!Objects.equals(recipient.getLocalisation(), standardInfo.getLocalisation())) {
            recipient.setNickName(nickName);
            recipient.setLocalisation(standardInfo.getLocalisation());
            recipientRepository.update(recipient);
        }
    }

    @Transactional(readOnly = true)
    public Set<ContactDto> getContactDtos(String recipientExternId) {
        Recipient recipient = notificationRepo.findRecipientWithContacts(recipientExternId);
        if (recipient != null && recipient.getContacts() != null && !recipient.getContacts().isEmpty())
            directiveRights.add(DirectiveGuards.builder()
                    .operation(KafkaOperation.ADD)
                    .person(recipient.getExternUuid())
                    .guards(recipient.getContacts().stream().map(Contact::getExternId).collect(Collectors.toSet()))
                    .build());
        else return new HashSet<>();
        return contactMapper.entitySetToDtoSet(recipient.getContacts());
    }

    @Transactional(readOnly = true)
    public ContactDto getContact(String recipientExternId, String personName) {
        Recipient owner = notificationRepo.findRecipientWithContacts(recipientExternId);
        if (owner.getContacts() == null) return null;
        return contactMapper.entityToDto(findContactByName(owner, personName));
    }

    @Transactional(readOnly = true)
    public Contact findContactByName(Recipient recipient, String personName) {
        for (Contact contact :
                recipient.getContacts()) {
            if (contact.getName().equals(personName)) return contact;
        }
        return null;
    }

    @Transactional
    public ContactDto addContactToOwner(String recipientExternId, RecipientDto recipientDto) {
        Recipient owner = notificationRepo.findRecipientWithContacts(recipientExternId);
        if (owner.getContacts() == null) owner.setContacts(new HashSet<>());
        Recipient person;
        if (recipientDto.getLinkExternId() != null)
            person = notificationRepo.findRecipientWithPodpisotaByLink(recipientDto.getLinkExternId());
        else person = notificationRepo.findRecipientWithPodpisota(recipientDto.getExternId());
        if (person == null) throw new RuntimeException("Такового подписанта еще нет");
        for (Contact contact :
                owner.getContacts()) {
            for (Contact podpisota :
                    person.getPodpisota()) {
                if (podpisota.equals(contact)) throw new RuntimeException("Такой контакт у Вас уже есть!");
            }
        }
        ContactDto contactDto = contactMapper.entityToDto(contactService.addContact(owner, recipientDto, person));
        contactDto.setOwnerId(owner.getExternUuid());
        return contactDto;
    }

    @Transactional
    public ContactDto editContact(String recipientExternId, RecipientDto recipientDto) {
        Recipient owner = notificationRepo.findRecipientWithContacts(recipientExternId);
        Recipient person = notificationRepo.findRecipientWithPodpisota(recipientDto.getExternId());
        Set<Contact> podpisota = person.getPodpisota();
        for (Contact contact :
                podpisota) {
            if (contact.getOwner() == owner) {
                ContactDto contactDto = contactMapper.entityToDto(contactService.editContact(person, recipientDto, contact));
                contactDto.setOwnerId(owner.getExternUuid());
                return contactDto;
            }
        }
        return null;
    }


}

