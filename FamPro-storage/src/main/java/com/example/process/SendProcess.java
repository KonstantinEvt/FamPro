package com.example.process;

import com.example.dtos.Directive;
import com.example.dtos.FamilyDirective;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Component
@AllArgsConstructor
@Log4j2
public class SendProcess implements Supplier<Message<LinkedList<FamilyDirective>>> {
    private final LinkedList<LinkedList<FamilyDirective>> directives;

    @Override
    public Message<LinkedList<FamilyDirective>> get() {
        if (!directives.isEmpty()) {
            log.info("Sending directive to family for add/change person: {}",directives.peek());
            return MessageBuilder.withPayload(Objects.requireNonNull(directives.poll())).build();
        }
        return null;
    }
}

