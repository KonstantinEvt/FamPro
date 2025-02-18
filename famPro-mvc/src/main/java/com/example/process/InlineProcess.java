package com.example.process;

import com.example.dtos.Directive;
import com.example.dtos.FamilyDirective;
import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Supplier;

@Component
@AllArgsConstructor
public class InlineProcess implements Supplier<Message<FamilyDirective>> {
    private final LinkedList<FamilyDirective> inline;

    @Override
    public Message<FamilyDirective> get() {
        if (!inline.isEmpty()) {
            System.out.println("tyt3");
            System.out.println(inline.peek());
            return MessageBuilder.withPayload(Objects.requireNonNull(inline.poll())).build();
        }
        System.out.println("tyt2");
        return null;
    }
}

