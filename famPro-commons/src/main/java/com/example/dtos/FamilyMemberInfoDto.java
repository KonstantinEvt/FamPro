package com.example.dtos;

import com.example.enums.SecretLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@ToString
public class FamilyMemberInfoDto {

    private Long id;
    @Schema(description = "Уникальный идентификатор члена семьи")
    private UUID uuid;

    private String mainEmail;
    private SecretLevel secretLevelEmail;
    private Set<EmailDto> emails;

    private String mainPhone;
    private SecretLevel secretLevelPhone;
    private Set<PhoneDto> phones;

    private String mainAddress;
    private SecretLevel secretLevelAddress;
    private Set<AddressDto> addresses;

    private BiometricDto biometric;
    private SecretLevel secretLevelBiometric;

    private BurialDto burial;
    private SecretLevel secretLevelBurial;

    private BirthDto birth;
    private SecretLevel secretLevelBirth;

    private DescriptionDto description;
    private SecretLevel secretLevelDescription;

    private boolean photoBirthExist;

    private boolean photoBurialExist;
}
