package com.example.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@ToString (callSuper = true)
public class PlaceDto extends InternDto{
    @Schema(description = "Страна")
    private String country;
    @Schema(description = "Регион или область")
    private String region;
    @Schema(description = "Город")
    private String city;
    @Schema(description = "Название улицы")
    private String street;
    @Schema(description = "Наличие фото")
    private Boolean photoExist;
}
