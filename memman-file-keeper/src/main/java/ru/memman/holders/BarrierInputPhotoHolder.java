package ru.memman.holders;

import ru.memman.dtos.Directive;
import ru.memman.enam.BucketCategory;
import ru.memman.services.FileStorageService;
import ru.memman.services.TokenService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
@Setter
@Slf4j
public class BarrierInputPhotoHolder {
    private Map<String, Map<String, byte[]>> frontPictures = new ConcurrentHashMap<>();
    private Map<String, Map<String, byte[]>> smallPrimePictures = new ConcurrentHashMap<>();
    private Map<String, Map<String, byte[]>> primePictures = new ConcurrentHashMap<>();
    private Map<String, Map<String, byte[]>> birthPictures = new ConcurrentHashMap<>();
    private Map<String, Map<String, byte[]>> burialPictures = new ConcurrentHashMap<>();
    private final TokenService tokenService;
    private final DirectiveHolder directiveHolder;
    private final FileStorageService fileStorageService;
    private final NameBucketHolder nameBucketHolder;

    public BarrierInputPhotoHolder(TokenService tokenService,
                                   DirectiveHolder directiveHolder,
                                   FileStorageService fileStorageService,
                                   NameBucketHolder nameBucketHolder) {
        this.tokenService = tokenService;
        this.directiveHolder = directiveHolder;
        this.fileStorageService = fileStorageService;
        this.nameBucketHolder = nameBucketHolder;
    }

    public void addFrontPicture(MultipartFile frontPicture, String place, String personUuid) {
        String frontUser = (String) tokenService.getTokenUser().getClaims().get("sub");
        Map<String, Map<String, byte[]>> placeHolder = switch (place) {
            case "prime":
                yield primePictures;
            case "little":
                yield smallPrimePictures;
            case "burial":
                yield burialPictures;
            case "birth":
                yield birthPictures;
            default:
                yield frontPictures;
        };
        try (InputStream file = frontPicture.getInputStream()) {
            placeHolder.putIfAbsent(frontUser, new HashMap<>());
            placeHolder.get(frontUser).put(personUuid, file.readAllBytes());
            log.info("Size of front picture:{}", placeHolder.get(frontUser).get(personUuid).length);
        } catch (IOException e) {
            log.error("cannot get file from front");
            throw new RuntimeException(e);
        }
        if (directiveHolder.getDirectiveMap().containsKey(frontUser)) {
            Optional<Directive> directive = directiveHolder.getDirectiveMap().get(frontUser).stream().filter(x -> Objects.equals(x.getPerson(), personUuid)).findFirst();
            directive.ifPresent(x -> {
                fileStorageService.savePhoto(placeHolder.get(frontUser).get(personUuid),
                        personUuid, nameBucketHolder.getNameOfBucket(BucketCategory.PRIVATE, place, frontUser));
                placeHolder.get(frontUser).remove(personUuid);
                log.info("Файл успешно сохранен на сервер MinIO.");
                directiveHolder.getDirectiveMap().get(frontUser).remove(directive.get());
            });
        }
    }

    public void logout(String tokenUser) {
        directiveHolder.getDirectiveMap().remove(tokenUser);
        frontPictures.remove(tokenUser);
        primePictures.remove(tokenUser);
        smallPrimePictures.remove(tokenUser);
        burialPictures.remove(tokenUser);
        birthPictures.remove(tokenUser);
    }
}

