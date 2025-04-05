package com.example.entity;

import com.example.enums.CheckStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Cacheable
@Table(name = "old_fio")
public class OldFio extends Fio {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genSeqOldFio")
    @SequenceGenerator(
            name = "genSeqOldFio",
            sequenceName = "oldFioSeq", initialValue = 1, allocationSize = 5)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "member_id")
    private FamilyMember member;
//    @Enumerated(EnumType.STRING)
//    @Column(name = "check_status")
//    private CheckStatus checkStatus;


}
