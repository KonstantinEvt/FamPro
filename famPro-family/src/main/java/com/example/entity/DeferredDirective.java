package com.example.entity;

import com.example.dtos.FamilyMemberDto;
import com.example.enums.SwitchPosition;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
@AllArgsConstructor
@Builder
public class DeferredDirective {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "directiveGen")
    @SequenceGenerator(name = "directiveGen",
            sequenceName = "seqDirective", initialValue = 1)
    private Long id;
    @Column(name="created")
    private Timestamp created;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directive_family")
    private Family directiveFamily;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directive_member")
    private ShortFamilyMember directiveMember;
    @Column(name="ExternId")
    private String enternId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_family")
    private Family processFamily;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directive_guard")
    private Guard directiveGuard;
    @Enumerated(EnumType.STRING)
    private SwitchPosition switchPosition;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directive_global")
    private GlobalFamily globalFor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_family")
    private GlobalFamily globalTo;


}
