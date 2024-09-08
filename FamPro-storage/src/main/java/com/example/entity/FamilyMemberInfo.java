package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
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
@Table(name = "members_info")
public class FamilyMemberInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MemberInfoSeq")
    @SequenceGenerator(name = "MemberInfoSeq",
            sequenceName = "FamInfo", initialValue = 1, allocationSize = 20
    )
    private Long id;
    @Column(name = "UUID", unique = true)
    private UUID uuid;

    @Column(name = "main_email")
    private String mainEmail;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "emailsOfFamilyMember",
            joinColumns = @JoinColumn(name = "member_info_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "email_id", referencedColumnName = "id"))
    private Set<Email> emails;

    @Column(name = "main_phone")
    private String mainPhone;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "phonesOfFamilyMember",
            joinColumns = @JoinColumn(name = "member_info_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "phone_id", referencedColumnName = "id"))
    private Set<Phone> phones;


    @Column(name = "main_address")
    private String mainAddress;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "addressesOfFamilyMember",
            joinColumns = @JoinColumn(name = "member_info_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "address_id", referencedColumnName = "id"))
    private Set<Address> addresses;

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
