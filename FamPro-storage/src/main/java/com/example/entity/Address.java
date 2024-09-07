package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genSeqAddress")
    @SequenceGenerator(
            name = "genSeqAddress",
            sequenceName = "seqAddress", initialValue = 1, allocationSize = 5)
    private Long id;
    /**
     * Полный адрес
     */
    @Column(name = "full_address", length = 500)
    private String internName;
    /**
     * Название улицы
     */
    @Column(name = "street", length = 50)
    private String street;
    /**
     * Номер дома
     */
    @Column(name = "house", length = 50)
    private String house;
    /**
     * Почтовый индекс
     */
    @Column(name = "postcode", length = 50)
    private String index;
    /**
     * Корпус здания
     */
    @Column(name = "building", length = 50)
    private String building;
    /**
     * Город
     */
    @Column(name = "city", length = 50)
    private String city;
    /**
     * Регион или область
     */
    @Column(name = "region", length = 50)
    private String region;
    /**
     * Страна
     */
    @Column(name = "country", length = 50)
    private String country;
    /**
     * Номер квартиры
     */
    @Column(name = "flat", length = 50)
    private String flat;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id) && Objects.equals(internName, address.internName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
