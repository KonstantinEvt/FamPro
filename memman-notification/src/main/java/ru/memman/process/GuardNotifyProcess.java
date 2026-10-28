package ru.memman.process;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.memman.dtos.DirectiveGuards;
import ru.memman.service.ReceiveAndFormService;

import java.util.function.Consumer;

@Component
@Log4j2
@AllArgsConstructor
public class GuardNotifyProcess implements Consumer<Message<DirectiveGuards>> {
    private final ReceiveAndFormService receiveAndFormService;

    @Override
    public void accept(Message<DirectiveGuards> directiveMessage) {
        DirectiveGuards directiveGuards = directiveMessage.getPayload();
        log.info("Receiving inform/voting directive: {}", directiveGuards);
        switch (directiveGuards.getOperation()) {
            case ADD -> receiveAndFormService.receiveAttentionLetter(directiveGuards);
            case EDIT -> receiveAndFormService.receiveVotingLetter(directiveGuards);
            default -> log.warn("Unknown directive");
        }
    }
}