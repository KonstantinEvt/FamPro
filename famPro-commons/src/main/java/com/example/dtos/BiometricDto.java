package com.example.dtos;

import com.example.enums.Colors;
import com.example.enums.Localisation;
import lombok.*;

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
    private int height;
    private int weight;
    private int footSize;
    private Colors hairColor;
    private Colors eyesColor;
    private int shirtSize;
    private Localisation localisation;
}
