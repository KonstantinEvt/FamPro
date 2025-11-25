package com.example.entity;

import com.example.enums.SwitchPosition;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
@AllArgsConstructor
@Builder
public class DeferredDirective {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="created")
    private Timestamp created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directive_family")
    private Family directiveFamily;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directive_member")
    private ShortFamilyMember directiveMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_member")
    private ShortFamilyMember shortFamilyMemberLink;

    @Column(name="info_link")
    private String info;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_family")
    private Family processFamily;

    @Column(name="tokenUser")
    private String tokenUser;

    @Enumerated(EnumType.STRING)
    private SwitchPosition switchPosition;
//    @Column(name="number_for")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "directive_global")
    private int globalFor;
//    @Column(name="number_to")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "link_global_family")
    private int globalTo;


}
