package com.example.process;

import com.example.dtos.Directive;
import com.example.enums.SwitchPosition;
import com.example.holders.DirectiveHolder;
import com.example.holders.PhotoHolder;
import com.example.services.FileStorageServiceImpl;
import io.minio.errors.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.function.Consumer;

@Component
@Log4j2
public class ReceiveProcess implements Consumer<Message<Directive>> {
    private final PhotoHolder photoHolder;
    private final FileStorageServiceImpl fileStorageService;
    private final DirectiveHolder directiveHolder;
    @Value("${minio.first_photo_bucket}")
    private String firstPhoto;
    @Value("${minio.photo_burial_bucket}")
    private String burial;
    @Value("${minio.photo_birth_bucket}")
    private String birth;

    public ReceiveProcess(PhotoHolder photoHolder, FileStorageServiceImpl fileStorageService, DirectiveHolder directiveHolder) {
        this.photoHolder = photoHolder;
        this.fileStorageService = fileStorageService;
        this.directiveHolder = directiveHolder;
    }

    @Override
    public void accept(Message<Directive> directiveMessage) {
        Directive directive = directiveMessage.getPayload();
        System.out.println(directive);
        System.out.println("tyt");
        Map<String, byte[]> placeHolder;
        String bucket = directive.getSwitchPosition().name().toLowerCase();

        switch (directive.getOperation()) {
            case ADD -> {
                switch (directive.getSwitchPosition()) {
                    case PRIME -> placeHolder = photoHolder.getPrimePictures();
                    case BIRTH -> placeHolder = photoHolder.getBirthPictures();
                    case BURIAL -> placeHolder = photoHolder.getBurialPictures();
                    default -> {
                        placeHolder = photoHolder.getFrontPictures();
                        bucket = directive.getTokenUser();
                    }
                }
                if (placeHolder.get(directive.getTokenUser()) != null) {
                    log.info("Выполняется сохранение файла на сервер MinIO...");
                    try {
                        fileStorageService.savePhoto(placeHolder.get(directive.getTokenUser()),
                                directive.getPerson(), bucket);
                        log.info("Файл успешно сохранен на сервер MinIO.");
                    } catch (Exception e) {
                        log.error("Ошибка при сохранении файла на сервер MinIO.", e);
                    }
                    placeHolder.remove(directive.getTokenUser());
                    System.out.println("tyt_0");
                } else
                    directiveHolder.getDirectiveMap().put(directive.getTokenUser().concat(directive.getSwitchPosition().name().toLowerCase()), directive);
            }
            case REMOVE -> {

                try {
                    fileStorageService.deletePhoto(bucket, directive.getPerson());
                System.out.println("remove");
                } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                         NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException |
                         XmlParserException | InternalException e) {
                    throw new RuntimeException(e);
                }

            }
            case RENAME -> {
                try {
                    fileStorageService.renamePhoto(bucket, directive.getTokenUser(), directive.getPerson());
                System.out.println("rename");
                } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                         NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException |
                         XmlParserException | InternalException e) {
                    throw new RuntimeException(e);
                }
            }
            default -> {
            }
        }
    }
}