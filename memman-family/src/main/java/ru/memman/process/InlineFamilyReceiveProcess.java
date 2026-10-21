package ru.memman.process;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.memman.dtos.OnlineUserDto;
import ru.memman.service.GuardService;

import java.util.function.Consumer;

@Component
@Log4j2
public class InlineFamilyReceiveProcess implements Consumer<Message<OnlineUserDto>> {
    private final GuardService guardService;

    InlineFamilyReceiveProcess(GuardService guardService) {
        this.guardService = guardService;
    }

    @Override
    public void accept(Message<OnlineUserDto> directiveMessage) {
        OnlineUserDto onlineUserDto = directiveMessage.getPayload();
        if (onlineUserDto.getExternUuid()==null) {log.info("Reload personsCount");
            guardService.reloadInline(onlineUserDto);}
        else {
        log.info("User is online: {}", onlineUserDto);
        guardService.mergeGuard(onlineUserDto);
    }}
}