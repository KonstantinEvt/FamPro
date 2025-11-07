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

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder
@ToString
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@NamedEntityGraphs(
//        {@NamedEntityGraph(name = "ListForSave",
//                attributeNodes = {
//                        @NamedAttributeNode("mother"),
//                        @NamedAttributeNode("father"),
//                        @NamedAttributeNode(value = "familyMemberInfo", subgraph = "links")},
//                subgraphs = {@NamedSubgraph(name = "links",
//                        attributeNodes = {
//                                @NamedAttributeNode("phonesSet"),
//                                @NamedAttributeNode("emailsSet"),
//                                @NamedAttributeNode("addressesSet")})}),
//                @NamedEntityGraph(name = "WithoutParents")})

@Table(name = "family_members")
public class FamilyMember extends Fio {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genSeqFamMem")
    @SequenceGenerator(
            name = "genSeqFamMem",
            sequenceName = "fam_mem", initialValue = 1, allocationSize = 20)
    private Long id;

    @Column(name = "mother_info")
    private String motherInfo;

    @Column(name = "father_info")
    private String fatherInfo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mother_id")
    private FamilyMember mother;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "father_id")
    private FamilyMember father;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "member_id")
    private List<FamilyMemberInfo> familyMemberInfo;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private Set<OldFio> otherNames;

    @Column(name = "other_names")
    private boolean otherNamesExist;

    @Enumerated(EnumType.STRING)
    @Column(name = "check_status")
    private CheckStatus checkStatus;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "parents_childs",
            joinColumns = @JoinColumn(name = "parent_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "child_id", referencedColumnName = "id"))
    private Set<FamilyMember> childs;

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
    @Column(name = "security_main")
    private SecretLevel secretLevelMainInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "security_remove")
    private SecretLevel secretLevelRemove;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FamilyMember that = (FamilyMember) o;
        return Objects.equals(id, that.id) && Objects.equals(super.getUuid(), that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

