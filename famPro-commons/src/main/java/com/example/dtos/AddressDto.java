package com.example.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressDto {
    @NotNull
    @Schema(description = "Идентификатор адреса")
    private Long id;
    @Schema(description = "Полный адрес")
    private String fullAddress;
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
