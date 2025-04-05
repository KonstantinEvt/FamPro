package com.example.process;

import com.example.dtos.DirectiveGuards;
import com.example.enums.KafkaOperation;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

@Component
@Log4j2
@AllArgsConstructor
public class RightsProcess implements Consumer<Message<DirectiveGuards>> {
    private final Map<String, Set<String>> rightsMap;

    @Override
    public void accept(Message<DirectiveGuards> directiveMessage) {
        DirectiveGuards directiveRights = directiveMessage.getPayload();
        log.info("Receiving rights directive: {}", directiveRights);
        if (Objects.requireNonNull(directiveRights.getOperation()) == KafkaOperation.ADD) {
            rightsMap.put(directiveRights.getPerson(), directiveRights.getGuards());
        } else {
            log.warn("unknown directive");
        }


    }
}