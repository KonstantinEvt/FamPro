package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder
@ToString(callSuper = true)
@Table(name = "births")
public class PlaceBirth extends Place{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genSeqBirths")
    @SequenceGenerator(
            name = "genSeqBirths",
            sequenceName = "seqBurials", initialValue = 1, allocationSize = 5)
    private Long id;
    /**
     * Полный адрес
     */
    @Column(name = "intern_name", length = 255)
    private String internName;
    /**
     * Роддом
     */
    @Column(name = "birth_house", length = 255)
    private String bithHouse;
    /**
     * Кто зарегистрировал
     */
    @Column(name = "registration_entity", length = 255)
    private String registration;

}
