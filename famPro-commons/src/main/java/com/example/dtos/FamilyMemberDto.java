package com.example.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;


@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true)
public class FamilyMemberDto extends FioDto implements Serializable {
    @Schema(description = "Идентификатор члена семьи")
    private Long id;
    @Schema(description = "Идентфикатор матери")
    private String motherInfo;
    @Schema(description = "Идентификатор отца")
    private String fatherInfo;
    @Schema(description = "Идентфикатор матери")
    private FioDto motherFio;
    @Schema(description = "Идентификатор отца")
    private FioDto fatherFio;
    @Schema(description = "Расширенная информация")
    private FamilyMemberInfoDto memberInfo;
    @Schema(description = "Старые имена")
    private Set<FioDto> fioDtos;
    @Schema(description = "Дата смерти")
    private Date deathday;
    private BurialDto burial;
    private BirthDto birth;
    private String creator;
    private Timestamp createTime;
    @Schema(description = "Выбранная локализцаия")
    private String localisation;
    private boolean primePhoto;
}


