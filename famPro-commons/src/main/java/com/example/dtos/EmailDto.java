package com.example.dtos;

import com.example.enums.Assignment;
import com.example.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@NotNull
@Schema(description = "Идентификатор эл.почты")
public class EmailDto {
    private Long id;
    private String email;
    private String description;
    private Assignment assignment;
    private Status status;
}
