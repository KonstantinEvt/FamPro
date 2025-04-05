package com.example.process;

import com.example.dtos.DirectiveGuards;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Supplier;

@Component
@AllArgsConstructor
@Log4j2
public class RightsProcess implements Supplier<Message<DirectiveGuards>> {
    private final LinkedList<DirectiveGuards> directiveRights;

    @Override
    public Message<DirectiveGuards> get() {
        if (!directiveRights.isEmpty()) {
            log.info("Sending rights directive: {}",directiveRights.peek());
            return MessageBuilder.withPayload(Objects.requireNonNull(directiveRights.poll())).build();
        }
        return null;
    }
}

