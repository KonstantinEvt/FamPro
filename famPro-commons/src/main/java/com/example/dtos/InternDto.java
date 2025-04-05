package com.example.dtos;

import com.example.enums.Assignment;
import com.example.enums.Localisation;
import com.example.enums.SecretLevel;
import com.example.enums.Status;
import lombok.*;


@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@ToString
public class InternDto {
    private Long id;
    private String description;
    private Assignment assignment;
    private Status status;
    private SecretLevel secretLevel;
    private Localisation localisation;
}
