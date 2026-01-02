package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Guard {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guardGen")
    @SequenceGenerator(name = "guardGen",
            sequenceName = "seq_guard_gen", initialValue = 1, allocationSize = 5
    )
    private Long id;

    @Column(name = "token_user")
    private String tokenUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linked_person")
    private ShortFamilyMember linkedPerson;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guard guard = (Guard) o;
        return Objects.equals(id, guard.id) && Objects.equals(tokenUser, guard.tokenUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
