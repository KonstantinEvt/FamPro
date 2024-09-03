package com.example.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class FamilyMemberInfoDto {


    private Long id;
    @Schema(description ="Уникальный идентификатор члена семьи")
    private UUID uuid;

    private EmailDto mainEmail;

    private Set<EmailDto> emails;

    private PhoneDto mainPhone;

    private Set<PhoneDto> phones;

    private AddressDto mainAddress;

    private Set<AddressDto> addresses;
}
