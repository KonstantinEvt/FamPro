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
@Table(name = "burials")
public class PlaceBurial extends Place{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genSeqBurials")
    @SequenceGenerator(
            name = "genSeqBurials",
            sequenceName = "seqBurials", initialValue = 1, allocationSize = 5)
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
}
