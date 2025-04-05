package com.example.entity;

import com.example.enums.KafkaOperation;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    private Recipient owner;
    @ManyToOne(fetch = FetchType.LAZY)
    private Recipient person;
    @Column(name = "contact_name")
    private String name;
    @Column(name = "contact_info")
    private String info;
    @Column(name = "extern_id")
    private String externId;
    @Column(name = "contact_photo")
    private boolean contactPhoto;
    @Column(name = "prime_photo")
    private boolean primePhoto;
    @Column(name = "status")
    private KafkaOperation status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return contactPhoto == contact.contactPhoto && primePhoto == contact.primePhoto && Objects.equals(uuid, contact.uuid) && Objects.equals(owner, contact.owner) && Objects.equals(person, contact.person) && Objects.equals(name, contact.name) && Objects.equals(info, contact.info) && Objects.equals(externId, contact.externId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
