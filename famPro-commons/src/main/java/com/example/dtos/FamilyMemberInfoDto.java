package com.example.dtos;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class FamilyMemberInfoDto {


    private Long id;

    private EmailDto mainEmail;

    private Set<EmailDto> emails;

    private PhoneDto mainPhone;

    private List<PhoneDto> phones;

    private AddressDto mainAddress;

    private List<AddressDto> addresses;
}
