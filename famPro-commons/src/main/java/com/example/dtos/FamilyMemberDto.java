package com.example.dtos;

import com.example.enums.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

@Data
public class FamilyMemberDto implements Serializable {
    @Schema(description ="Идентификатор члена семьи")
    private Long id;
    @Schema(description ="Уникальный идентификатор члена семьи")
    private UUID uuid;
    @Schema(description ="Имя")
    private String firstName;
    @Schema(description ="Фамилия")
    private String lastName;
    @Schema(description ="Отчество")
    private String middleName;
    @Schema(description ="Пол")
    private Sex sex;
    @Schema(description ="День рождения")
    private Date birthday;
    @Schema(description ="Идентфикатор матери")
    private FioDto motherFio;
    @Schema(description ="Идентификатор отца")
    private FioDto fatherFio;
    @Schema(description ="Расширенная информация")
    private FamilyMemberInfoDto memberInfo;
}


