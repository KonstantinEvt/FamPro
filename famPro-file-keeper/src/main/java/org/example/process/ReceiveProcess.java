package org.example.process;

import com.example.dtos.Directive;
import com.example.enums.KafkaOperation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.holders.DirectiveHolder;
import org.example.holders.PhotoHolder;
import org.example.services.FileStorageService;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@Log4j2
@AllArgsConstructor
public class ReceiveProcess implements Consumer<Message<Directive>> {
    private final PhotoHolder photoHolder;
    private final FileStorageService fileStorageService;
    private final DirectiveHolder directiveHolder;

    @Override
    public void accept(Message<Directive> directiveMessage) {
        Directive directive = directiveMessage.getPayload();
        System.out.println(directive);
        System.out.println("tyt");
        switch (directive.getOperation()) {
            case ADD -> {
                if (photoHolder.getFrontPictures().get(directive.getTokenUser()) != null) {
                    log.info("Выполняется сохранение файла на сервер MinIO...");
                    try {
                        fileStorageService.saveFirstPhoto(photoHolder.getFrontPictures().get(directive.getTokenUser()),
                                directive.getPerson());
                        log.info("Файл успешно сохранен на сервер MinIO.");
                    } catch (Exception e) {
                        log.error("Ошибка при сохранении файла на сервер MinIO.", e);
                    }
                    photoHolder.getFrontPictures().remove(directive.getTokenUser());
                    System.out.println("tyt_0");
                } else directiveHolder.getDirectiveMap().put(directive.getTokenUser(),directive);
            }
            case REMOVE -> {}
            case RENAME -> {}
            default -> {}
        }
    }
}