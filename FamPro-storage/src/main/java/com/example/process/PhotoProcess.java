package com.example.process;

import com.example.dtos.Directive;
import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Supplier;

@Component
@AllArgsConstructor
public class PhotoProcess implements Supplier<Message<Directive>> {
    private final LinkedList<Directive> directivePhotos;

    @Override
    public Message<Directive> get() {
        if (!directivePhotos.isEmpty()) {
            System.out.println(directivePhotos.peek());
            return MessageBuilder.withPayload(Objects.requireNonNull(directivePhotos.poll())).build();
        }
        return null;
    }
}

