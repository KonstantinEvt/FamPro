package ru.memman.entity;

import ru.memman.enums.Localisation;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.memman.enums.SecretLevel;

import java.sql.Timestamp;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RealMen {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipientGen")
    @SequenceGenerator(name = "realManGen",
            sequenceName = "seq_real_man_gen", initialValue = 1, allocationSize = 5
    )
    private Long id;

    @Column(name = "extern_uuid")
    private String externUuid;

    @Column(name = "nick")
    private String nickName;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "owner", cascade = {CascadeType.REMOVE})
    private Set<Contact> contacts;

    @OneToMany(mappedBy = "person", cascade = {CascadeType.REMOVE})
    private Set<Contact> persons;

    @Column(name = "exist_prime")
    private boolean existPrimePhoto;

    @Transient
    private boolean online;

    @Column(name="link_extern_id")
    private String linkExternId;

    @Enumerated(EnumType.STRING)
    private Localisation localisation;

    @Column(name = "last_entering")
    private Timestamp lastOnline;

    @Column(name = "last_time_use")
    private Timestamp lastTimeUse;

    @Column(name = "update_time")
    private Timestamp lastUpdate;

    @Enumerated(EnumType.STRING)
    @Column(name = "security_phone")
    private SecretLevel secretLevelPhone;
    @Enumerated(EnumType.STRING)
    @Column(name = "security_photo")
    private SecretLevel secretLevelPhoto;

    @Enumerated(EnumType.STRING)
    @Column(name = "security_email")
    private SecretLevel secretLevelEmail;

    @Column(name = "main_phone")
    private String mainPhone;

    @Column(name = "main_email")
    private String mainEmail;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RealMen realMen = (RealMen) o;
        return existPrimePhoto == realMen.existPrimePhoto && Objects.equals(id, realMen.id) && Objects.equals(externUuid, realMen.externUuid) && Objects.equals(nickName, realMen.nickName) && Objects.equals(email, realMen.email) && Objects.equals(contacts, realMen.contacts)  && Objects.equals(linkExternId, realMen.linkExternId) && localisation == realMen.localisation && Objects.equals(lastOnline, realMen.lastOnline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
