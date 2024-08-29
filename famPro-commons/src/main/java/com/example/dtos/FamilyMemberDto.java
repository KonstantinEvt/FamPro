package com.example.dtos;

import com.example.enums.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
@Data
public class FamilyMemberDto implements Serializable {
    @Schema(description ="Идентификатор члена семьи")
    private Long id;
    @Schema(description ="Имя")
    private String firstname;
    @Schema(description ="Фамилия")
    private String lastname;
    @Schema(description ="Отчество")
    private String middlename;
    @Schema(description ="Пол")
    private Sex sex;
    @Schema(description ="День рождения")
    private Date birthday;
    @Schema(description ="Идентфикатор матери")
    private Long mother_id;
    @Schema(description ="Идентификатор отца")
    private Long father_id;
    @Schema(description ="Расширенная информация")
    private FamilyMemberInfoDto familyMemberInfo;
}
