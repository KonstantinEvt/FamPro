package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;
import java.util.Set;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder
@ToString(callSuper = true)
@Table(name = "addresses")
public class Address extends Place{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genSeqAddress")
    @SequenceGenerator(
            name = "genSeqAddress",
            sequenceName = "seq_address", initialValue = 1, allocationSize = 5)
    private Long id;
    /**
     * Полный адрес
     */
    @Column(name = "intern_name", length = 500)
    private String internName;
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
     * Номер дома
     */
    @Column(name = "house", length = 50)
    private String house;
   /**
     * Номер квартиры
     */
    @Column(name = "flat", length = 50)
    private String flatNumber;

    @ManyToMany(mappedBy = "addressesSet")
    private Set<FamilyMemberInfo> familyMemberInfo;

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
