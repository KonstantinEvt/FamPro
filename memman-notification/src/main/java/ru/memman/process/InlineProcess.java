package ru.memman.process;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.memman.dtos.OnlineUserDto;

import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Supplier;

@Component
public class InlineProcess implements Supplier<Message<OnlineUserDto>> {
    private final LinkedList<OnlineUserDto> inline;

    public InlineProcess(@Qualifier("inlineResource") LinkedList<OnlineUserDto> inline) {
        this.inline = inline;
    }

    @Override
    public Message<OnlineUserDto> get() {
        if (!inline.isEmpty()) {
            return MessageBuilder.withPayload(Objects.requireNonNull(inline.poll())).build();
        }
        return null;
    }
}

