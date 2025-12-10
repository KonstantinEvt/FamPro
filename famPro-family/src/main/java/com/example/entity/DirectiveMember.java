package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
@AllArgsConstructor
@Builder
public class DirectiveMember {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directive_member")
    private ShortFamilyMember directiveMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directive")
    private DeferredDirective directive;
}
