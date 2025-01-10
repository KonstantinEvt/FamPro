package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "familyGen")
    @SequenceGenerator(name = "familyGen",
            sequenceName = "seqFamilyGen", initialValue = 1, allocationSize = 5
    )
    private Long id;
    @ManyToMany
    @JoinTable(name = "family_parents",
            joinColumns = @JoinColumn(name = "family_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"))
    private Set<ShortFamilyMember> parents;
    @ManyToMany
    @JoinTable(name = "family_children",
            joinColumns = @JoinColumn(name = "family_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"))
    private Set<ShortFamilyMember> children;
    @Column(name = "uuid")
    private UUID uuid;
    @Column(name = "Birthday")
    private Date birthday;
    @Column(name = "Death_Day")
    private Date deathDay;
    @Column(name = "description")
    private String description;
}
