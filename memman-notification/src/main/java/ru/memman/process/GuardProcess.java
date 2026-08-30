package ru.memman.process;

import ru.memman.dtos.AloneNewDto;
import ru.memman.dtos.DirectiveGuards;
import ru.memman.enums.KafkaOperation;
import ru.memman.enums.SwitchPosition;
import ru.memman.holders.StandardInfoHolder;
import ru.memman.service.ReceiveAndFormService;
import ru.memman.service.RecipientService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Consumer;

@Component
@Log4j2
@AllArgsConstructor
public class GuardProcess implements Consumer<Message<DirectiveGuards>> {
    private final ReceiveAndFormService receiveAndFormService;
    private final StandardInfoHolder standardInfoHolder;
    private final RecipientService recipientService;

    @Override
    public void accept(Message<DirectiveGuards> directiveMessage) {
        DirectiveGuards directiveGuards = directiveMessage.getPayload();
        log.info("Receiving inform directive: {}", directiveGuards);
        switch (directiveGuards.getOperation()) {
            case ADD -> {
                receiveAndFormService.receiveAttentionLetter(directiveGuards);

            }
            case EDIT -> {
                receiveAndFormService.receiveVotingLetter(directiveGuards);

            }
            case GET -> {
                if (!standardInfoHolder.getOnlineInfo().containsKey(directiveGuards.getTokenUser()) ||
                        !Objects.equals(directiveGuards.getLocalisation(), standardInfoHolder.getOnlineInfo().get(directiveGuards.getTokenUser()).getLocalisation())) {
                    recipientService.inlineProcess(directiveGuards);
                    log.info("User {} is entering in system", directiveGuards.getTokenUser());
                }
                log.info("User {} is online", directiveGuards.getTokenUser());
            }
            default -> log.warn("Unknown directive");
        }


    }
}