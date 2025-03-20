package com.example.process;

import com.example.dtos.FamilyDirective;
import com.example.enums.KafkaOperation;
import com.example.service.FamilyMemberService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@Log4j2
@AllArgsConstructor
public class ReceiveProcess implements Consumer<Message<FamilyDirective>> {
    private FamilyMemberService familyMemberService;

    @Override
    public void accept(Message<FamilyDirective> directiveMessage) {
        FamilyDirective directive = directiveMessage.getPayload();
        log.info("Receiving directive to change member Parents/Status: {}",directive);
        if (directive.getOperation() == KafkaOperation.EDIT && directive.getFamilyMemberDto() == null)
            familyMemberService.changeParentsAfterVoting(directive);
        if (directive.getOperation() == KafkaOperation.RENAME && directive.getFamilyMemberDto() == null)
            familyMemberService.changeCheckStatus(directive);
    }
}