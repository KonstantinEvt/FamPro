package com.example.process;

import com.example.dtos.AloneNewDto;
import com.example.dtos.DirectiveGuards;
import com.example.entity.AloneNew;
import com.example.holders.StandardInfoHolder;
import com.example.mappers.AloneNewMapper;
import com.example.service.FamilyService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@Log4j2
@AllArgsConstructor
public class GuardProcess implements Consumer<Message<DirectiveGuards>> {
    private final StandardInfoHolder standardInfoHolder;
    private FamilyService familyService;
    private AloneNewMapper aloneNewMapper;

    @Override
    public void accept(Message<DirectiveGuards> directiveMessage) {
        DirectiveGuards directiveGuards = directiveMessage.getPayload();
        log.info("Receiving inform directive: {}", directiveGuards);
        AloneNew aloneNew1;
        if (directiveGuards.getGuards() != null && !directiveGuards.getGuards().isEmpty()) {
            AloneNew aloneNew = familyService.formMessageToGuards(directiveGuards);
            AloneNewDto letterForGuard = aloneNewMapper.entityToDto(aloneNew);
            letterForGuard.setSendingFromAlt(aloneNew.getSendFrom().getNickName());
            for (String guard :
                    directiveGuards.getGuards()) {
                if (standardInfoHolder.getOnlineInfo().containsKey(guard)) {
                    letterForGuard.setSendingTo(guard);
                    standardInfoHolder.addNewMessageToPerson(letterForGuard);
                }
            }
            aloneNew1 = familyService.formMessageToRequester(directiveGuards);
        } else {
            aloneNew1 = familyService.formAnswerToRequester(directiveGuards);
        }
        AloneNewDto letterForRequester = aloneNewMapper.entityToDto(aloneNew1);
        letterForRequester.setSendingTo(directiveGuards.getTokenUser());
        letterForRequester.setSendingFromAlt(aloneNew1.getSendFrom().getNickName());
        standardInfoHolder.addNewMessageToPerson(letterForRequester);
    }
}