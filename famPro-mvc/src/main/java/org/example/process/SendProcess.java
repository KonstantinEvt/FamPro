package org.example.process;

import com.example.dtos.TokenUser;
import lombok.AllArgsConstructor;
import org.example.models.OnlineUserHolder;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Role;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Supplier;

@Component
@AllArgsConstructor
public class SendProcess implements Supplier<Message<TokenUser>> {
    private final LinkedList<TokenUser> tokenUserResource;

    @Override
    public Message<TokenUser> get() {
        if (!tokenUserResource.isEmpty()) {
            System.out.println("tyt0");
            System.out.println(tokenUserResource.peek());
            return MessageBuilder.withPayload(Objects.requireNonNull(tokenUserResource.poll())).build();
        }
        System.out.println("tyt1");
        return null;
    }
}

