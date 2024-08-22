package com.family.fampro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
//@NamedEntityGraphs({@NamedEntityGraph(name = "ListOfOne",
//        attributeNodes = {@NamedAttributeNode("mother"),
//                @NamedAttributeNode("father")}),
//        @NamedEntityGraph(name = "WithoutParents")})
@Table(name = "FamilyMembers")
public class FamilyMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "Name", length = 20)
    private String firstname;
    @Column(name = "Familiya", length = 20)
    private String lastname;
    @Column(name = "Fathername", length = 100)
    private String middlename;
    @Column(name="sex")
    private Boolean sex;
    @Column(name = "Birthday")
    private Date birthday;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "mother")
    private FamilyMember mother;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "father")
    private FamilyMember father;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FamilyMember that = (FamilyMember) o;
        return Objects.equals(id, that.id) && Objects.equals(firstname, that.firstname) && Objects.equals(lastname, that.lastname) && Objects.equals(middlename, that.middlename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

