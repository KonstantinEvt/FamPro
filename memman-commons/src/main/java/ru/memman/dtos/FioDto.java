package ru.memman.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.memman.enums.Localisation;
import ru.memman.enums.Sex;

import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@SuperBuilder
@ToString
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
    private Localisation localisation;
}


