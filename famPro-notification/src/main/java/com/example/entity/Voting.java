package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Builder
@EqualsAndHashCode
public class Voting {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    @Column(name="letter_externId")
    private String letter;
    @Column(name="guard_size")
    private int guardSize;
    @Column(name="rejects")
    private int rejects;
    @Column(name="accepts")
    private int accepts;
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "voting_recipient",
            joinColumns = @JoinColumn(name = "voting_id", referencedColumnName = "uuid"),
            inverseJoinColumns = @JoinColumn(name = "recipient_id", referencedColumnName = "id"))
    private Set<Recipient> recipients;
}
