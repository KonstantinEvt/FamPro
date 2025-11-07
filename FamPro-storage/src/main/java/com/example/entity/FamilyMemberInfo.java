package com.example.entity;

import com.example.enums.SecretLevel;
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
@Cacheable
@Table(name = "members_info")
public class FamilyMemberInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MemberInfoSeq")
    @SequenceGenerator(name = "MemberInfoSeq",
            sequenceName = "fam_info", initialValue = 1, allocationSize = 20
    )
    private Long id;
    @Column(name = "UUID")
    private UUID uuid;

    @Column(name = "main_email")
    private String mainEmail;

    @Column(name = "secret_main_email")
    @Enumerated(EnumType.STRING)
    private SecretLevel secretLevelEmail;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "emails_of_family_member",
            joinColumns = @JoinColumn(name = "member_info_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "email_id", referencedColumnName = "id"))
    private Set<Email> emailsSet;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "biometric_of_family_member",
            joinColumns = @JoinColumn(name = "member_info_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "biometric_id", referencedColumnName = "id"))
    private List<Biometric> biometricData;

    @Column(name = "secret_biometric")
    @Enumerated(EnumType.STRING)
    private SecretLevel secretLevelBiometric;

    @Column(name = "main_phone")
    private String mainPhone;

    @Column(name = "secret_main_phone")
    @Enumerated(EnumType.STRING)
    private SecretLevel secretLevelPhone;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "phones_Of_family_member",
            joinColumns = @JoinColumn(name = "member_info_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "phone_id", referencedColumnName = "id"))
    private Set<Phone> phonesSet;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "description_Of_family_member",
            joinColumns = @JoinColumn(name = "member_info_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "description_id", referencedColumnName = "id"))
    private List<Description> descriptionData;

    @Column(name = "secret_description")
    @Enumerated(EnumType.STRING)
    private SecretLevel secretLevelDescription;

    @Column(name = "main_address")
    private String mainAddress;

    @Column(name = "secret_main_address")
    @Enumerated(EnumType.STRING)
    private SecretLevel secretLevelAddress;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "addresses_Of_family_member",
            joinColumns = @JoinColumn(name = "member_info_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "address_id", referencedColumnName = "id"))
    private Set<Address> addressesSet;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "birth_place_member",
            joinColumns = @JoinColumn(name = "member_info_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "birth_id", referencedColumnName = "id"))
    private List<PlaceBirth> birthPlace;

    @Enumerated(EnumType.STRING)
    @Column(name = "secret_birth")
    private SecretLevel secretLevelBirth;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "burial_place_member",
            joinColumns = @JoinColumn(name = "member_info_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "burial_id", referencedColumnName = "id"))
    private List<PlaceBurial> burialPlace;

    @Enumerated(EnumType.STRING)
    @Column(name = "secret_burial")
    private SecretLevel secretLevelBurial;

    @Column(name = "photo_birth")
    private boolean photoBirthExist;

    @Column(name = "photo_burial")
    private boolean photoBurialExist;

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
