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
public class StorageProcess implements Supplier<Message<FamilyDirective>> {
    private final LinkedList<FamilyDirective> storageDirective;

    @Override
    public Message<FamilyDirective> get() {
        if (!storageDirective.isEmpty()) {
            log.info("Sending directive to change member in Storage: {}",storageDirective.peek());
            return MessageBuilder.withPayload(Objects.requireNonNull(storageDirective.poll())).build();
        }
        return null;
    }
}

