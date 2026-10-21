package ru.memman.process;

import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.memman.dtos.OnlineUserDto;
import ru.memman.service.RealManService;

import java.util.function.Consumer;

@Component
@Log4j2
public class InlineContactReceiveProcess implements Consumer<Message<OnlineUserDto>> {
    private final RealManService realManService;
    InlineContactReceiveProcess(RealManService realManService) {
        this.realManService = realManService;
    }

//    @KafkaListener(topics = "contact_inline_channel")
    @Override
    public void accept(Message<OnlineUserDto> onlineUserDtoMessage) {
        OnlineUserDto onlineUserDto = onlineUserDtoMessage.getPayload();
        log.info("Receiving inline User: {}", onlineUserDto);
        realManService.mergeRealMen(onlineUserDto);
    }
}