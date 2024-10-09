package com.example.dtos;

import com.example.enums.CheckStatus;
import com.example.enums.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@SuperBuilder
public class FioDto {
    @Schema(description ="Идентификатор члена семьи")
    private Long id;
    @Schema(description = "Имя")
    private String firstName;
    @Schema(description = "Отчество")
    private String middleName;
    @Schema(description = "Фамилия")
    private String lastName;
    @Schema(description = "День рождения")
    private Date birthday;
    @Schema(description = "Уникальный идентификатор члена семьи")
    private UUID uuid;
    @Schema(description ="Пол")
    private Sex sex;
    @Schema(name = "full_name")
    private String fullName;
    @Schema(name="status")
    private CheckStatus checkStatus;
}


