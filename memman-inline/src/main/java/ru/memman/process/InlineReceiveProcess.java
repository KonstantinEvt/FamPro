package ru.memman.process;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.memman.dtos.OnlineUserDto;
import ru.memman.holders.OnlineUserHolder;

import java.util.function.Consumer;

@Component
@Log4j2
public class InlineReceiveProcess implements Consumer<Message<OnlineUserDto>> {
    private final OnlineUserHolder onlineUserHolder;
    InlineReceiveProcess(OnlineUserHolder onlineUserHolder) {
        this.onlineUserHolder = onlineUserHolder;
    }
    @Override
    public void accept(Message<OnlineUserDto> directiveMessage) {
        OnlineUserDto onlineUserDto = directiveMessage.getPayload();
        log.info("Receiving inline User:{}", onlineUserDto);
        onlineUserHolder.mergeUser(onlineUserDto);
    }
}