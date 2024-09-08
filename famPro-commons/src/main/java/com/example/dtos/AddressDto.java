package com.example.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class AddressDto extends InternDto{
    private String internName;
    @Schema(description = "Название улицы")
    private String street;
    @Schema(description = "Номер дома")
    private String house;
    @Schema(description = "Почтовый индекс")
    private String index;
    @Schema(description = "Корпус здания")
    private String building;
    @Schema(description = "Город")
    private String city;
    @Schema(description = "Регион или область")
    private String region;
    @Schema(description = "Страна")
    private String country;
    @Schema(description = "Номер квартиры")
    private String flat;
}
