package com.example.dtos;

import com.example.enums.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class FamilyMemberDto extends FioDto implements Serializable {
    @Schema(description ="Пол")
    private Sex sex;
    @Schema(description ="Идентфикатор матери")
    private String motherInfo;
    @Schema(description ="Идентификатор отца")
    private String fatherInfo;
    @Schema(description ="Идентфикатор матери")
    private FioDto motherFio;
    @Schema(description ="Идентификатор отца")
    private FioDto fatherFio;
    @Schema(description ="Расширенная информация")
    private FamilyMemberInfoDto memberInfo;

}


