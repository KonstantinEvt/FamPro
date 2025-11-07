package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
@Table(name = "description")
public class Description {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DescriptionGen")
    @SequenceGenerator(name = "DescriptionGen",
            sequenceName = "description_seq", initialValue = 1, allocationSize = 20
    )
    private Long id;

    @Column(name = "UUID")
    private UUID uuid;

    @Column(name = "common")
    private String common;

    @Column(name = "education")
    private String education;

    @Column(name="profession")
    private String profession;

    @ManyToMany(mappedBy = "descriptionData")
    private Set<FamilyMemberInfo> familyMemberInfo;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Description that = (Description) o;
        return Objects.equals(id, that.id) && Objects.equals(uuid, that.uuid) && Objects.equals(common, that.common) && Objects.equals(education, that.education) && Objects.equals(profession, that.profession);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
