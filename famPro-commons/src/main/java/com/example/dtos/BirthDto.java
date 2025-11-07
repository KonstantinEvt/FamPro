package com.example.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BirthDto extends PlaceDto{

    private String internName;
    /**
     * Роддом
     */
    private String birthHouse;
    /**
     * Кто зарегистрировал
     */
    private String registration;
}
