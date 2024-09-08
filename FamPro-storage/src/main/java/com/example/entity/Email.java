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
@Table(name = "emails")
public class Email extends InternEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genEmailSeq")
    @SequenceGenerator(
            name = "genEmailSeq",
            sequenceName = "EmailSeq", allocationSize = 5)
    private Long id;
    @Column(name="email_name")
    private String internName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(id, email.id) && Objects.equals(internName, email.internName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
