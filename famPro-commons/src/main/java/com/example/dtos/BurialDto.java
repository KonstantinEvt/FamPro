package com.example.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true)
public class BurialDto extends PlaceDto{
    /**
     * Полный адрес
     */
    private String internName;
    /**
     * Кладбище
     */

    private String cemetery;
    /**
     * Раздел
     */

    private String chapter;
    /**
     * Квадрат
     */

    private String square;
    /**
     * Номер могилы
     */
    private String grave;
}
