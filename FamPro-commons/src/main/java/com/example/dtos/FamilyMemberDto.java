package com.example.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Date;
@Data
public class FamilyMemberDto  {
    @Schema(description ="Идентификатор члена семьи")
    private Long id;
    @Schema(description ="Имя")
    private String firstname;
    @Schema(description ="Фамилия")
    private String lastname;
    @Schema(description ="Отчество")
    private String middlename;
    @Schema(description ="Пол")
    private Boolean sex;
    @Schema(description ="День рождения")
    private Date birthday;
    @Schema(description ="Идентфикатор матери")
    private Long mother_id;
    @Schema(description ="Идентификатор отца")
    private Long father_id;
}
