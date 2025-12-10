package com.example.process;

import com.example.dtos.Directive;
import com.example.dtos.DirectiveGuards;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Supplier;

@Component
@Log4j2
public class VotingProcess implements Supplier<Message<DirectiveGuards>> {

    private final LinkedList<DirectiveGuards> directives;

    public VotingProcess(@Qualifier("directiveVoting") LinkedList<DirectiveGuards> directives) {
        this.directives = directives;
    }

    @Override
    public Message<DirectiveGuards> get() {
        if (!directives.isEmpty()) {
            log.info("Sending voting directive: {}", directives.peek());
            return MessageBuilder.withPayload(Objects.requireNonNull(directives.poll())).build();
        }
        return null;
    }
}

