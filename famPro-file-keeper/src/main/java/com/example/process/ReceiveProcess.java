package com.example.process;

import com.example.dtos.Directive;
import com.example.enums.SwitchPosition;
import com.example.services.FileStorageService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import com.example.holders.DirectiveHolder;
import com.example.holders.PhotoHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@Log4j2
public class ReceiveProcess implements Consumer<Message<Directive>> {
    private final PhotoHolder photoHolder;
    private final FileStorageService fileStorageService;
    private final DirectiveHolder directiveHolder;
    @Value("${minio.first_photo_bucket}")
    private String firstPhoto;

    public ReceiveProcess(PhotoHolder photoHolder, FileStorageService fileStorageService, DirectiveHolder directiveHolder) {
        this.photoHolder = photoHolder;
        this.fileStorageService = fileStorageService;
        this.directiveHolder = directiveHolder;
    }

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
                        if (directive.getSwitchPosition()== SwitchPosition.PRIME_PHOTO) {
                        fileStorageService.savePhoto(photoHolder.getFrontPictures().get(directive.getTokenUser()),
                                directive.getPerson(),firstPhoto);} else {
                            fileStorageService.savePhoto(photoHolder.getFrontPictures().get(directive.getTokenUser()),
                                    directive.getPerson(),directive.getTokenUser());
                        }
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