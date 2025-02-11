package com.example.entity;

import com.example.enums.SecretLevel;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
@Table(name = "short_members_info")
public class ShortFamilyMemberInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "info_gen")
    @SequenceGenerator(name = "info_gen",
            sequenceName = "seq_member_info", initialValue = 1, allocationSize = 20)
    private Long id;
    @Column(name = "UUID")
    private UUID uuid;

    @Column(name = "main_email")
    private String mainEmail;

    @Column(name = "secret_main_email")
    @Enumerated(EnumType.STRING)
    private SecretLevel secretLevelEmail;

    @Column(name = "main_phone")
    private String mainPhone;

    @Column(name = "secret_main_phone")
    @Enumerated(EnumType.STRING)
    private SecretLevel secretLevelPhone;

    @Column(name = "main_address")
    private String mainAddress;

    @Column(name = "secret_main_address")
    @Enumerated(EnumType.STRING)
    private SecretLevel secretLevelAddress;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShortFamilyMemberInfo that = (ShortFamilyMemberInfo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
