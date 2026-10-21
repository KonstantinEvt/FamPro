package ru.memman.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.memman.dtos.ContactDto;
import ru.memman.dtos.DirectiveGuards;
import ru.memman.dtos.RecipientDto;
import ru.memman.entity.Contact;
import ru.memman.entity.ContactLocal;
import ru.memman.entity.RealMen;
import ru.memman.enums.*;
import ru.memman.holders.OnlineContactHolder;
import ru.memman.holders.TranscriptHolder;
import ru.memman.repository.ContactRepository;
import ru.memman.repository.RealManRepository;
import ru.memman.transcriters.AbstractTranscripter;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ContactService {
    private final ContactRepository contactRepository;
    private final RealManRepository realManRepository;
    private final OnlineContactHolder onlineContactHolder;
    private final TranscriptHolder transcriptHolder;
    private final LinkedList<List<ContactDto>> checkRelation;
    private final LinkedList<DirectiveGuards> directiveRights;


    public ContactService(ContactRepository contactRepository,
                          RealManRepository realManRepository,
                          OnlineContactHolder onlineContactHolder,
                          TranscriptHolder transcriptHolder,
                          LinkedList<List<ContactDto>> checkRelation,
                          LinkedList<DirectiveGuards> directiveRights) {
        this.contactRepository = contactRepository;
        this.realManRepository = realManRepository;
        this.onlineContactHolder = onlineContactHolder;
        this.transcriptHolder = transcriptHolder;
        this.checkRelation = checkRelation;
        this.directiveRights = directiveRights;
    }

    @Transactional(readOnly = true)
    public void addContactsToHolder(RealMen realMen) {
//        Set<UUID> contactSetString = realMen.getContacts().stream().map(Contact::getUuid).collect(Collectors.toSet());
        Set<String> persons = realMen.getContacts().stream().map(Contact::getPersonUuid).collect(Collectors.toSet());
        Set<String> outHolder = new HashSet<>();
        Map<String, Contact> contactMap = new HashMap<>();
        List<Contact> listToResetRelation = new ArrayList<>();
        Set<String> rightAgree = new HashSet<>();
        log.info("будем искать его контакты");
        Set<Contact> contactSet = new HashSet<>(contactRepository.getContactsWithLocalisations(realMen.getContacts()));
        realManRepository.detach(realMen);
        if (!contactSet.isEmpty()) contactSet.forEach(x -> {
            contactRepository.detach(x);
            x.setChange(true);
            log.info("Найден контакт с {}",x.getPersonUuid());
            contactMap.put(x.getPersonUuid(), x);
        }); else   log.info("не юзер, а ноль - ну ноль");
        for (String person : persons) {
            RealMen men = onlineContactHolder.getOnlineManMap().get(person);
            if (men != null) {
                men.setLastTimeUse(new Timestamp(System.currentTimeMillis()));
                contactMap.get(men.getExternUuid()).setPerson(men);
            } else outHolder.add(person);
        }
        Set<RealMen> personsOutHolder = new HashSet<>();
        if (!outHolder.isEmpty()) personsOutHolder.addAll(realManRepository.getPersonsByUuids(outHolder));
        if (!personsOutHolder.isEmpty()) personsOutHolder.forEach(x -> {
            realManRepository.detach(x);
            onlineContactHolder.getOnlineManMap().put(x.getExternUuid(), x);
            contactMap.get(x.getExternUuid()).setPerson(x);
            x.setLastTimeUse(new Timestamp(System.currentTimeMillis()));
        });
        log.info("still life");
        AbstractTranscripter transcripter = transcriptHolder.getTranscript(realMen.getLocalisation());
        contactMap.values().forEach(x -> {
            if (x.getLastTimeUse() == null || x.getLastTimeUse().before(x.getPerson().getLastUpdate())) {
                x.setRelation(SecretLevel.OPEN);
                listToResetRelation.add(x);
            } else {
                if (x.getPerson().isExistPrimePhoto() && x.getRelation().ordinal() >= x.getPerson().getSecretLevelPhoto().ordinal())
                    rightAgree.add(String.valueOf(SwitchPosition.LITTLE.ordinal()).concat(x.getPersonUuid()));
            }
            x.setLastTimeUse(new Timestamp(System.currentTimeMillis()));
            if (x.getLocals().stream().filter(y -> y.getLocalisation() == realMen.getLocalisation()).findFirst().isEmpty())
                generateContactLocal(transcripter, x, realMen.getLocalisation());
        });
        log.info("still life2");
        onlineContactHolder.getOnlineContactsMap().put(realMen.getExternUuid(), contactMap.values());
        if (!listToResetRelation.isEmpty()) {
            Optional<Contact> himself = listToResetRelation.stream().filter(x -> Objects.equals(x.getPersonUuid(), realMen.getExternUuid())).findFirst();
            himself.ifPresent(listToResetRelation::remove);
            if (!listToResetRelation.isEmpty())
                checkRelation.add(generateSimpleContactListDto(listToResetRelation, realMen.getExternUuid()));
        }
        if (!rightAgree.isEmpty()) directiveRights.add(DirectiveGuards.builder()
                .tokenUser(realMen.getExternUuid())
                .operation(KafkaOperation.ADD)
                .guards(rightAgree).build());
    }

    private ContactLocal generateContactLocal(AbstractTranscripter transcripter, Contact contact, Localisation localisation) {
        ContactLocal contactLocal = ContactLocal.builder()
                .localisation(localisation)
                .origin(false)
                .name(transcripter.transcritWordToLocalisation(contact.getName()))
                .info(transcripter.getDefaultInfo()).build();
        contact.getLocals().add(contactLocal);
        return contactLocal;
    }

    private List<ContactDto> generateSimpleContactListDto(List<Contact> contactList, String owner) {
        List<ContactDto> contactDtoList = new ArrayList<>();
        contactList.forEach(x -> contactDtoList.add(ContactDto.builder()
                .uuid(x.getUuid())
                .ownerId(owner)
                .personUuid(x.getPersonUuid()).build()));
        return contactDtoList;
    }

    private SecretLevel getSecretLevel(int ord) {
        for (SecretLevel secretLevel :
                SecretLevel.values()) {
            if (secretLevel.ordinal() == ord) return secretLevel;
        }
        return SecretLevel.OPEN;
    }

    private Relation getRelation(String name) {
        for (Relation relation :
                Relation.values()) {
            if (relation.name().equals(name)) return relation;
        }
        return Relation.OTHER;
    }

    @Transactional
    public void setContactsRelations(List<ContactDto> contactsDtos) {
        if (contactsDtos.isEmpty()) return;
        Set<String> rightsAgree = new HashSet<>();
        Set<String> rightsDecline = new HashSet<>();
        if (contactsDtos.stream().anyMatch(x -> x.getUuid() == null)) checkAndCreatNewContacts(contactsDtos);
        else {
            Collection<Contact> contactsInHolder = onlineContactHolder.getOnlineContactsMap().get(contactsDtos.get(0).getOwnerId());
            if (contactsInHolder.isEmpty()) return;
            Map<UUID, Contact> contacts = contactsInHolder.stream().collect(Collectors.toMap(Contact::getUuid, x -> x));

            contactsDtos.forEach(x -> {
                Contact contact = contacts.get(x.getUuid());
                contact.setRelation(getSecretLevel(x.getRelation()));
                contact.setSpecify(getRelation(x.getSpecify()));
                if (contact.getPerson().isExistPrimePhoto() && x.getRelation() >= contact.getPerson().getSecretLevelPhoto().ordinal()) {
                    rightsAgree.add(String.valueOf(SwitchPosition.LITTLE.ordinal()).concat(contact.getPersonUuid()));
                    contact.setChange(true);
                } else if (contact.getPerson().isExistPrimePhoto()) {
                    rightsDecline.add(String.valueOf(SwitchPosition.LITTLE.ordinal()).concat(contact.getPersonUuid()));
                    contact.setChange(true);
                }
            });
        }
        if (!rightsAgree.isEmpty()) directiveRights.add(DirectiveGuards.builder()
                .tokenUser(contactsDtos.get(0).getOwnerId())
                .operation(KafkaOperation.ADD)
                .guards(rightsAgree).build());
        if (!rightsDecline.isEmpty()) directiveRights.add(DirectiveGuards.builder()
                .tokenUser(contactsDtos.get(0).getOwnerId())
                .operation(KafkaOperation.REMOVE)
                .guards(rightsDecline).build());
    }

    public List<ContactDto> getContactsDto(String ownerId) {
        List<ContactDto> contactDtoList = new ArrayList<>();
        List<Contact> listToResetRelation = new ArrayList<>();
        RealMen owner = onlineContactHolder.getOnlineManMap().get(ownerId);
        if (owner == null) return new ArrayList<>();
        Localisation localisation = owner.getLocalisation();
        log.info("Текущая локализация: {}",localisation.name());
        Collection<Contact> contacts = onlineContactHolder.getOnlineContactsMap().get(ownerId);
        AbstractTranscripter transcripter = transcriptHolder.getTranscript(localisation);
        if (contacts==null||contacts.isEmpty()) {
            log.info("Contact list is empty");
            return contactDtoList;
        }
        for (Contact con :
                contacts) {
            Optional<ContactLocal> contactLocal;
            if (owner.getLocalisation() == Localisation.EN)
                contactLocal = Optional.of(ContactLocal.builder().name(con.getName()).info(con.getInfo()).build());
            else {
                contactLocal = con.getLocals().stream().filter(y -> y.getLocalisation() == owner.getLocalisation()).findFirst();
                if (contactLocal.isEmpty())
                    contactLocal = Optional.of(generateContactLocal(transcripter, con, owner.getLocalisation()));
            }
            RealMen person = con.getPerson();
            if (!Objects.equals(con.getPerson().getExternUuid(), ownerId)&&(con.getLastTimeUse() == null || con.getLastTimeUse().before(person.getLastUpdate()))) {
               listToResetRelation.add(con);
            }
            con.setLastTimeUse(new Timestamp(System.currentTimeMillis()));
            contactDtoList.add(ContactDto.builder()
                    .personUuid(con.getPersonUuid())
                    .linkPersonUuid(person.getLinkExternId())
                    .localisation(localisation)
                    .personLocalisation(person.getLocalisation())
                    .primePhoto(person.isExistPrimePhoto())
                    .contactPhoto(con.isContactPhoto())
                    .specify(transcripter.getSpecify(con.getSpecify()))
                    .preferContact(con.isPreferContact())
                    .lastOnline(person.getLastOnline())
                    .online(person.isOnline())
                    .change(con.isChange())
                    .email(person.getEmail())
                    .phone(person.getMainPhone())
                    .relation(con.getRelation().ordinal())
                    .secretLevelPhone(person.getSecretLevelPhone().ordinal())
                    .secretLevelPhoto(person.getSecretLevelPhoto().ordinal())
                    .secretLevelEmail(person.getSecretLevelEmail().ordinal())
                    .info(contactLocal.get().getInfo())
                    .name(contactLocal.get().getName())
                    .build());
            con.setChange(false);
        }
        if (!listToResetRelation.isEmpty())
            checkRelation.add(generateSimpleContactListDto(listToResetRelation, owner.getExternUuid()));

        return contactDtoList;
    }

    @Transactional
    public void checkAndCreatNewContacts(List<ContactDto> contactDtoList) {
        if (contactDtoList.size() > 1) {
            List<String> realMenUuid = contactDtoList.stream().map(ContactDto::getOwnerId).toList();
            Map<String, RealMen> realMenMap = realManRepository.getRealManWithContactsByExternId(realMenUuid).stream().collect(Collectors.toMap(RealMen::getExternUuid, x -> x));
            List<Contact> newContacts = new ArrayList<>();
            List<Contact> updateContacts = new ArrayList<>();
            for (RealMen person :
                    realMenMap.values()) {
                for (RealMen owner :
                        realMenMap.values()) {
                    if (owner.getContacts() == null) owner.setContacts(new HashSet<>());
                    Collection<Contact> contactsInHolder = onlineContactHolder.getOnlineContactsMap().get(owner.getExternUuid());
                    if (contactsInHolder == null) {
                        Optional<Contact> contact = owner.getContacts().stream().filter(x -> Objects.equals(x.getPersonUuid(), person.getExternUuid())).findFirst();
                        if (contact.isEmpty()) newContacts.add(creatNewContact(owner, person));
                        else if (!Objects.equals(person.getExternUuid(), owner.getExternUuid())) {
                            contact.get().setLastTimeUse(null);
                            updateContacts.add(contact.get());
                        }
                    } else {
                        Optional<Contact> contact = contactsInHolder.stream().filter(x -> Objects.equals(x.getPersonUuid(), person.getExternUuid())).findFirst();
                        if (contact.isEmpty()) newContacts.add(creatNewContact(owner, person));
                        else if (!Objects.equals(person.getExternUuid(), owner.getExternUuid()))
                            contact.get().setLastTimeUse(null);
                    }
                }
            }
            if (!newContacts.isEmpty()) contactRepository.persistAll(newContacts);
            if (!updateContacts.isEmpty()) contactRepository.updateAll(updateContacts);
        } else if (contactDtoList.size() == 1) {
            Optional<String> ownerUuid = contactDtoList.stream().map(ContactDto::getOwnerId).findFirst();
            RealMen realMen = onlineContactHolder.getOnlineManMap().get(ownerUuid.get());
            if (realMen == null) {
                realMen = realManRepository.getRealManWithContactsByExternId(ownerUuid.get()).orElseThrow(() -> new RuntimeException("request to unknown user"));
                onlineContactHolder.getOnlineManMap().put(ownerUuid.get(), realMen);
                if (realMen.getContacts() != null)
                    onlineContactHolder.getOnlineContactsMap().put(ownerUuid.get(), realMen.getContacts());
            }
            if (realMen.getContacts() == null) {
                realMen.setContacts(new HashSet<>());
            }
            onlineContactHolder.getOnlineContactsMap().putIfAbsent(ownerUuid.get(), new ArrayList<>());
            if (realMen.getContacts().stream().filter(x -> Objects.equals(x.getPersonUuid(), ownerUuid.get())).findFirst().isEmpty()) {
                Contact himself = creatNewContact(realMen, realMen);
                onlineContactHolder.getOnlineContactsMap().get(ownerUuid.get()).add(himself);
                contactRepository.persist(himself);
            }
        }
    }

    public void editContact(String userUuid, RecipientDto recipientDto) {
    }

    public boolean getAccessContact(String userUuid, String uuid) {
        String personUuid = uuid.substring(1);
        log.info("Получаем добро на little photo персоны {}", personUuid);
        Optional<Contact> contact = onlineContactHolder.getOnlineContactsMap().get(userUuid).stream().filter(x -> Objects.equals(x.getPersonUuid(), personUuid)).findFirst();
        return contact.isPresent() && contact.get().getRelation().ordinal() >= contact.get().getPerson().getSecretLevelPhoto().ordinal();
    }

    public Contact creatNewContact(RealMen owner, RealMen person) {
        Contact contact = Contact.builder()
                .owner(owner)
                .person(person)
                .personUuid(person.getExternUuid())
                .contactPhoto(false)
                .origin(false)
                .build();
        AbstractTranscripter transcripterOwner = transcriptHolder.getTranscript(owner.getLocalisation());
        contact.setInfo(transcriptHolder.getTranscript(Localisation.EN).getDefaultInfo());
        contact.setName(transcriptHolder.getTranscript(person.getLocalisation()).transcritWordFromLocalisation(person.getNickName()));
        if (owner.getLocalisation() != Localisation.EN) {
            {
                contact.setLocals(new HashSet<>());
                if (person.getLocalisation() != owner.getLocalisation()) contact.getLocals().add(ContactLocal.builder()
                        .localisation(owner.getLocalisation())
                        .name(transcripterOwner.transcritWordToLocalisation(contact.getName()))
                        .info(transcripterOwner.getDefaultInfo())
                        .origin(false)
                        .build());
                else contact.getLocals().add(ContactLocal.builder()
                        .localisation(owner.getLocalisation())
                        .name(contact.getName())
                        .info(transcripterOwner.getDefaultInfo())
                        .origin(true)
                        .build());
            }
        }

        if (Objects.equals(owner.getExternUuid(), person.getExternUuid())) {
            contact.setLastTimeUse(new Timestamp(System.currentTimeMillis()));
            contact.setRelation(SecretLevel.CONFIDENTIAL);
            contact.setSpecify(Relation.PERSON);
        }
        return contact;
    }
}
