package com.example.process;

import com.example.dtos.DirectiveGuards;
import com.example.service.TransctiptGuardDirective;
import lombok.extern.log4j.Log4j2;
//import org.springframework.messaging.Message;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Objects;
import java.util.function.Function;

@Component
@Log4j2
public class TranslateToNotification implements Function<Message<DirectiveGuards>,Message<DirectiveGuards>> {
private final TransctiptGuardDirective transctiptGuardDirective;

    public TranslateToNotification(TransctiptGuardDirective transctiptGuardDirective) {
        this.transctiptGuardDirective = transctiptGuardDirective;
    }

    @Override
    public Message<DirectiveGuards> apply(Message<DirectiveGuards> directiveGuardsMessage) {
        DirectiveGuards directiveGuards = directiveGuardsMessage.getPayload();
        log.info("Receiving inform directive: {}", directiveGuards);
        DirectiveGuards directive=switch (directiveGuards.getOperation()) {
            case EDIT -> {
                try {
                    yield transctiptGuardDirective.transcriptVoting(directiveGuards);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            case ADD -> {
                try {
                    yield transctiptGuardDirective.transcriptAttention(directiveGuards);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            default -> directiveGuards;
        };

        return MessageBuilder.withPayload(Objects.requireNonNull(directive)).build();
    }
}


