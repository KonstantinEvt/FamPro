package ru.memman.config;

import io.minio.MinioClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Сервис для сохранения файлов в хранилище MinIO и для получения файлов из хранилища MinIO.
 * Реализует интерфейс FileStorageService.
 */
@Log4j2
@Configuration
public class MinioConfig {
    /**
     * Объект MinioClient из библиотеки MinIO, используемый для взаимодействия с хранилищем MinIO.
     */
    @Value("${minio.endpoint}")
    String minioEndpoint;
    @Value("${minio.accessKey}")
    String minioAccessKey;
    @Value("${minio.secretKey}")
    String minioSecretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioEndpoint)
                .credentials(minioAccessKey, minioSecretKey)
                .build();
    }

}

