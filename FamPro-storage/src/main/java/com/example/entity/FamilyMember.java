package com.example.entity;


import com.example.enums.CheckStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.sql.Date;
import java.sql.Timestamp;
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
@NamedEntityGraphs(
        {@NamedEntityGraph(name = "ListForSave",
                attributeNodes = {
                        @NamedAttributeNode("mother"),
                        @NamedAttributeNode("father"),
                        @NamedAttributeNode(value = "familyMemberInfo", subgraph = "links")},
                subgraphs = {@NamedSubgraph(name = "links",
                        attributeNodes = {
                                @NamedAttributeNode("phones"),
                                @NamedAttributeNode("emails"),
                                @NamedAttributeNode("addresses")})}),
                @NamedEntityGraph(name = "WithoutParents")})

@Table(name = "family_members")
public class FamilyMember extends Fio {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genSeqFamMem")
    @SequenceGenerator(
            name = "genSeqFamMem",
            sequenceName = "FamMem", initialValue = 1, allocationSize = 20)
    private Long id;
    //    @Column(name = "Name", length = 20)
//    private String firstName;
//    @Column(name = "Fathername", length = 50)
//    private String middleName;
//    @Column(name = "Familiya", length = 50)
//    private String lastName;
//    @Column(name = "Birthday")
//    private Date birthday;
//    @Column(name = "UUID")
//    private UUID uuid;
//    @Enumerated(EnumType.STRING)
//    @Column(name = "sex")
//    private Sex sex;
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
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "info_id")
    private FamilyMemberInfo familyMemberInfo;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<OldFio> otherNames;
    @Enumerated(EnumType.STRING)
    @Column(name = "check_status")
    private CheckStatus checkStatus;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "parents_childs",
            joinColumns = @JoinColumn(name = "parent_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "child_id", referencedColumnName = "id"))
    private Set<FamilyMember> childs;

    @ManyToMany(mappedBy = "parents")
    private Set<Family> familyWhereParent;

    @ManyToMany(mappedBy = "children")
    private Set<Family> familyWhereChild;

    @Column(name = "death_day")
    private Date deathday;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "burial_id")
    private PlaceBurial burial;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "birth_id")
    private PlaceBirth birth;

    @Column(name = "creator")
    private String creator;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "prime_photo")
    private boolean primePhoto;

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

