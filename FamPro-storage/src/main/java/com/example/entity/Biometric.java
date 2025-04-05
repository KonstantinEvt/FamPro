package com.example.entity;

import com.example.enums.Colors;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
@EqualsAndHashCode
@Table(name = "biometric")
public class Biometric {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BiometricGen")
    @SequenceGenerator(name = "BiometricGen",
            sequenceName = "biometric_seq", initialValue = 1, allocationSize = 20
    )
    private Long id;
    @Column(name = "UUID")
    private UUID uuid;
    @Column(name = "height")
    private int height;
    @Column(name = "weight")
    private int weight;
    @Column(name="foot_size")
    private int footSize;

    @Enumerated(EnumType.STRING)
    @Column(name="hair_color")
    private Colors hairColor;

    @Enumerated(EnumType.STRING)
    @Column(name = "eyes_color")
    private Colors eyesColor;
    @Column(name = "shirt_size")
    private int shirtSize;
}
