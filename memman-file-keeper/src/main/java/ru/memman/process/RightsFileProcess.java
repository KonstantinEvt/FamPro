package ru.memman.process;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.memman.dtos.DirectiveGuards;
import ru.memman.holders.BarrierInputPhotoHolder;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@Component
@Log4j2
@AllArgsConstructor
public class RightsFileProcess implements Consumer<Message<List<DirectiveGuards>>> {
    private final Map<String, Set<String>> rightsMap;
    private final BarrierInputPhotoHolder barrierInputPhotoHolder;
    @Override
    public void accept(Message<List<DirectiveGuards>> directiveMessage) {
        List<DirectiveGuards> directiveRights = directiveMessage.getPayload();
        for (DirectiveGuards dr :
                directiveRights) {
        switch (dr.getOperation()) {
            case ADD -> {
                log.info("Receiving rights directive: {}", dr);
                rightsMap.merge(dr.getTokenUser(), dr.getGuards(), (x, y) -> {
                    x.addAll(y);
                    return x;
                });
            }
            case REMOVE -> {
                log.info("Removing rights directive: {}", dr);
                for (String person :
                        dr.getGuards()) {
                    rightsMap.get(dr.getTokenUser()).remove(person);
                }
            }
            case EXIT -> {
                rightsMap.remove(dr.getTokenUser());
                barrierInputPhotoHolder.logout(dr.getTokenUser());
                log.info("clear holders by logout user");

            }
            default -> {
                log.warn("Receiving unknown directive");
            }

        }
    }}
}