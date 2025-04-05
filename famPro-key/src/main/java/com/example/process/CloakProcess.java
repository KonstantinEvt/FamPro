package com.example.process;

import com.example.dtos.Directive;
import com.example.dtos.TokenUser;
import com.example.enums.KafkaOperation;
import com.example.enums.SwitchPosition;
import com.example.enums.UserRoles;
import com.example.services.KeyCloakService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;

@Component
@Log4j2
@AllArgsConstructor
public class CloakProcess implements Function<Message<Directive>,Message<Directive>> {
    private KeyCloakService keyCloakService;

    @Override
    public Message<Directive> apply(Message<Directive> directiveMessage) {
        Directive directive = directiveMessage.getPayload();
        if (directive.getOperation() == KafkaOperation.EDIT) {

            keyCloakService.editUser(TokenUser.builder()
                    .username(directive.getTokenUser())
                    .roles(Set.of(UserRoles.LINKED_USER.getNameSSO()))
                    .build());
        }
    return directiveMessage;
    }
}