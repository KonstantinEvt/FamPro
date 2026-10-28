package ru.memman.process;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import ru.memman.dtos.DirectiveGuards;
import ru.memman.service.FamilyMemberService;

import java.util.function.Consumer;

@Component
@Log4j2
public class StorageReceiverGuards implements Consumer<Message<DirectiveGuards>> {
    private final FamilyMemberService familyMemberService;

    StorageReceiverGuards(FamilyMemberService familyMemberService) {
        this.familyMemberService = familyMemberService;
    }

    @Override
    public void accept(Message<DirectiveGuards> directiveMessage) {
        DirectiveGuards directive = directiveMessage.getPayload();
        log.info("Receiving directive to change member Parents/Status: {}", directive);
        switch (directive.getOperation()) {
            case EDIT -> familyMemberService.changeParentsAfterVoting(directive);
            case RENAME -> familyMemberService.changeCheckStatus(directive);
            default -> log.warn("received unknown directive");
        }
    }
}