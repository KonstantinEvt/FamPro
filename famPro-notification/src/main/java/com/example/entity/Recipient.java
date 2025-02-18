package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class Recipient {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipientGen")
    @SequenceGenerator(name = "recipientGen",
            sequenceName = "seq_recipient_gen", initialValue = 1, allocationSize = 5
    )
    private Long id;
    @Column(name = "extern_id")
    private String externId;
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
}
