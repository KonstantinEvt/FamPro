package ru.memman.process;

import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.memman.dtos.ContactDto;
import ru.memman.service.ContactService;

import java.util.List;
import java.util.function.Consumer;

@Component
@Log4j2
public class RelationContactReceiveProcess implements Consumer<Message<List<ContactDto>>> {
    private final ContactService contactService;
    RelationContactReceiveProcess(ContactService contactService) {
        this.contactService = contactService;
    }
    @Override
    public void accept(Message<List<ContactDto>> contactDtoListDtoMessage) {
        List<ContactDto> contactDtoList = contactDtoListDtoMessage.getPayload();
        log.info("Receiving response to set relation: {}", contactDtoList);
        contactService.setContactsRelations(contactDtoList);
    }
}