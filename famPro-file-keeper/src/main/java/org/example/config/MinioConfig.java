package org.example.config;

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
    @Value("${minio.first_photo_bucket}")
    String firstPhoto;
    @Value("${minio.photo_events_bucket}")
    String events;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioEndpoint)
                .credentials(minioAccessKey, minioSecretKey)
                .build();
    }
//    @Bean(name = "commonsMultipartResolver")
//    public MultipartResolver multipartResolver() {
//        return new StandardServletMultipartResolver();
//    }
//
//
//    @Bean
//    public MultipartConfigElement multipartConfigElement() {
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//
//        factory.setMaxFileSize(DataSize.ofBytes(2000000L));
//        factory.setMaxRequestSize(DataSize.ofBytes(2000000L));
//
//        return factory.createMultipartConfig();
//    }
}

