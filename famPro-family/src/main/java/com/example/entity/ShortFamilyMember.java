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

    @Column(name = "ancestors",length = 4095)
    private String ancestors;

    @Column(name = "ancestorsGuard",length = 4095)
    private String ancestorsGuard;

    @Column(name = "linkGuard")
    private String linkGuard;

    @Column(name = "topAncestors",length = 4095)
    private String topAncestors;

    @Column(name = "descendants",length = 65535)
    private String descendants;

    @Column(name = "descendantsGuard",length = 65535)
    private String descendantsGuard;

    @Column(name = "activeMembers",length = 4095)
    private String activeMember;

    @Column(name = "activeGuard",length = 4095)
    private String activeGuard;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "active_family")
    private Family activeFamily;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "info_id")
    private List<ShortFamilyMemberInfo> shortFamilyMemberInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "check_status")
    private CheckStatus checkStatus;

    @OneToMany(mappedBy = "directiveMember", cascade = {CascadeType.REMOVE})
    private List<DirectiveMember> directiveMembers;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private Set<FamilyMemberLink> familyLinks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prime_family")
    private Family familyWhereChild;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "security_birthday")
    private SecretLevel secretLevelBirthday;

    @Column(name = "burial_exist")
    private boolean burialExist;
    @Column(name = "birth_exist")
    private boolean birthExist;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShortFamilyMember that = (ShortFamilyMember) o;
        return primePhoto == that.primePhoto && burialExist == that.burialExist && birthExist == that.birthExist && Objects.equals(id, that.id) && Objects.equals(motherInfo, that.motherInfo) && Objects.equals(motherUuid, that.motherUuid) && Objects.equals(fatherInfo, that.fatherInfo) && Objects.equals(fatherUuid, that.fatherUuid) && Objects.equals(ancestors, that.ancestors) && Objects.equals(ancestorsGuard, that.ancestorsGuard) && Objects.equals(linkGuard, that.linkGuard) && Objects.equals(topAncestors, that.topAncestors) && Objects.equals(descendants, that.descendants) && Objects.equals(descendantsGuard, that.descendantsGuard) && Objects.equals(activeMember, that.activeMember) && Objects.equals(activeGuard, that.activeGuard) && checkStatus == that.checkStatus && Objects.equals(deathday, that.deathday) && Objects.equals(creator, that.creator) && Objects.equals(createTime, that.createTime) && Objects.equals(lastUpdate, that.lastUpdate) && secretLevelPhoto == that.secretLevelPhoto && secretLevelEdit == that.secretLevelEdit && secretLevelRemove == that.secretLevelRemove && secretLevelMainInfo == that.secretLevelMainInfo && secretLevelBirthday == that.secretLevelBirthday;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

