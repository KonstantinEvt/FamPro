package ru.memman.process;

import ru.memman.dtos.Directive;
import ru.memman.dtos.TokenUser;
import ru.memman.enums.KafkaOperation;
import ru.memman.enums.UserRole;
import ru.memman.services.KeyCloakService;
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
                    .roles(Set.of(UserRole.LINKED_USER.getNameSSO()))
                    .build());
        }
    return directiveMessage;
    }
}