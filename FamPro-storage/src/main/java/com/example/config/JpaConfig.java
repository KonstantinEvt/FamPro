package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EntityScan(basePackages = "com.example.entity")
public class JpaConfig {
    @Value("${spring.application.datasource.url}")
    private String url;
    @Value("${spring.application.datasource.username}")
    private String username;
    @Value("${spring.application.datasource.password}")
    private String password;

    @Bean
    DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url(url)
                .username(username)
                .password(password)
                .build();
    }

}
