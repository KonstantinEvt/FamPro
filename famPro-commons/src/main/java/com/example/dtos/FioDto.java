package com.example.dtos;

import com.example.enums.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class FioDto {
    @Schema(description ="Уникальный идентификатор члена семьи")
    private UUID uuid;
    @Schema(description ="Имя")
    private String firstName;
    @Schema(description ="Фамилия")
    private String lastName;
    @Schema(description ="Отчество")
    private String middleName;
    @Schema(description ="День рождения")
    private Date birthday;
}
