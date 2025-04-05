package com.example.process;

import com.example.dtos.Directive;
import com.example.dtos.FamilyDirective;
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
public class CloakProcess implements Supplier<Message<Directive>> {
    private final LinkedList<Directive> cloakDirective;

    @Override
    public Message<Directive> get() {
        if (!cloakDirective.isEmpty()) {
            log.info("Sending directive to change member Status in Cloak: {}",cloakDirective.peek());
            return MessageBuilder.withPayload(Objects.requireNonNull(cloakDirective.poll())).build();
        }
        return null;
    }
}

