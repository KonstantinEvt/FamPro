package com.example.entity;

import com.example.enums.Assignment;
import com.example.enums.CheckStatus;
import com.example.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder
public class InternEntity {
    private String internName;
    @Enumerated(EnumType.STRING)
    private Assignment assignment;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private CheckStatus checkStatus;
    private String description;
    @Column(name = "UUID")
    private UUID uuid;
    @Column(name="tech_string")
    private String techString;
}
