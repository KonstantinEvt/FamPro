package com.example.dtos;

import com.example.enums.SecretLevel;
import lombok.*;

import java.sql.Date;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class FamilyDto {
    private Long id;
    private UUID uuid;
    private Set<String> children;
    private Set<String> halfFather;
    private Set<String> halfMother;
    private Set<String> inLow;
    private Set<String> other;
    private String familyName;
    private String husbandInfo;
    private String wifeInfo;
    private String activeGuard;
    private Date birthday;
    private Date deathDay;
    private String description;
    private SecretLevel secretLevelPhoto;
    private SecretLevel secretLevelEdit;
    private SecretLevel secretLevelRemove;
    private SecretLevel secretLevelGet;
}
