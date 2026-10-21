package ru.memman.holders;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.memman.enam.BucketCategory;

@Component

public class NameBucketHolder {
    @Value("${minio.buckets.first_photo}")
    private String primePhoto;
    @Value("${minio.buckets.contact_photo}")
    private String contactPhoto;
    @Value("${minio.buckets.system_news}")
    private String sysNews;
    @Value("${minio.buckets.common_news}")
    private String commonNews;
    @Value("${minio.buckets.default_photo}")
    private String defaultPhoto;
    @Value("${minio.buckets.photo_burial}")
    private String burial;
    @Value("${minio.buckets.photo_birth}")
    private String birth;
    @Value("${minio.buckets.little_prime}")
    private String littlePrime;
    public String getNameOfBucket(@NotNull BucketCategory bucketCategory, String context, String personId) {
        return switch (bucketCategory) {
            case STANDARD: {
                switch (context) {
                    case "system" -> {
                        yield sysNews;
                    }
                    case "common" -> {
                        yield commonNews;
                    }
                    default -> {
                        yield defaultPhoto;
                    }
                }
            }
            case PRIVATE: {
                switch (context) {
                    case "prime" -> {
                        yield primePhoto;
                    }
                    case "little" -> {
                        yield littlePrime;
                    }
                    case "burial" -> {
                        yield burial;
                    }
                    case "birth" -> {
                        yield birth;
                    }
                    default -> {
                        yield personId;
                    }
                }
            }
            case CONTACT: {
                if (context.equals("contact")) {
                    yield contactPhoto.concat(personId);
                }
                yield "unknown";
            }
            default:
                yield "unknown";
        };
    }
}
