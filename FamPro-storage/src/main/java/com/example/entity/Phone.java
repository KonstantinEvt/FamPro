package com.example.entity;

import com.example.enums.Assignment;
import com.example.enums.Status;
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
@Table(name = "phones")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genSeqPhone")
    @SequenceGenerator(
            name = "genSeqPhone",
            sequenceName = "MemPhone", initialValue = 1, allocationSize = 50)
    private Long id;
    private String phoneNumber;
    private String description;
    @Enumerated(EnumType.STRING)
    private Assignment assignment;
    @Enumerated(EnumType.STRING)
    private Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return Objects.equals(id, phone.id) && Objects.equals(phoneNumber, phone.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
