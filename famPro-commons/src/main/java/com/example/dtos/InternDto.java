package com.example.dtos;

import com.example.enums.Assignment;
import com.example.enums.CheckStatus;
import com.example.enums.Status;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class InternDto {
    private Long id;
    private String description;
    private Assignment assignment;
    private Status status;
}
