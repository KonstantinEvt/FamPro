package com.example.process;

import com.example.dtos.DirectiveGuards;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Supplier;

@Component
public class InlineProcess implements Supplier<Message<DirectiveGuards>> {
    private final LinkedList<DirectiveGuards> inline;

    public InlineProcess(@Qualifier("inlineResource") LinkedList<DirectiveGuards> inline) {
        this.inline = inline;
    }

    @Override
    public Message<DirectiveGuards> get() {
        if (!inline.isEmpty()) {
            return MessageBuilder.withPayload(Objects.requireNonNull(inline.poll())).build();
        }
        return null;
    }
}

