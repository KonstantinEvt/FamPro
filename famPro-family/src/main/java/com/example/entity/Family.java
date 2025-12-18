package com.example.entity;

import com.example.enums.SecretLevel;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Objects;
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
            sequenceName = "seq_family_gen", initialValue = 1, allocationSize = 5
    )
    private Long id;

    @Column(name = "UUID")
    private UUID uuid;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinColumn(name = "global_family")
//    private GlobalFamily globalFamily;

//    @Column(name = "activeStatus")
//    private boolean activeStatus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "family", cascade = {CascadeType.REMOVE})
    private Set<FamilyMemberLink> familyMemberLinks;

//    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinTable(name = "family_parents",
//            joinColumns = @JoinColumn(name = "family_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"))
//    private Set<ShortFamilyMember> parents;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "familyWhereChild")
    private Set<ShortFamilyMember> children;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "half_children_by_father",
//            joinColumns = @JoinColumn(name = "family_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"))
//    private Set<ShortFamilyMember> halfChildrenByFather;
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "half_children_by_mother",
//            joinColumns = @JoinColumn(name = "family_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"))
//    private Set<ShortFamilyMember> halfChildrenByMother;
//
//    @Column(name = "halfMother")
//    private boolean halfMotherExist;
//
//    @Column(name = "halfFather")
//    private boolean halfFatherExist;
//
//    @Column(name = "InLow")
//    private boolean inLowExist;
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "children-in-low",
//            joinColumns = @JoinColumn(name = "family_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"))
//    private Set<ShortFamilyMember> childrenInLow;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "family_guard",
            joinColumns = @JoinColumn(name = "family_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "guard_id", referencedColumnName = "id"))
    private Set<Guard> guard;

    @Column(name="family_name")
    private String familyName;

    @Column(name = "husband_info")
    private String husbandInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "husband")
    private ShortFamilyMember husband;

    @Column(name = "wife_info")
    private String wifeInfo;

    @ManyToOne(fetch = FetchType.LAZY)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Family family = (Family) o;
        return Objects.equals(id, family.id) && Objects.equals(uuid, family.uuid) && Objects.equals(guard, family.guard) && Objects.equals(husbandInfo, family.husbandInfo) && Objects.equals(husband, family.husband) && Objects.equals(wifeInfo, family.wifeInfo) && Objects.equals(wife, family.wife) && Objects.equals(activeGuard, family.activeGuard) && Objects.equals(birthday, family.birthday) && Objects.equals(deathDay, family.deathDay) && Objects.equals(description, family.description) && secretLevelPhoto == family.secretLevelPhoto && secretLevelEdit == family.secretLevelEdit && secretLevelRemove == family.secretLevelRemove && secretLevelGet == family.secretLevelGet;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
