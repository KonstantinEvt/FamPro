package com.example.process;

import com.example.dtos.DirectiveGuards;
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
public class CheckLevelProcess implements Supplier<Message<DirectiveGuards>> {
    private final LinkedList<DirectiveGuards> checkLevelDirective;

    @Override
    public Message<DirectiveGuards> get() {
        if (!checkLevelDirective.isEmpty()) {
            log.info("Sending directive to change member Status in Storage: {}",checkLevelDirective.peek());
            return MessageBuilder.withPayload(Objects.requireNonNull(checkLevelDirective.poll())).build();
        }
        return null;
    }
}

