package com.example.entity;

import com.example.enums.Assignment;
import com.example.enums.CheckStatus;
import com.example.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class InternEntity {
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