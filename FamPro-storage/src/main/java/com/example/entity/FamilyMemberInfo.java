package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
@Table(name = "members_info")
public class FamilyMemberInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MemberInfoSeq")
    @SequenceGenerator(name = "MemberInfoSeq",
            sequenceName = "FamInfo", initialValue = 1, allocationSize = 50
    )
    private Long id;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "email_id")
    private Email mainEmail;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "emailsOfFamilyMember")
    private List<Email> emails;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "phone_id")
    private Phone mainPhone;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "phonesOfFamilyMember")
    private List<Phone> phones;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id")
    private Address mainAddress;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "addressesOfFamilyMember")
    private List<Address> addresses;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FamilyMemberInfo that = (FamilyMemberInfo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
