package com.example.entity;


import com.example.enums.CheckStatus;
import com.example.enums.SecretLevel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "short_family_members")
public class ShortFamilyMember extends Fio {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genSeqShortFamMem")
    @SequenceGenerator(
            name = "genSeqShortFamMem",
            sequenceName = "ShortFamMem", initialValue = 1, allocationSize = 20)
    private Long id;

    @Column(name = "mother_info")
    private String motherInfo;

    @Column(name = "mother_uuid")
    private UUID motherUuid;

    @Column(name = "father_info")
    private String fatherInfo;

    @Column(name = "father_uuid")
    private UUID fatherUuid;

    @Column(name = "ancestors")
    private String ancestors;

    @Column(name = "ancestorsGuard")
    private String ancestorsGuard;

    @Column(name = "linkGuard")
    private String linkGuard;

    @Column(name = "topAncestors")
    private String topAncestors;

    @Column(name = "descendants")
    private String descendants;

    @Column(name = "descendantsGuard")
    private String descendantsGuard;

    @Column(name = "activeMembers")
    private String activeMember;

    @Column(name = "activeGuard")
    private String activeGuard;

    @Column(name = "lowChildren")
    private String lowChildren;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "active_family")
    private Family activeFamily;


//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "mother_id")
//    private ShortFamilyMember mother;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "father_id")
//    private ShortFamilyMember father;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "info_id")
    private List<ShortFamilyMemberInfo> shortFamilyMemberInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "check_status")
    private CheckStatus checkStatus;


//    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinTable(name = "parents_childs_shorts",
//            joinColumns = @JoinColumn(name = "parent_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "child_id", referencedColumnName = "id"))
//    private Set<ShortFamilyMember> childs;

    @ManyToMany(mappedBy = "familyMembers")
    private Set<Family> families;

    @ManyToMany(mappedBy = "parents")
    private Set<Family> familyWhereParent;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "prime_family")
    private Family familyWhereChild;

    @ManyToMany(mappedBy = "halfChildrenByFather")
    private Set<Family> familyWhereHalfChildByFather;

    @ManyToMany(mappedBy = "halfChildrenByMother")
    private Set<Family> familyWhereHalfChildByMother;

    @ManyToMany(mappedBy = "childrenInLow")
    private Set<Family> familyWhereChildInLow;

    @Column(name = "death_day")
    private Date deathday;

    @Column(name = "creator")
    private String creator;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "update_time")
    private Timestamp lastUpdate;

    @Column(name = "prime_photo")
    private boolean primePhoto;

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
    @Column(name = "security_main")
    private SecretLevel secretLevelMainInfo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "linkedPerson")
    private List<Guard> linkedGuard;

    @Column(name = "burial_exist")
    private boolean burialExist;
    @Column(name = "birth_exist")
    private boolean birthExist;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShortFamilyMember that = (ShortFamilyMember) o;
        return Objects.equals(id, that.id) && Objects.equals(super.getUuid(), that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

