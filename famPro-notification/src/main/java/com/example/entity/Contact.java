package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "owner")
    private Recipient owner;
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    private Recipient person;
    @Column(name = "contact_info")
    private String info;

}
