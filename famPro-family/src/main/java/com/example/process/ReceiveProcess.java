package com.example.process;

import com.example.dtos.FamilyDirective;
import com.example.enums.SwitchPosition;
import com.example.holders.DirectiveHolder;
import com.example.service.IncomingService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.function.Consumer;

@Component
@Log4j2
@AllArgsConstructor
public class ReceiveProcess implements Consumer<Message<FamilyDirective>> {
    private final DirectiveHolder directiveHolder;
    private IncomingService incomingService;

    @Override
    public void accept(Message<FamilyDirective> directiveMessage) {
        FamilyDirective directive = directiveMessage.getPayload();
        log.info("Receiving directive from storage: {}",directive);
        String keyOperation = directive.getTokenUser().concat(directive.getPerson());

        directiveHolder.getDirectiveMap().putIfAbsent(keyOperation, new LinkedList<>());
        if (!directiveHolder.getDirectiveMap().get(keyOperation).contains(directive)) {
            directiveHolder.getDirectiveMap().get(keyOperation).add(directive);
            if (directive.getSwitchPosition() == SwitchPosition.MAIN) {
                incomingService.checkFamilyDirectives(directiveHolder.getDirectiveMap().get(keyOperation));
                directiveHolder.getDirectiveMap().remove(keyOperation);
            }
        }
    }
}