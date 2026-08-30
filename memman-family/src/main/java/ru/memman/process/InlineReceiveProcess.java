package ru.memman.process;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.memman.dtos.OnlineUserDto;
import ru.memman.enums.UserRole;
import ru.memman.service.GuardService;

import java.util.LinkedList;
import java.util.function.Consumer;

@Component
@Log4j2
public class InlineReceiveProcess implements Consumer<Message<OnlineUserDto>> {
    private final GuardService guardService;

    InlineReceiveProcess(GuardService guardService,
                         LinkedList<OnlineUserDto> inline) {
        this.guardService = guardService;
    }

    @Override
    public void accept(Message<OnlineUserDto> directiveMessage) {
        OnlineUserDto onlineUserDto = directiveMessage.getPayload();
        log.info("Receiving number of persons in storage: {}", onlineUserDto);
        guardService.mergeGuard(onlineUserDto);
    }
}