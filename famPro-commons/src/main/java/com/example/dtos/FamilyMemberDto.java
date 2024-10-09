package com.example.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Set;


@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder
public class FamilyMemberDto extends FioDto implements Serializable {
    @Schema(description ="Идентификатор члена семьи")
    private Long id;
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
    @Schema(description ="Старые имена")
    public Set<FioDto> fioDtos;

}


