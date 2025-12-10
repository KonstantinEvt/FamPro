package com.example.entity;

import com.example.enums.Localisation;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.lang.reflect.Array;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Recipient {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipientGen")
    @SequenceGenerator(name = "recipientGen",
            sequenceName = "seq_recipient_gen", initialValue = 1, allocationSize = 5
    )
    private Long id;

    @Column(name = "extern_uuid")
    private String externUuid;

    @Column(name = "nick")
    private String nickName;

    @OneToMany(mappedBy = "sendFrom")
    private List<AloneNew> sendingLetters;

    @ManyToMany(mappedBy = "sendTo")
    private List<AloneNew> receivedLetters;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "owner")
    private Set<Contact> contacts;

    @Column (name="read_common")
    private String commonReading;

    @Column (name="read_system")
    private String systemReading;

    @Column (name="exist_prime_Photo")
    private boolean urlPhoto;

    @Column(name="link_extern_id")
    private String linkExternId;

    @OneToMany(mappedBy = "person")
    private Set<Contact> podpisota;

    @Enumerated(EnumType.STRING)
    private Localisation localisation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipient recipient = (Recipient) o;
        return Objects.equals(id, recipient.id) && Objects.equals(externUuid, recipient.externUuid) && Objects.equals(nickName, recipient.nickName) && Objects.equals(sendingLetters, recipient.sendingLetters) && Objects.equals(receivedLetters, recipient.receivedLetters) && Objects.equals(email, recipient.email) && Objects.equals(contacts, recipient.contacts) && Objects.equals(commonReading, recipient.commonReading) && Objects.equals(systemReading, recipient.systemReading);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
