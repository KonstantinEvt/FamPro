package ru.memman.process;

import ru.memman.dtos.DirectiveGuards;
import ru.memman.holders.StandardInfoHolder;
import ru.memman.service.ReceiveAndFormService;
import ru.memman.service.RecipientService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@Log4j2
@AllArgsConstructor
public class GuardNotifyProcess implements Consumer<Message<DirectiveGuards>> {
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
            default -> log.warn("Unknown directive");
        }


    }
}