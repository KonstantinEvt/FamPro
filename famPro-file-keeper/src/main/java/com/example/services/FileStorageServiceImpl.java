package com.example.services;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import com.example.holders.PhotoHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Сервис для сохранения файлов в хранилище MinIO и для получения файлов из хранилища MinIO.
 * Реализует интерфейс FileStorageService.
 */
@Log4j2
@Service
public class FileStorageServiceImpl implements FileStorageService {

    private MinioClient minioClient;
    private TokenService tokenService;

    @Value("${minio.first_photo_bucket}")
    private String firstPhoto;
    @Value("${minio.photo_events_bucket}")
    private String events;
    @Value("${minio.system_news_bucket}")
    private String sysNews;
    @Value("${minio.common_news_bucket}")
    private String commonNews;
    private Map<String, byte[]> systemPictures;
    private Map<String,byte[]> commonPictures;

    public FileStorageServiceImpl(MinioClient minioClient, TokenService tokenService, Map<String, byte[]> systemPictures, Map<String, byte[]> commonPictures) {
        this.minioClient = minioClient;
        this.tokenService = tokenService;
        this.systemPictures = systemPictures;
        this.commonPictures = commonPictures;
    }

    @PostConstruct
    void initStartBuckets() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        createBucketInMinioIfNotExist(firstPhoto);
        createBucketInMinioIfNotExist(events);
        createBucketInMinioIfNotExist(sysNews);
        createBucketInMinioIfNotExist(commonNews);
        getSystemNews();
        getCommonNews();
    }

    public void createBucketInMinioIfNotExist(String bucketName) {
        try {
            if (!minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName).build()
            )) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName).build()
                );
                log.info("Бакет для хранения файлов с именем " + bucketName + " успешно создан на сервере MinIO");
            }
        } catch (Exception e) {
            log.error("Ошибка при создании бакета на сервере MinIO.");
            e.printStackTrace();
        }
    }


    public void savePhoto(byte[] photo, String uuid, String bucket) {

        try (InputStream inputStream = new ByteArrayInputStream(photo)) {

//            String fileName = ((String) tokenService.getTokenUser().getClaims().get("sub"));
            minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket(bucket)
                    .object(uuid)
                    .stream(inputStream, photo.length, -1)
                    .build());

        } catch (ServerException | InternalException | XmlParserException | InvalidResponseException |
                 InvalidKeyException |
                 NoSuchAlgorithmException | ErrorResponseException | InsufficientDataException | IOException e) {
            throw new RuntimeException(e);
        }
        ResponseEntity.ok("Photo is saved");
    }

    @Override
    public byte[] getPhoto(String bucket, String fileName) {
        try {
            // Получение объекта (файла) из MinIO
            try (InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .build()
            )) {
                // Создание ресурса с содержимым файла
               return IOUtils.toByteArray(inputStream);

            } catch (ServerException | InsufficientDataException | ErrorResponseException | NoSuchAlgorithmException |
                     InvalidKeyException | InvalidResponseException | XmlParserException | InternalException e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException | IOException e) {
                        log.error("Ошибка при получении файла из сервера MinIO.");

            }

        return new byte[0];
    }
    private void getSystemNews() {
        loadPictureToHolder(sysNews, systemPictures);
        log.info("System News photo is load to Holder");
    }

    private void loadPictureToHolder(String sysNews, Map<String, byte[]> systemPictures) {
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(sysNews).build());
            for (Result<Item> object :
                    results) {
                // Получение объекта (файла) из MinIO
                try (InputStream inputStream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(sysNews)
                                .object(object.get().objectName())
                                .build()
                )) {
                    // Создание ресурса с содержимым файла
                    byte[] fileBytes = IOUtils.toByteArray(inputStream);
                    systemPictures.put(object.get().objectName(), fileBytes);

                }
            }
        } catch (ErrorResponseException e) {
            if (e.getMessage().equals("The specified key does not exist.")) {
                log.warn("Файла на сервере MinIO не существует по UUID");

            } else {
                log.error("Ошибка при получении файла из сервера MinIO.");
                e.printStackTrace();

            }
        } catch (MinioException | IOException e) {
            log.error("Ошибка при получении файла из сервера MinIO.");
            e.printStackTrace();

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Ошибка при получении файла из сервера MinIO.");
            throw new RuntimeException(e);
        }
    }

    private void getCommonNews() {
        loadPictureToHolder(commonNews, commonPictures);
        log.info("Common News photo is load to Holder");
    }
}