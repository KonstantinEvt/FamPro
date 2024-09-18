package com.example.models;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

// пока тут потом перемещу/изменю
@Component
@Data
@PropertySource("classpath:bootstrap.yml")
public class Bot {

    String name="FamPro_v1_bot";
    @Value("${spring.telegram}")
    String token;

}