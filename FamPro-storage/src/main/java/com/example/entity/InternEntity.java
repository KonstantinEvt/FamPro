package com.example.entity;

import com.example.enums.Assignment;
import com.example.enums.CheckStatus;
import com.example.enums.SecretLevel;
import com.example.enums.WorkStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@MappedSuperclass
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder
public class InternEntity {
    @Access(AccessType.PROPERTY)
    private String internName;

    @Enumerated(EnumType.STRING)
    private Assignment assignment;

    @Enumerated(EnumType.STRING)
    private WorkStatus workStatus;

    @Enumerated(EnumType.STRING)
    private CheckStatus checkStatus;

    @Column(name = "description")
    private String description;

    @Column(name = "UUID")
    private UUID uuid;

    @Column(name="tech_string")
    private String techString;

    @Enumerated(EnumType.STRING)
    private SecretLevel secretLevel;
}
