package com.example.dtos;

import com.example.enums.Assignment;
import com.example.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PhoneDto {
    @NotNull
    @Schema(description = "Идентификатор телефона")
    private Long id;
    private String phoneNumber;
    private String description;
    private Assignment assignment;
    private Status status;
}
