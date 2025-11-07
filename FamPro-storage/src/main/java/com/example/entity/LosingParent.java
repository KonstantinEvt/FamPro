package com.example.entity;

import com.example.enums.CheckStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;
import java.util.UUID;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@Table(name = "losing_parents")
public class LosingParent extends Fio {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genSeqLosFio")
    @SequenceGenerator(
            name = "genSeqLosFio",
            sequenceName = "losing_parents_seq", initialValue = 1, allocationSize = 5)
    private Long id;

    @Column(name = "losing_UUID")
    private UUID losingUUID;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "member_id")
    private FamilyMember member;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LosingParent that = (LosingParent) o;
        return Objects.equals(id, that.id) && Objects.equals(member, that.member) && Objects.equals(losingUUID, that.losingUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member);
    }
}
