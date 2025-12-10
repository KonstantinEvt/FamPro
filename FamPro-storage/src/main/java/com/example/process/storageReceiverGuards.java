package com.example.process;

import com.example.dtos.DirectiveGuards;
import com.example.enums.Localisation;
import com.example.service.FamilyMemberService;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Component
@Log4j2
public class StorageReceiverGuards implements Consumer<Message<DirectiveGuards>> {
    private final FamilyMemberService familyMemberService;
    private final Map<UUID, Localisation> tempLocalisation;

    StorageReceiverGuards(FamilyMemberService familyMemberService, Map<UUID, Localisation> tempLocalisation) {
        this.familyMemberService = familyMemberService;
        this.tempLocalisation = tempLocalisation;
    }

    @Override
    public void accept(Message<DirectiveGuards> directiveMessage) {
        DirectiveGuards directive = directiveMessage.getPayload();
        log.info("Receiving directive to change member Parents/Status: {}", directive);
        switch (directive.getOperation()) {
            case EDIT -> familyMemberService.changeParentsAfterVoting(directive);
            case RENAME -> familyMemberService.changeCheckStatus(directive);
            default -> tempLocalisation.put(UUID.fromString(directive.getTokenUser()), directive.getLocalisation());
        }
    }
}