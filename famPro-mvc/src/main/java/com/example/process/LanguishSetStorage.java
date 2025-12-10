package com.example.process;

import com.example.dtos.DirectiveGuards;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Supplier;

@Log4j2
@Component
public class LanguishSetStorage implements Supplier<Message<DirectiveGuards>> {
    private final LinkedList<DirectiveGuards> languishStorage;

    public LanguishSetStorage(@Qualifier("languishStorage") LinkedList<DirectiveGuards> languishStorage) {
        this.languishStorage = languishStorage;
    }

    @Override
    public Message<DirectiveGuards> get() {
        if (!languishStorage.isEmpty()) {
            log.info("Setup languish for {} in storage{}",languishStorage.peek().getTokenUser(), languishStorage.peek().getLocalisation());
            return MessageBuilder.withPayload(Objects.requireNonNull(languishStorage.poll())).build();
        }
        return null;
    }
}
