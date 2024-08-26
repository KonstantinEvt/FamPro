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
@Table(name = "Emails")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genEmailSeq")
    @SequenceGenerator(
            name = "genEmailSeq",
            sequenceName = "EmailSeq", initialValue = 1, allocationSize = 50)
    private Long id;
    private String email;
    private String description;
    @Enumerated(EnumType.STRING)
    private Assignment assignment;
    @Enumerated(EnumType.STRING)
    private Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email1 = (Email) o;
        return Objects.equals(id, email1.id) && Objects.equals(email, email1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
