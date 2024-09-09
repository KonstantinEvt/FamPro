package com.example.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class FamilyMemberInfoDto {

    private Long id;
    @Schema(description = "Уникальный идентификатор члена семьи")
    private UUID uuid;

    private String mainEmail;

    private Set<EmailDto> emails;

    private String mainPhone;

    private Set<PhoneDto> phones;

    private String mainAddress;

    private Set<AddressDto> addresses;

}
