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
@Table(name = "emails")
public class Email extends InternEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genEmailSeq")
    @SequenceGenerator(
            name = "genEmailSeq",
            sequenceName = "email_seq", allocationSize = 5)
    private Long id;

    @ManyToMany(mappedBy = "emailsSet")
    private Set<FamilyMemberInfo> familyMemberInfo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(id, email.id) && Objects.equals(super.getInternName(),email.getInternName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
