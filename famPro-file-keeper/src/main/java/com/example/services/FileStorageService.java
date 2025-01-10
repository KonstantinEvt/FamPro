package com.example.services;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

/**
 * Сервис для сохранения файлов в хранилище и получения файлов из хранилища.
 */
public interface FileStorageService {
    /**
     * Метод для создания бакета (контейнера) в MinIO, если его не существует.
     * Он проверяет, существует ли бакет с указанным именем в MinIO, и если нет, то создает его.
     *
     * @param bucketName имя создаваемого бакета
     */
    void createBucketInMinioIfNotExist(String bucketName);

    /**
     * Метод для сохранения файла в MinIO.
     *
     * @param file загружаемый файл
     */

    void saveFirstPhoto(byte[] file, String uuid);


    /**
     * Метод для получения файла по-заданному UUID.
     *
     * @param fileName UUID файла
     * @return ответ с файлом или статусом "Not Found", если файл не найден
     */
    ResponseEntity<Resource> getFirstPhoto(String fileName);

}