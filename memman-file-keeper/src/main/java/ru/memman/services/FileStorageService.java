package ru.memman.services;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Сервис для сохранения файлов в хранилище MinIO и для получения файлов из хранилища MinIO.
 * Реализует интерфейс FileStorageService.
 */
@Log4j2
@Service
public class FileStorageService {

    private final MinioClient minioClient;

    @Value("${minio.buckets.first_photo}")
    private String primePhoto;
    @Value("${minio.buckets.little_prime}")
    private String littlePrime;
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
    private final List<String> bucketsNames = new ArrayList<>();
    private final Map<String, byte[]> systemPictures;
    private final Map<String, byte[]> commonPictures;
    private final Map<String, byte[]> defaultPhotos;

    public FileStorageService(MinioClient minioClient,
                              Map<String, byte[]> systemPictures,
                              Map<String, byte[]> commonPictures,
                              Map<String, byte[]> defaultPhotos) {
        this.minioClient = minioClient;
        this.systemPictures = systemPictures;
        this.commonPictures = commonPictures;
        this.defaultPhotos = defaultPhotos;
    }

    @PostConstruct
    void initStartBuckets() {
        createBucketInMinioIfNotExist(primePhoto);
        createBucketInMinioIfNotExist(defaultPhoto);
        createBucketInMinioIfNotExist(littlePrime);
        createBucketInMinioIfNotExist(sysNews);
        createBucketInMinioIfNotExist(commonNews);
        createBucketInMinioIfNotExist(birth);
        createBucketInMinioIfNotExist(burial);
        getSystemNews();
        getCommonNews();
        getDefaultPhotos();
        bucketsNames.addAll(List.of(primePhoto,defaultPhoto,commonNews, littlePrime,sysNews,burial,birth));
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
        if (!bucketsNames.contains(bucket)) createBucketInMinioIfNotExist(bucket);
        try (InputStream inputStream = new ByteArrayInputStream(photo)) {
            minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket(bucket)
                    .object(uuid)
                    .stream(inputStream, photo.length, -1)
                    .build());
        } catch (ServerException | InternalException | XmlParserException | InvalidResponseException |
                 InvalidKeyException |
                 NoSuchAlgorithmException | ErrorResponseException | InsufficientDataException | IOException e) {
            throw new RuntimeException("Ошибка при сохранении файла на сервер MinIO", e);
        }
        ResponseEntity.ok("Photo is saved");
    }

    public byte[] getPhoto(String bucket, String fileName) {
        log.info("Получаем фото из бакета {} с именем {}", bucket, fileName);
        try {
            try (InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .build()
            )) {
                log.info("Получено фото из бакета {} с именем {}", bucket, fileName);
                return IOUtils.toByteArray(inputStream);
            } catch (ServerException | InsufficientDataException | ErrorResponseException | NoSuchAlgorithmException |
                     InvalidKeyException | InvalidResponseException | XmlParserException | InternalException e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException | IOException e) {
            log.error("Ошибка при получении файла из сервера MinIO.");
        }
        log.info("Something wrong");
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
                    String name = object.get()
                            .objectName()
                            .split("\\.")[0];
                    mapOfPhoto.put(name, fileBytes);

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

    public void savePrivatePhoto(String name, MultipartFile frontPicture, String bucket) {
        try (InputStream file = frontPicture.getInputStream()) {
            byte[] bytesOfPicture = file.readAllBytes();
            savePhoto(bytesOfPicture, name, bucket);
            log.info("Файл успешно сохранен на сервер MinIO.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("Ошибка при сохранении файла на сервер MinIO.", e);
        }
    }

    public void deletePhoto(String bucket, String name) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucket)
                .object(name)
                .build());
    }

    public void renamePhoto(String bucket, String oldName, String newName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.copyObject(CopyObjectArgs.builder()
                .bucket(bucket)
                .object(newName)
                .source(CopySource.builder()
                        .bucket(bucket)
                        .object(oldName)
                        .build())
                .build());
        deletePhoto(bucket, oldName);
    }
}