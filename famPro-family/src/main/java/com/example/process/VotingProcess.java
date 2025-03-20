package com.example.process;

import com.example.dtos.Directive;
import com.example.enums.KafkaOperation;
import com.example.service.DirectiveService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@Log4j2
@AllArgsConstructor
public class VotingProcess implements Consumer<Message<Directive>> {
    private DirectiveService directiveService;
    @Override
    public void accept(Message<Directive> directiveMessage) {
        Directive directive = directiveMessage.getPayload();
        log.info("Receiving directive from voting: {}",directive);
    switch (directive.getOperation()){
        case ADD ->  directiveService.setChangesFromVotingDirective(directive.getPerson());
        case REMOVE ->  directiveService.negativeVoting(directive.getPerson());
        case EDIT -> directiveService.setLinkGuardFromVotingDirective(directive.getPerson());
        default -> directiveService.rejectLinkGuard(directive.getPerson());
}}
}