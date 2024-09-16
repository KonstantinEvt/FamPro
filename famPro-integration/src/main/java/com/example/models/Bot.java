package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@PropertySource("classpath:application.yml")
public class Bot {

    String name="FamPro_v1_bot";
    @Value("${spring.telegram}")
    String token;

}