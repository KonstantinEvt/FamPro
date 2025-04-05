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
@Table(name = "burials")
public class PlaceBurial extends Place{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genSeqBurials")
    @SequenceGenerator(
            name = "genSeqBurials",
            sequenceName = "seq_burials", initialValue = 1, allocationSize = 5)
    private Long id;
    /**
     * Полный адрес
     */
    @Column(name = "intern_name", length = 255)
    private String internName;
    /**
     * Почтовый индекс
     */
    @Column(name = "cemetery", length = 255)
    private String cemetery;
    /**
     * Корпус здания
     */
    @Column(name = "chapter", length = 50)
    private String chapter;
    /**
     * Номер дома
     */
    @Column(name = "square", length = 50)
    private String square;
    /**
     * Номер квартиры
     */
    @Column(name = "grave", length = 50)
    private String grave;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceBurial address = (PlaceBurial) o;
        return Objects.equals(id, address.id) && Objects.equals(internName, address.internName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
