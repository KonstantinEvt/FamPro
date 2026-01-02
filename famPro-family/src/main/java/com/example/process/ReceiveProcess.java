package com.example.process;

import com.example.dtos.FamilyDirective;
import com.example.service.DirectiveService;
import com.example.service.FacadeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.function.Consumer;

@Component
@Log4j2
@AllArgsConstructor
public class ReceiveProcess implements Consumer<Message<LinkedList<FamilyDirective>>> {

    private final FacadeService facadeService;
    private final DirectiveService directiveService;

    @Override
    public void accept(Message<LinkedList<FamilyDirective>> directiveMessage) {
        LinkedList<FamilyDirective> directive = directiveMessage.getPayload();
        log.info("Receiving directive from storage: {}", directive);
        if (!directive.isEmpty()) directiveService.setLanguish(directive.peek());
        facadeService.checkFamilyDirectives(directive);


    }

}