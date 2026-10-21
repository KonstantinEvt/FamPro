package ru.memman.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.memman.enums.Localisation;
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LetterLocalDto {
    @Schema(description = "Локализация письма")
    private Localisation localisation;

    @Schema(description = "Заголовок письма")
    private String subject;

    @Schema(description = "Текст письма")
    private String textInfo;
}
