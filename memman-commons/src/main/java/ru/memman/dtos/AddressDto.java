package ru.memman.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.memman.enums.Localisation;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AddressDto extends PlaceDto {
    /**
     * Полный адрес
     */
    @Schema(description = "Полный адрес")
    private String internName;
    /**
     * Номер дома
     */
    @Schema(description = "Номер дома")
    private String house;
    /**
     * Почтовый индекс
     */
    @Schema(description = "Почтовый индекс")
    private String index;
    /**
     * Корпус здания
     */
    @Schema(description = "Корпус здания")
    private String building;
    /**
     * Номер квартиры
     */
    @Schema(description = "Номер квартиры")
    private String flatNumber;
    /**
     * Локализация адреса
     */
    @Schema(description = "Локализация адреса")
    private Localisation localisation;
}
