package ru.memman.dtos;


import lombok.*;
import ru.memman.enums.Colors;
import ru.memman.enums.Localisation;

import java.util.UUID;
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class BiometricDto {
    private Long id;
    private UUID uuid;
    private int age;
    private int height;
    private int weight;
    private int footSize;
    private Colors hairColor;
    private Colors eyesColor;
    private int shirtSize;
    private String description;
    private Localisation localisation;
}
