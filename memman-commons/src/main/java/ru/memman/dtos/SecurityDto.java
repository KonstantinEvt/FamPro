package ru.memman.dtos;

import ru.memman.enums.Localisation;
import ru.memman.enums.SecretLevel;
import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class SecurityDto {
    private String owner;
    private Long personId;
    private String personUuid;
    private boolean infoExist;
    private boolean otherNamesExist;
    private Timestamp lastUpdate;
    private SecretLevel secretLevelEdit;
    private SecretLevel secretLevelPhone;
    private SecretLevel secretLevelBiometric;
    private SecretLevel secretLevelDescription;
    private SecretLevel secretLevelEmail;
    private SecretLevel secretLevelAddress;
    private SecretLevel secretLevelBurial;
    private SecretLevel secretLevelBirth;
    private Localisation localisation;
}
