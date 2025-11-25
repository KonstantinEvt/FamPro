package com.example.entity;

import com.example.enums.SecretLevel;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "familyGen")
    @SequenceGenerator(name = "familyGen",
            sequenceName = "seqFamilyGen", initialValue = 1, allocationSize = 5
    )
    private Long id;

    @Column(name = "UUID")
    private UUID uuid;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinColumn(name = "global_family")
//    private GlobalFamily globalFamily;

//    @Column(name = "activeStatus")
//    private boolean activeStatus;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "family_member",
            joinColumns = @JoinColumn(name = "family_id ", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"))
    private Set<ShortFamilyMember> familyMembers;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "family_parents",
            joinColumns = @JoinColumn(name = "family_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"))
    private Set<ShortFamilyMember> parents;

    @OneToMany(mappedBy = "familyWhereChild")
    private Set<ShortFamilyMember> children;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "half_children_by_father",
            joinColumns = @JoinColumn(name = "family_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"))
    private Set<ShortFamilyMember> halfChildrenByFather;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "half_children_by_mother",
            joinColumns = @JoinColumn(name = "family_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"))
    private Set<ShortFamilyMember> halfChildrenByMother;

    @Column(name = "halfMother")
    private boolean halfMotherExist;

    @Column(name = "halfFather")
    private boolean halfFatherExist;

    @Column(name = "InLow")
    private boolean inLowExist;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "children-in-low",
            joinColumns = @JoinColumn(name = "family_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"))
    private Set<ShortFamilyMember> childrenInLow;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "family_guard",
            joinColumns = @JoinColumn(name = "family_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "guard_id", referencedColumnName = "id"))
    private Set<Guard> guard;

    @Column(name = "husband_info")
    private String husbandInfo;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "husband")
    private ShortFamilyMember husband;

    @Column(name = "wife_info")
    private String wifeInfo;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "wife")
    private ShortFamilyMember wife;

    @Column(name = "activeGuard")
    private String activeGuard;

    @Column(name = "Birthday")
    private Date birthday;

    @Column(name = "Death_Day")
    private Date deathDay;

    @Column(name = "description")
    private String description;
    @Enumerated(EnumType.STRING)

    @Column(name = "security_photo")
    private SecretLevel secretLevelPhoto;

    @Enumerated(EnumType.STRING)
    @Column(name = "security_edit")
    private SecretLevel secretLevelEdit;

    @Enumerated(EnumType.STRING)
    @Column(name = "security_remove")
    private SecretLevel secretLevelRemove;

    @Enumerated(EnumType.STRING)
    @Column(name = "security_get")
    private SecretLevel secretLevelGet;
}
