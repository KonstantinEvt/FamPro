package ru.memman.entity;

import ru.memman.enums.Localisation;
import ru.memman.enums.SecretLevel;
import ru.memman.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
@Table(name = "user_online")
public class BaseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userOnlineGen")
    @SequenceGenerator(name = "userOnlineGen",
            sequenceName = "seq_user_gen", initialValue = 1, allocationSize = 5
    )
    private Long id;

    @Column(name = "extern_uuid")
    private String externUuid;

    @Column(name = "nick")
    private String nickName;

    @Column(name = "logName")
    private String logName;

    @Column(name = "fullName")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "exist_prime_Photo")
    private boolean primePhoto;

    @Column(name = "update_time")
    private Timestamp lastUpdate;

    @Enumerated(EnumType.STRING)
    @Column(name = "security_photo")
    private SecretLevel secretLevelPhoto;

    @Column(name = "link_extern_id")
    private String linkExternId;

    @Enumerated(EnumType.STRING)
    private Localisation localisation;

    @Enumerated(EnumType.STRING)
    private UserRole priorityRole;

    @Column(name = "lastEntering")
    private Timestamp lastOnline;

    @Enumerated(EnumType.STRING)
    @Column(name = "security_phone")
    private SecretLevel secretLevelPhone;

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
        BaseUser baseUser = (BaseUser) o;
        return primePhoto == baseUser.primePhoto && Objects.equals(id, baseUser.id) && Objects.equals(externUuid, baseUser.externUuid) && Objects.equals(nickName, baseUser.nickName) && Objects.equals(email, baseUser.email) && Objects.equals(linkExternId, baseUser.linkExternId) && localisation == baseUser.localisation && priorityRole == baseUser.priorityRole && Objects.equals(lastOnline, baseUser.lastOnline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
