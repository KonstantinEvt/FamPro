package com.example.dtos;

import com.example.enums.Localisation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true)
public class AddressDto extends PlaceDto{
    @Schema(description = "Полный адрес")
    private String internName;
    @Schema(description = "Номер дома")
    private String house;
    @Schema(description = "Почтовый индекс")
    private String index;
    @Schema(description = "Корпус здания")
    private String building;
    @Schema(description = "Номер квартиры")
    private String flat;
    private Localisation localisation;
}
