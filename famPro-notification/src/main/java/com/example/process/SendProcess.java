package com.example.process;

import com.example.dtos.Directive;
import lombok.AllArgsConstructor;
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
public class SendProcess implements Supplier<Message<Directive>> {
    private final LinkedList<Directive> directives;

    public SendProcess(@Qualifier("directiveResource") LinkedList<Directive> directives) {
        this.directives = directives;
    }

    @Override
    public Message<Directive> get() {
        if (!directives.isEmpty()) {
            log.info("Sending agreement directive to photoModule: {}", directives.peek());
            return MessageBuilder.withPayload(Objects.requireNonNull(directives.poll())).build();
        }
        return null;
    }
}

