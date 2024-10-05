package com.example.entity;


import com.example.dtos.FioDto;
import com.example.enums.Sex;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
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
public class FamilyMember {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genSeqFamMem")
    @SequenceGenerator(
            name = "genSeqFamMem",
            sequenceName = "FamMem", initialValue = 1, allocationSize = 20)
    private Long id;
    @Column(name = "UUID", unique = true)
    private UUID uuid;
    @Column(name = "Name", length = 20)
    private String firstName;
    @Column(name = "Fathername", length = 50)
    private String middleName;
    @Column(name = "Familiya", length = 50)
    private String lastName;
    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;
    @Column(name = "Birthday")
    private Date birthday;
    @Column(name = "Death_Day")
    private Date deathday;
    @Column(name = "father_info")
    private String fatherInfo;
    @Column(name = "mother_info")
    private String motherInfo;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="member_id")
    private Set<Fio> oldNames;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "info_id")
    private FamilyMemberInfo familyMemberInfo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mother_id")
    private FamilyMember mother;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "father_id")
    private FamilyMember father;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "parents_childs",
            joinColumns = @JoinColumn(name = "parent_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "child_id", referencedColumnName = "id"))
    private Set<FamilyMember> childs;

    @ManyToMany(mappedBy = "parents")
    private Set<Family> familyWhereParent;

    @ManyToMany(mappedBy = "children")
    private Set<Family> familyWhereChild;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FamilyMember that = (FamilyMember) o;
        return Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(middleName, that.middleName) && sex == that.sex && Objects.equals(birthday, that.birthday) && Objects.equals(deathday, that.deathday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

