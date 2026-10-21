package ru.memman.dtos;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.memman.enums.Attention;
import ru.memman.enums.Localisation;
import ru.memman.enums.NewsCategory;

import java.util.*;

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
    @Schema(description = "Есть ли прайм фото у посылающего")
    private boolean existPrimePhoto;
    @Schema(description = "Скрыто ли прайм фото посылающего")
    private boolean hidden;
    @Schema(description = "Альтернативный посылающий")
    private String sendingFromAlt;
    @Schema(description = "Кому послали")
    private String sendingTo;
    @Schema(description = "Кому послали список")
    private Set<String> addressList;
    @Schema(description = "Аннотация письма")
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
    @Schema(description = "Адрес картинки письма")
    private String imageUrl;
    @Schema(description = "Альтернативные локализации")
    private List<LetterLocalDto> letterLocals;
    @Schema(description = "Наличие дополнительных локализаций")
    private boolean existLocalisations;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AloneNewDto that = (AloneNewDto) o;
        return existPrimePhoto == that.existPrimePhoto && alreadyRead == that.alreadyRead && existLocalisations == that.existLocalisations && Objects.equals(id, that.id) && Objects.equals(externId, that.externId) && Objects.equals(sendingFrom, that.sendingFrom) && Objects.equals(sendingFromAlt, that.sendingFromAlt) && Objects.equals(sendingTo, that.sendingTo) && Objects.equals(addressList, that.addressList) && attention == that.attention && Objects.equals(creationDate, that.creationDate) && Objects.equals(subject, that.subject) && Objects.equals(textInfo, that.textInfo) && category == that.category && localisation == that.localisation && Objects.equals(imageUrl, that.imageUrl) && Objects.equals(letterLocals, that.letterLocals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
