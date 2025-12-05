package com.example.service;

import com.example.dtos.DirectiveGuards;
import com.example.dtos.FamilyDirective;
import com.example.entity.DeferredDirective;
import com.example.entity.ShortFamilyMember;
import com.example.enums.Attention;
import com.example.enums.CheckStatus;
import com.example.enums.KafkaOperation;
import com.example.enums.SwitchPosition;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
@Log4j2
public class SendAndFormService {
    private final LinkedList<DirectiveGuards> checkLevelDirective;
    private final LinkedList<DirectiveGuards> guardsLetters;
    private final LinkedList<FamilyDirective> storageDirective;
    private final List<DirectiveGuards> contactDirective;


    SendAndFormService(@Qualifier("checkLevelDirective") LinkedList<DirectiveGuards> checkLevelDirective,
                       @Qualifier("directiveGuards") LinkedList<DirectiveGuards> guardsLetters,
                       @Qualifier("storageDirective") LinkedList<FamilyDirective> storageDirective,
                       @Qualifier("contactDirective") List<DirectiveGuards> contactDirective) {
        this.checkLevelDirective = checkLevelDirective;
        this.guardsLetters = guardsLetters;
        this.storageDirective = storageDirective;
        this.contactDirective = contactDirective;
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
        DirectiveGuards directiveGuards=DirectiveGuards.builder()
                .created(new Timestamp(System.currentTimeMillis()))
                .tokenUser(userUuid)
                .operation(KafkaOperation.ADD)
                .info2(personInfo)
                .build();
        switch (attention) {
            case MODERATE -> {
                directiveGuards.setInfo1("You are trying changing person under voting or moderation");
                directiveGuards.setSwitchPosition(SwitchPosition.MAIN);
            }
            case RIGHTS -> {
                directiveGuards.setInfo1("You are trying changing person without rights");
                directiveGuards.setSwitchPosition(SwitchPosition.FATHER);
            }
            case LINK -> {
                if (person == null) {
                    directiveGuards.setInfo1("Link is rejected");
                } else {
                    directiveGuards.setInfo1("You are successful linked");
                    directiveGuards.setPerson(person.getUuid().toString());
                    directiveGuards.setPhotoExist(person.isPrimePhoto());
                }
                directiveGuards.setSwitchPosition(SwitchPosition.PRIME);
            }
            case NEGATIVE -> {
                directiveGuards.setInfo1("You request for linking is reject");
                directiveGuards.setSwitchPosition(SwitchPosition.MOTHER);
            }
            default -> {
                directiveGuards.setInfo1("You are do something. But we are unknown that");
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
                .build();
        directiveGuards.setNumber1(0L);
        directiveGuards.setNumber2(0);
        guardsLetters.add(directiveGuards);
    }

    public void sendChangeInStorageByNegative(DeferredDirective directive, String person) {
        storageDirective.add(FamilyDirective.builder()
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
        log.info("Directive to storage for remove parent-child link is send");
    }
}
