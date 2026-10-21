package ru.memman.process;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.memman.dtos.ContactDto;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Component
@Log4j2
public class RelationFamilyProcess implements Supplier<Message<List<ContactDto>>> {
    private final LinkedList<List<ContactDto>> checkRelation;

    public RelationFamilyProcess(@Qualifier("checkRelation") LinkedList<List<ContactDto>> checkRelation) {
        this.checkRelation = checkRelation;
    }

    @Override
    public Message<List<ContactDto>> get() {
        if (!checkRelation.isEmpty()) {
            log.info("Response to set relation: {}", checkRelation.peek());
            return MessageBuilder.withPayload(Objects.requireNonNull(checkRelation.poll())).build();
        }
        return null;
    }
}

