package com.example.dtos;

import com.example.enums.Assignment;
import com.example.enums.Localisation;
import com.example.enums.SecretLevel;
import com.example.enums.WorkStatus;
import lombok.*;


@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class InternDto {
    private Long id;
    private String description;
    private Assignment assignment;
    private WorkStatus workStatus;
    private SecretLevel secretLevel;
    private Localisation localisation;
}
