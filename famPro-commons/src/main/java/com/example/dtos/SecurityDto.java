package com.example.dtos;

import com.example.enums.SecretLevel;
import lombok.*;

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
    private SecretLevel secretLevelEdit;
    private SecretLevel secretLevelPhone;
    private SecretLevel secretLevelBiometric;
    private SecretLevel secretLevelEmail;
    private SecretLevel secretLevelAddress;
    private SecretLevel secretLevelBurial;
    private SecretLevel secretLevelBirth;
}
