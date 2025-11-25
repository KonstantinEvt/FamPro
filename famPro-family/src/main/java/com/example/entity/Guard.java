package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class Guard {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guardGen")
    @SequenceGenerator(name = "guardGen",
            sequenceName = "seq_guard_gen", initialValue = 1, allocationSize = 5
    )
    private Long id;

    @Column(name = "token_user")
    private String tokenUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linked_person")
    private ShortFamilyMember linkedPerson;

//    @ManyToMany(mappedBy = "guard")
//    private Set<Family> familiesAtGuard;
//
//    @ManyToMany(mappedBy = "guard")
//    private Set<Family> globalFamiliesByGuard;
}
