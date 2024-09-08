package com.example.dtos;

import com.example.enums.Assignment;
import com.example.enums.CheckStatus;
import com.example.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class EmailDto extends InternDto{
    private String internName;
}
