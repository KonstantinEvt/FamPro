package com.example.dtos;

import com.example.enums.Assignment;
import com.example.enums.Status;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@Builder
public class InternDto {
    private Long id;
    private String description;
    private Assignment assignment;
    private Status status;
}
