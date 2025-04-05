package com.example.process;

import com.example.dtos.Directive;
import com.example.enums.KafkaOperation;
import com.example.enums.UserRoles;
import com.example.models.OnlineUserHolder;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@AllArgsConstructor
@Log4j2
public class ReceiveProcess implements Consumer<Message<Directive>> {
    private final OnlineUserHolder onlineUserHolder;

    @Override
    public void accept(Message<Directive> directiveMessage) {
        Directive directive = directiveMessage.getPayload();
        if (directive.getOperation() == KafkaOperation.EDIT) {
        log.info("User {} is change ROLE in system", directive.getTokenUser());
        onlineUserHolder.changeUserRole(directive.getPerson(), UserRoles.LINKED_USER);
        }
    }
}

