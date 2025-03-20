package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class MapsOfHolders {
    @Bean
    Map<String, byte[]> systemPictures(){return new ConcurrentHashMap<>();}
    @Bean
    Map<String, byte[]> commonPictures(){return new ConcurrentHashMap<>();}

    @Bean
    Map<String, byte[]> defaultPhotos(){return new ConcurrentHashMap<>();}
}
