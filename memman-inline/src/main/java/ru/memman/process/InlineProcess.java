package ru.memman.process;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.memman.dtos.OnlineUserDto;

import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Supplier;

@Component
@Log4j2
public class InlineProcess implements Supplier<Message<OnlineUserDto>> {
    private final LinkedList<OnlineUserDto> inline;

    public InlineProcess(@Qualifier("inlineResource") LinkedList<OnlineUserDto> inline) {
        this.inline = inline;
    }

    @Override
    public Message<OnlineUserDto> get() {
        if (!inline.isEmpty()) {
       log.info("Inline process: {}", inline.peek());
            return MessageBuilder.withPayload(Objects.requireNonNull(inline.poll())).build();
        }
        return null;
    }
}

