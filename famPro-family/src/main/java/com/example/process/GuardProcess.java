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
public class GuardProcess implements Supplier<Message<DirectiveGuards>> {
    private final LinkedList<DirectiveGuards> directiveGuards;

    @Override
    public Message<DirectiveGuards> get() {
        if (!directiveGuards.isEmpty()) {
            log.info("Sending guard directive: {}",directiveGuards.peek());
            return MessageBuilder.withPayload(Objects.requireNonNull(directiveGuards.poll())).build();
        }
        return null;
    }
}

