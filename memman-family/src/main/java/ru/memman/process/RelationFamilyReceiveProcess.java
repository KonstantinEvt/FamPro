package ru.memman.process;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.memman.dtos.ContactDto;
import ru.memman.service.TreeService;

import java.util.List;
import java.util.function.Consumer;

@Component
@Log4j2
public class RelationFamilyReceiveProcess implements Consumer<Message<List<ContactDto>>> {
    private final TreeService treeService;


    RelationFamilyReceiveProcess(TreeService treeService) {
        this.treeService = treeService;
    }

    @Override
    public void accept(Message<List<ContactDto>> onlineUserDtoMessage) {
        List<ContactDto> contactDtos = onlineUserDtoMessage.getPayload();
        log.info("Receiving request to set relations: {}", contactDtos);
        treeService.setContactsRelations(contactDtos);
    }
}