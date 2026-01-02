package com.example.service;

import com.example.dtos.DirectiveGuards;
import com.example.entity.DeferredDirective;
import com.example.entity.ShortFamilyMember;
import com.example.enums.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
@Log4j2
public class SendAndFormService {
    private final LinkedList<DirectiveGuards> checkLevelDirective;
    private final LinkedList<DirectiveGuards> guardsLetters;
    private final List<DirectiveGuards> contactDirective;
    private final Map<UUID, Localisation> tempLocalisation;


    SendAndFormService(@Qualifier("checkLevelDirective") LinkedList<DirectiveGuards> checkLevelDirective,
                       @Qualifier("directiveGuards") LinkedList<DirectiveGuards> guardsLetters,
                       @Qualifier("contactDirective") List<DirectiveGuards> contactDirective,
                       @Qualifier("tempLocalisation") Map<UUID, Localisation> tempLocalisation) {
        this.checkLevelDirective = checkLevelDirective;
        this.guardsLetters = guardsLetters;
        this.contactDirective = contactDirective;
        this.tempLocalisation = tempLocalisation;
    }

    /***
     * Форма отправки изменений CheckStatus персон в Storage модуль
     * @param tokenUser пользаватель инииатор изменений, NotNull, при линковании - Creator of Person
     * @param person основная персона изменений
     * @param position при наличии сопутствующих персон определяет CheckStatus основной персоны
     * @param operation пока тут Rename
     * @param changingPersons сопутствующие персоны изменений
     * @param checkStatus устанавлемый checkStatus для сопутствующих персон, если position null, то и для основной персоны
     */
    public void formDirectiveToStorageForChangeStatus(String tokenUser,
                                                      String person,
                                                      SwitchPosition position,
                                                      KafkaOperation operation, Set<String> changingPersons, CheckStatus checkStatus) {
        checkLevelDirective.add(DirectiveGuards.builder()
                .tokenUser(tokenUser)
                .operation(operation)
                .checkStatus(checkStatus)
                .switchPosition(position)
                .person(person)
                .guards(changingPersons)
                .build());
        log.info("Directive to storage for change CheckLevel is send");
    }

    /***
     * Отпрвка директивы в модуль Notification
     * @param userUuid отправитель-получатель
     * @param person тот, кто тронут
     * @param personInfo info о персоне
     * @param attention место влияния
     */
    public void sendAttentionToUser(String userUuid, String personInfo, ShortFamilyMember person, Attention attention) {
        DirectiveGuards directiveGuards = DirectiveGuards.builder()
                .created(new Timestamp(System.currentTimeMillis()))
                .tokenUser(userUuid)
                .operation(KafkaOperation.ADD)
                .localisation(Objects.requireNonNullElse(tempLocalisation.get(UUID.fromString(userUuid)),Localisation.EN))
                .info2(personInfo)
                .build();
        switch (attention) {
            case MODERATE -> {
                directiveGuards.setSubject(Subject.MODERATION_WARNING);
                directiveGuards.setSwitchPosition(SwitchPosition.MAIN);
            }
            case RIGHTS -> {
                directiveGuards.setSubject(Subject.RIGHTS);
                directiveGuards.setSwitchPosition(SwitchPosition.FATHER);
            }
            case LINK -> {
                if (person == null) {
                    directiveGuards.setSubject(Subject.LINK_NEGATIVE);
                    directiveGuards.setPerson(personInfo);
                } else {
                    directiveGuards.setSubject(Subject.LINK_POSITIVE);
                    directiveGuards.setPerson(personInfo);
                    directiveGuards.setInfo1(person.getUuid().toString());
                    directiveGuards.setPhotoExist(person.isPrimePhoto());
                }
                directiveGuards.setSwitchPosition(SwitchPosition.PRIME);
            }
            default -> {
                directiveGuards.setSubject(Subject.UNKNOWN);
                directiveGuards.setSwitchPosition(SwitchPosition.CHILD);
            }
        }
        guardsLetters.add(directiveGuards);
    }

    public void sendVotingDirective(DeferredDirective directive, Set<String> guards) {
        DirectiveGuards directiveGuards = DirectiveGuards.builder()
                .id(directive.getUuid().toString())
                .created(directive.getCreated())
                .operation(KafkaOperation.EDIT)
                .guards(guards)
                .tokenUser(directive.getTokenUser())
                .switchPosition(directive.getSwitchPosition())
                .info1(directive.getDirectiveMember().getFullName())
                .info2(directive.getInfo())
                .localisation(Objects.requireNonNullElse(directive.getLocalisation(), Localisation.RU))
                .build();
        if (directive.getSwitchPosition() == SwitchPosition.BIRTH) directiveGuards.setSubject(Subject.LINK);
        else directiveGuards.setSubject(Subject.VOTING);
        guardsLetters.add(directiveGuards);
    }

    public void sendChangeInStorageByNegative(DeferredDirective directive, String person) {
        checkLevelDirective.add(DirectiveGuards.builder()
                .tokenUser(directive.getTokenUser())
                .operation(KafkaOperation.EDIT)
                .switchPosition(directive.getSwitchPosition())
                .person(person)
                .build());
        log.info("Directive to storage for remove parent-child link is send");
    }

    public void sendAddingNewContacts(Set<String> guards) {
        contactDirective.add(DirectiveGuards.builder()
                .operation(KafkaOperation.ADD)
                .switchPosition(SwitchPosition.MAIN)
                .guards(guards)
                .build());
        log.info("Directive for contact is send");
    }
}
