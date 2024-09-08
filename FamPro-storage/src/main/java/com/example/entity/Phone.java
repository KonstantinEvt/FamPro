package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
@Table(name = "phones")
public class Phone  extends InternEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genSeqPhone")
    @SequenceGenerator(
            name = "genSeqPhone",
            sequenceName = "MemPhone", initialValue = 1, allocationSize = 5)
    private Long id;
    private String internName;

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
