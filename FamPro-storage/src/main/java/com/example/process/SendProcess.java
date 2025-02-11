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
public class SendProcess implements Supplier<Message<FamilyDirective>> {
    private final LinkedList<FamilyDirective> directives;

    @Override
    public Message<FamilyDirective> get() {
        if (!directives.isEmpty()) {
            System.out.println("tyt0");
            System.out.println(directives.peek());
            return MessageBuilder.withPayload(Objects.requireNonNull(directives.poll())).build();
        }
        System.out.println("tyt1");
        return null;
    }
}

