package ru.memman.process;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.memman.dtos.OnlineUserDto;
import ru.memman.service.RecipientService;

import java.util.LinkedList;
import java.util.function.Consumer;

@Component
@Log4j2
public class InlineReceiveProcess implements Consumer<Message<OnlineUserDto>> {
    private final RecipientService recipientService;


    InlineReceiveProcess(RecipientService recipientService) {
        this.recipientService = recipientService;
    }

    @Override
    public void accept(Message<OnlineUserDto> directiveMessage) {
        OnlineUserDto onlineUserDto = directiveMessage.getPayload();
        log.info("Receiving inline User: {}", onlineUserDto);
        recipientService.mergeRecipient(onlineUserDto);
    }
}