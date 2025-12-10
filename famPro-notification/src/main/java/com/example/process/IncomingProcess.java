//package com.example.process;
//
//import com.example.dtos.FamilyDirective;
//import com.example.enums.KafkaOperation;
//import com.example.holders.StandardInfoHolder;
//import com.example.service.RecipientService;
//import lombok.AllArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.messaging.Message;
//import org.springframework.stereotype.Component;
//
//import java.util.function.Consumer;

//@Component
//@Log4j2
//@AllArgsConstructor
//public class IncomingProcess implements Consumer<Message<FamilyDirective>> {
//    private final StandardInfoHolder standardInfoHolder;
//    private RecipientService recipientService;
//
//    @Override
//    public void accept(Message<FamilyDirective> directiveMessage) {
//        FamilyDirective directive = directiveMessage.getPayload();
//        String inlineUser = directive.getTokenUser();
//        log.info("User {} is entering in system", inlineUser);
//        if (directive.getOperation() == KafkaOperation.ADD) {
//            if (!standardInfoHolder.getOnlineInfo().containsKey(inlineUser))
//                recipientService.inlineProcess(directive);
//        }
//    }
//        directiveHolder.getDirectiveMap().putIfAbsent(inlineUser, new LinkedList<>());
//        if (!directiveHolder.getDirectiveMap().get(inlineUser).contains(directive)) {
//            System.out.println("Директива обрабатывается");
//            System.out.println(directive);
//            directiveHolder.getDirectiveMap().get(inlineUser).add(directive);
//            if (directive.getSwitchPosition() == SwitchPosition.MAIN) {
//                incomingService.checkFamilyDirectives(directiveHolder.getDirectiveMap().get(inlineUser));
//                directiveHolder.getDirectiveMap().remove(inlineUser);
//            }
//        }
//}