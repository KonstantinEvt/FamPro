package com.example.process;

import com.example.dtos.DirectiveGuards;
import com.example.service.DirectiveService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@Log4j2
@AllArgsConstructor
public class VotingProcess implements Consumer<Message<DirectiveGuards>> {
        private DirectiveService directiveService;
    @Override
    public void accept(Message<DirectiveGuards> directiveMessage) {
        DirectiveGuards directive = directiveMessage.getPayload();
        log.info("Receiving directive from voting: {}",directive);
    switch (directive.getOperation()){
        case ADD ->  directiveService.setChangesFromVotingDirective(directive.getPerson());
        case REMOVE ->  directiveService.negativeVoting(directive.getPerson());
        case EDIT -> directiveService.setLinkGuardFromVotingDirective(directive.getPerson());
        case GET -> directiveService.setLanguish(directive);
        default -> directiveService.rejectLinkGuard(directive.getPerson());
}}
}