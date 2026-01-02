package com.example.entity;

import com.example.enums.RoleInFamily;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;
import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Builder
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "family_member_links")
public class FamilyMemberLink {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family")
    private Family family;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member")
    private ShortFamilyMember member;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleInFamily roleInFamily;

    @Column(name = "description_link")
    private String description;

    @Column(name = "cause_person")
    private UUID causePerson;

    @Column(name = "linking_guard")
    private UUID linkGuard;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FamilyMemberLink that = (FamilyMemberLink) o;
        return Objects.equals(uuid, that.uuid) && roleInFamily == that.roleInFamily && Objects.equals(description, that.description) && Objects.equals(causePerson, that.causePerson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
