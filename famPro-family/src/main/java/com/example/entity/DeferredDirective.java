package com.example.entity;

import com.example.enums.Localisation;
import com.example.enums.SwitchPosition;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Set;
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
    private UUID uuid;

    @Column(name = "created")
    private Timestamp created;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "directive_family")
//    private Family directiveFamily;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directive_member")
    private ShortFamilyMember directiveMember;

    @OneToMany(mappedBy = "directive",cascade = {CascadeType.REMOVE})
    private Set<DirectiveMember> shortFamilyMemberLink;

    @Column(name = "info_link")
    private String info;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "process_family")
//    private Family processFamily;

    @Column(name = "tokenUser")
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

    @Enumerated(EnumType.STRING)
    private Localisation localisation;


}
