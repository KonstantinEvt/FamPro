package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
@SuperBuilder
@Table(name = "phones")
public class Phone  extends InternEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genSeqPhone")
    @SequenceGenerator(
            name = "genSeqPhone",
            sequenceName = "mem_phone", initialValue = 1, allocationSize = 5)
    private Long id;

    private String internName;

    @ManyToMany(mappedBy = "phonesSet")
    private Set<FamilyMemberInfo> familyMemberInfo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return Objects.equals(id, phone.id) && Objects.equals(internName, phone.internName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
