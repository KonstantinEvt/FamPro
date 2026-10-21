package ru.memman.process;

import ru.memman.dtos.Directive;
import ru.memman.enam.BucketCategory;
import ru.memman.holders.DirectiveHolder;
import ru.memman.holders.NameBucketHolder;
import ru.memman.holders.BarrierInputPhotoHolder;
import ru.memman.services.FileStorageService;
import io.minio.errors.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Component
@Log4j2
public class ReceiveFileProcess implements Consumer<Message<Directive>> {
    private final BarrierInputPhotoHolder barrierInputPhotoHolder;
    private final FileStorageService fileStorageService;
    private final DirectiveHolder directiveHolder;

private final NameBucketHolder nameBucketHolder;
    public ReceiveFileProcess(BarrierInputPhotoHolder barrierInputPhotoHolder,
                              FileStorageService fileStorageService,
                              DirectiveHolder directiveHolder,
                              NameBucketHolder nameBucketHolder) {
        this.barrierInputPhotoHolder = barrierInputPhotoHolder;
        this.fileStorageService = fileStorageService;
        this.directiveHolder = directiveHolder;
        this.nameBucketHolder = nameBucketHolder;
    }

    @Override
    public void accept(Message<Directive> directiveMessage) {
        Directive directive = directiveMessage.getPayload();
        Map<String,Map<String, byte[]>> placeHolder;
        log.info("получена фото директива: {}", directive);
        String place = directive.getSwitchPosition().name().toLowerCase();
        switch (directive.getOperation()) {
            case ADD -> {
                switch (directive.getSwitchPosition()) {
                    case PRIME -> placeHolder = barrierInputPhotoHolder.getPrimePictures();
                    case LITTLE -> placeHolder= barrierInputPhotoHolder.getSmallPrimePictures();
                    case BIRTH -> placeHolder = barrierInputPhotoHolder.getBirthPictures();
                    case BURIAL -> placeHolder = barrierInputPhotoHolder.getBurialPictures();
                    default -> {
                        placeHolder = barrierInputPhotoHolder.getFrontPictures();
                        place = directive.getTokenUser();
                    }
                }
                placeHolder.putIfAbsent(directive.getTokenUser(),new HashMap<>());
                if (placeHolder.get(directive.getTokenUser()).get(directive.getPerson()) != null) {
                    log.info("Выполняется сохранение файла на сервер MinIO...");
                    try {
                        fileStorageService.savePhoto(placeHolder.get(directive.getTokenUser()).get(directive.getPerson()),
                                directive.getPerson(), nameBucketHolder.getNameOfBucket(BucketCategory.PRIVATE, place, directive.getTokenUser()));
                        log.info("Файл успешно сохранен на сервер MinIO.");
                    } catch (Exception e) {
                        log.error("Ошибка при сохранении файла на сервер MinIO.", e);
                    }
                    placeHolder.get(directive.getTokenUser()).remove(directive.getPerson());
                } else{
                    directiveHolder.getDirectiveMap().putIfAbsent(directive.getTokenUser(), new ArrayList<>());
                    directiveHolder.getDirectiveMap().get(directive.getTokenUser()).add(directive);
            }}
            case REMOVE -> {

                try {
                    fileStorageService.deletePhoto(nameBucketHolder.getNameOfBucket(BucketCategory.PRIVATE, place, directive.getTokenUser()), directive.getPerson());
                System.out.println("remove");
                } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                         NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException |
                         XmlParserException | InternalException e) {
                    throw new RuntimeException(e);
                }

            }
            case RENAME -> {
                try {
                    fileStorageService.renamePhoto(nameBucketHolder.getNameOfBucket(BucketCategory.PRIVATE, place, directive.getTokenUser()), directive.getTokenUser(), directive.getPerson());
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