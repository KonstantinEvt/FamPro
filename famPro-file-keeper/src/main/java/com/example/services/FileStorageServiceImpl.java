package com.example.services;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Сервис для сохранения файлов в хранилище MinIO и для получения файлов из хранилища MinIO.
 * Реализует интерфейс FileStorageService.
 */
@Log4j2
@Service
public class FileStorageServiceImpl {

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
    @Value("${minio.default_photo_bucket}")
    private String defaultPhoto;

    private Map<String, byte[]> systemPictures;
    private Map<String, byte[]> commonPictures;

    private Map<String, byte[]> defaultPhotos;

    public FileStorageServiceImpl(MinioClient minioClient,
                                  TokenService tokenService,
                                  Map<String, byte[]> systemPictures,
                                  Map<String, byte[]> commonPictures,
                                  Map<String, byte[]> defaultPhotos) {
        this.minioClient = minioClient;
        this.tokenService = tokenService;
        this.systemPictures = systemPictures;
        this.commonPictures = commonPictures;
        this.defaultPhotos = defaultPhotos;
    }

    @PostConstruct
    void initStartBuckets() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        createBucketInMinioIfNotExist(firstPhoto);
        createBucketInMinioIfNotExist(defaultPhoto);
        createBucketInMinioIfNotExist(events);
        createBucketInMinioIfNotExist(sysNews);
        createBucketInMinioIfNotExist(commonNews);
        getSystemNews();
        getCommonNews();
        getDefaultPhotos();
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
        if (!bucket.equals(firstPhoto) || !bucket.equals(sysNews) || !bucket.equals(commonNews) || !bucket.equals(events))
            createBucketInMinioIfNotExist(bucket);
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

    private void getCommonNews() {
        loadPictureToHolder(commonNews, commonPictures);
        log.info("Common News photo is load to Holder");
    }

    private void getDefaultPhotos() {
        loadPictureToHolder(defaultPhoto, defaultPhotos);
        log.info("Default photo is load to Holder");
    }

    private void loadPictureToHolder(String bucket, Map<String, byte[]> mapOfPhoto) {
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucket).build());
            for (Result<Item> object :
                    results) {
                // Получение объекта (файла) из MinIO
                try (InputStream inputStream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(bucket)
                                .object(object.get().objectName())
                                .build()
                )) {
                    // Создание ресурса с содержимым файла
                    byte[] fileBytes = IOUtils.toByteArray(inputStream);
                    mapOfPhoto.put(object.get().objectName(), fileBytes);

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

    public void saveNewsPhoto(String name, MultipartFile frontPicture) {
        String bucketMinio = (String) tokenService.getTokenUser().getClaims().get("sub");
        try (InputStream file = frontPicture.getInputStream()) {
            byte[] bytesOfPicture = file.readAllBytes();
            savePhoto(bytesOfPicture, name, bucketMinio);
            log.info("Файл успешно сохранен на сервер MinIO.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("Ошибка при сохранении файла на сервер MinIO.", e);
        }
        System.out.println("tyt_tyt");
    }

}