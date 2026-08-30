package ru.memman.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
            sequenceName = "old_fio_seq", initialValue = 1, allocationSize = 5)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private FamilyMember member;


}
