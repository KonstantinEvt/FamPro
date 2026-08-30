package ru.memman.process;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.memman.holders.OnlineUserHolder;

import java.util.function.Consumer;

@Component
@Log4j2
public class ReceivePersonsCountProcess implements Consumer<Message<Long>> {
    private final OnlineUserHolder onlineUserHolder;

    ReceivePersonsCountProcess(OnlineUserHolder onlineUserHolder) {
        this.onlineUserHolder = onlineUserHolder;
    }
    @Override
    public void accept(Message<Long> directiveMessage) {
        Long personsCount = directiveMessage.getPayload();
        log.info("Receiving number of persons in storage: {}", personsCount);
        onlineUserHolder.setInBasePersonsCount(personsCount);
    }
}