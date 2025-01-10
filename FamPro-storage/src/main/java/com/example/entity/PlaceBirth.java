package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceBirth address = (PlaceBirth) o;
        return Objects.equals(id, address.id) && Objects.equals(internName, address.internName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
