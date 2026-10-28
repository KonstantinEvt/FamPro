package ru.memman.process;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.memman.dtos.OnlineUserDto;
import ru.memman.holders.OnlineUserHolder;
import ru.memman.service.OnlineUserService;

import java.util.Objects;
import java.util.function.Consumer;

@Component
@Log4j2
public class InlineReceiveProcess implements Consumer<Message<OnlineUserDto>> {
    private final OnlineUserHolder onlineUserHolder;
    private final OnlineUserService onlineUserService;

    InlineReceiveProcess(OnlineUserHolder onlineUserHolder, OnlineUserService onlineUserService) {
        this.onlineUserHolder = onlineUserHolder;
        this.onlineUserService = onlineUserService;
    }

    @Override
    public void accept(Message<OnlineUserDto> onlineUserDtoMessage) {
        OnlineUserDto onlineUserDto = onlineUserDtoMessage.getPayload();
        String externUuid = onlineUserDto.getExternUuid();
        if (externUuid == null) {
            if (onlineUserDto.getPersonsInBase() == 0L) {
                log.info("Reload inline");
                onlineUserService.reloadInline();
            } else onlineUserHolder.setInBasePersonsCount(onlineUserDto.getPersonsInBase());
        } else {
            if (Objects.equals(externUuid, "system") || Objects.equals(externUuid, "common"))
                onlineUserHolder.addGlobalMessageCount(externUuid);
            else {
                onlineUserService.mergeUser(onlineUserDto);
                log.info("Receiving inline User:{}", onlineUserDto);
            }
        }
    }
}