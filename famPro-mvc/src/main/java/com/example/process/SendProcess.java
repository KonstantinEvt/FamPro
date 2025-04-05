//package com.example.process;
//
//import com.example.dtos.Directive;
//import lombok.AllArgsConstructor;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.stereotype.Component;
//
//import java.util.LinkedList;
//import java.util.Objects;
//import java.util.function.Supplier;
//
//@Component
//@AllArgsConstructor
//public class SendProcess implements Supplier<Message<Directive>> {
//    private final LinkedList<Directive> directives;
//
//    @Override
//    public Message<Directive> get() {
//        if (!directives.isEmpty()) {
//            System.out.println("tyt0");
//            System.out.println(directives.peek());
//            return MessageBuilder.withPayload(Objects.requireNonNull(directives.poll())).build();
//        }
//        System.out.println("tyt1");
//        return null;
//    }
//}

