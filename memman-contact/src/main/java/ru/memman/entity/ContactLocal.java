package ru.memman.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.memman.enums.Localisation;

import java.util.UUID;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ContactLocal {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact")
    private Contact contact;

    @Column(name = "origin")
    private boolean origin;

    @Column(name = "contact_name")
    private String name;

    @Column(name = "contact_info")
    private String info;

    @Column(name = "localisation")
    @Enumerated(EnumType.STRING)
    private Localisation localisation;
}
