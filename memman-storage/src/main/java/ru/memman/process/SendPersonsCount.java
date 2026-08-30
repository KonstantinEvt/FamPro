package ru.memman.process;

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
public class SendPersonsCount implements Supplier<Message<Long>> {
    private final LinkedList<Long> personsCount;

    @Override
    public Message<Long> get() {
        if (!personsCount.isEmpty()) {
            log.info("Sending count of person: {}", personsCount.peek());
            return MessageBuilder.withPayload(Objects.requireNonNull(personsCount.poll())).build();
        }
        return null;
    }
}

