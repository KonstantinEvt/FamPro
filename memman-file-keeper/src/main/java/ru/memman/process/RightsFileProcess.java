package ru.memman.process;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.memman.dtos.DirectiveGuards;
import ru.memman.holders.BarrierInputPhotoHolder;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@Component
@Log4j2
@AllArgsConstructor
public class RightsFileProcess implements Consumer<Message<DirectiveGuards>> {
    private final Map<String, Set<String>> rightsMap;
    private final BarrierInputPhotoHolder barrierInputPhotoHolder;
    @Override
    public void accept(Message<DirectiveGuards> directiveMessage) {
        DirectiveGuards directiveRights = directiveMessage.getPayload();
        switch (directiveRights.getOperation()) {
            case ADD -> {
                log.info("Receiving rights directive: {}", directiveRights);
                rightsMap.merge(directiveRights.getTokenUser(), directiveRights.getGuards(), (x, y) -> {
                    x.addAll(y);
                    return x;
                });
            }
            case REMOVE -> {
                log.info("Removing rights directive: {}", directiveRights);
                for (String person :
                        directiveRights.getGuards()) {
                    rightsMap.get(directiveRights.getTokenUser()).remove(person);
                }
            }
            case EXIT -> {
                rightsMap.remove(directiveRights.getTokenUser());
                barrierInputPhotoHolder.logout(directiveRights.getTokenUser());
                log.info("clear holders by logout user");

            }
            default -> {
                log.warn("Receiving unknown directive");
            }

        }
    }
}