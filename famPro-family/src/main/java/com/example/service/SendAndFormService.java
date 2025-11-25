package com.example.service;

import com.example.dtos.Directive;
import com.example.dtos.DirectiveGuards;
import com.example.enums.CheckStatus;
import com.example.enums.KafkaOperation;
import com.example.enums.SwitchPosition;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

@Service
@Log4j2
public class SendAndFormService {
    LinkedList<DirectiveGuards> checkLevelDirective;
    LinkedList<DirectiveGuards> guardsLetters;
    SendAndFormService(@Qualifier("checkLevelDirective") LinkedList<DirectiveGuards> checkLevelDirective,
                       @Qualifier("directiveGuards")LinkedList<DirectiveGuards> guardsLetters) {
        this.checkLevelDirective = checkLevelDirective;
        this.guardsLetters=guardsLetters;
    }

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
     * @param userUuid - отправитель-получатель
     * @param personInfo - тот, кто тронут, но был под модерацией
     * @param position - место влияния
     */
    public void sendAttentionOfModerate(String userUuid,String personInfo, SwitchPosition position){
    guardsLetters.add(DirectiveGuards.builder()
            .created(new Timestamp(System.currentTimeMillis()))
            .tokenUser(userUuid)
            .switchPosition(position)
            .info1("trying changing person under voting or moderation")
            .info2(personInfo)
            .build());
}
}
