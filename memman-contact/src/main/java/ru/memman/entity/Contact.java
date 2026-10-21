package ru.memman.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.memman.enums.Relation;
import ru.memman.enums.SecretLevel;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    private RealMen owner;

    @ManyToOne(fetch = FetchType.LAZY)
    private RealMen person;

    @OneToMany(mappedBy = "contact", cascade = {CascadeType.REMOVE, CascadeType.DETACH})
    private Set<ContactLocal> locals;

    @Column(name = "person_uuid")
    private String personUuid;

    @Column(name = "contact_photo")
    private boolean contactPhoto;

    @Column(name = "prefer_contact_photo")
    private boolean preferContact;

    @Column(name = "contact_name")
    private String name;

    @Column(name = "contact_info")
    private String info;

    @Column(name = "last_time_use")
    private Timestamp lastTimeUse;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation")
    private SecretLevel relation;

    @Enumerated(EnumType.STRING)
    @Column(name = "specify")
    private Relation specify;

    @Column(name = "origin")
    private boolean origin;

    @Transient
    private boolean change;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return contactPhoto == contact.contactPhoto && origin == contact.origin && Objects.equals(uuid, contact.uuid) && Objects.equals(personUuid, contact.personUuid) && Objects.equals(name, contact.name) && Objects.equals(info, contact.info) && Objects.equals(lastTimeUse, contact.lastTimeUse) && relation == contact.relation && specify == contact.specify;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
