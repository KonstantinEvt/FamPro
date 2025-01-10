package com.example.services;

import io.minio.*;
import io.minio.errors.*;
import jakarta.annotation.PostConstruct;
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

/**
 * Сервис для сохранения файлов в хранилище MinIO и для получения файлов из хранилища MinIO.
 * Реализует интерфейс FileStorageService.
 */
@Log4j2
@Service

public class FileStorageServiceImpl implements FileStorageService {

    private final MinioClient minioClient;
    private final TokenService tokenService;

    @Value("${minio.first_photo_bucket}")
    private String firstPhoto;
    @Value("${minio.photo_events_bucket}")
    private String events;
    private PhotoHolder photoHolder;

    public FileStorageServiceImpl(MinioClient minioClient, TokenService tokenService) {
        this.minioClient = minioClient;
        this.tokenService = tokenService;
    }

    @PostConstruct
    void initStartBuckets() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        createBucketInMinioIfNotExist(firstPhoto);
        createBucketInMinioIfNotExist(events);
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


    public void saveFirstPhoto(byte[] photo, String uuid){

        try (InputStream inputStream = new ByteArrayInputStream(photo)) {

//            String fileName = ((String) tokenService.getTokenUser().getClaims().get("sub"));
            minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket(firstPhoto)
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
    public ResponseEntity<Resource> getFirstPhoto(String fileName) {
        try {
            // Получение объекта (файла) из MinIO
            try (InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(firstPhoto)
                            .object(fileName)
                            .build()
            )) {
                       // Создание ресурса с содержимым файла
                    byte[] fileBytes = IOUtils.toByteArray(inputStream);
                    Resource resource = new InputStreamResource(new ByteArrayInputStream(fileBytes));
                    // Возвращение ResponseEntity с содержимым файла
                    return ResponseEntity.ok()
                            .header("Content-Name", "UUID = \"" + fileName + "\"")
                            .contentLength(fileBytes.length)
                            .body(resource);

            }
        } catch (ErrorResponseException e) {
            if (e.getMessage().equals("The specified key does not exist.")) {
                log.warn("Файла на сервере MinIO не существует по UUID: " + fileName);
                return ResponseEntity.notFound().build();
            } else {
                log.error("Ошибка при получении файла из сервера MinIO.");
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (MinioException | IOException e) {
            log.error("Ошибка при получении файла из сервера MinIO.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Ошибка при получении файла из сервера MinIO.");
            throw new RuntimeException(e);
        }
    }
}