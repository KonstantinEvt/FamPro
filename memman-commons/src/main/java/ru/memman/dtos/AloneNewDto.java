package ru.memman.dtos;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.memman.enums.Attention;
import ru.memman.enums.Localisation;
import ru.memman.enums.NewsCategory;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AloneNewDto {
    @Schema(description = "id письма")
    private UUID id;
    @Schema(description = "Внешнее uuid письма")
    private String externId;
    @Schema(description = "Кто послал")
    private String sendingFrom;
    @Schema(description = "Альтернативный посылающий")
    private String sendingFromAlt;
    @Schema(description = "Кому послали")
    private String sendingTo;
    @Schema(description = "Аннотаия письма")
    private Attention attention;
    @Schema(description = "Дата создания")
    private Date creationDate;
    @Schema(description = "Заголовок письма")
    private String subject;
    @Schema(description = "Текст письма")
    private String textInfo;
    @Schema(description = "В какую категорию писем попадет")
    private NewsCategory category;
    @Schema(description = "Триггер прочтения письма")
    private boolean alreadyRead;
    @Schema(description = "Локализация письма")
    private Localisation localisation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AloneNewDto that = (AloneNewDto) o;
        return Objects.equals(id, that.id) && Objects.equals(subject, that.subject) && Objects.equals(textInfo, that.textInfo) && category == that.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
