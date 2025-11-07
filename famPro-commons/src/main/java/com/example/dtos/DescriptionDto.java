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
public class DescriptionDto {
    private Long id;
    private UUID uuid;
    private String common;
    private String education;
    private String profession;
}
