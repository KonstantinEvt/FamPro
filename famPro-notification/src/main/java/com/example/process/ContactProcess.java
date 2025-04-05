package com.example.process;

import com.example.dtos.AloneNewDto;
import com.example.dtos.DirectiveGuards;
import com.example.entity.AloneNew;
import com.example.holders.StandardInfoHolder;
import com.example.mappers.AloneNewMapper;
import com.example.service.ContactService;
import com.example.service.FamilyService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@Log4j2
@AllArgsConstructor
public class ContactProcess implements Consumer<Message<DirectiveGuards>> {
    private final ContactService contactService;

    @Override
    public void accept(Message<DirectiveGuards> directiveMessage) {
        DirectiveGuards directiveGuards = directiveMessage.getPayload();
        log.info("Receiving directive by Contact_channel: {}", directiveGuards);
        switch (directiveGuards.getOperation()){
            case ADD -> {contactService.refreshContactsByGlobalUnion(directiveGuards.getGuards());}
            default -> {log.warn("Its unknown directive");}
        }
    }
}