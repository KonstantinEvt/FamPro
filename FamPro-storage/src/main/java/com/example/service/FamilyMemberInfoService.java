package com.example.service;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.FamilyMemberInfoDto;
import com.example.entity.FamilyMember;
import com.example.entity.FamilyMemberInfo;
import com.example.mappers.FamilyMemberInfoMapper;
import com.example.repository.FamilyMemberInfoRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class FamilyMemberInfoService {
    private final FamilyMemberInfoMapper familyMemberInfoMapper;
    private final FamilyMemberInfoRepo familyMemberInfoRepo;
    private final EmailService emailService;
    private final PhoneService phoneService;
    private final AddressService addressService;

    public FamilyMemberInfo merge(FamilyMemberDto familyMemberDto, UUID oldUUID) {
        FamilyMemberInfo fmi = familyMemberInfoMapper.dtoToEntity(familyMemberDto.getMemberInfo());
        FamilyMemberInfo fmiFromBase;
        if (familyMemberDto.getUuid() != oldUUID)
            fmiFromBase = Optional.of(familyMemberInfoRepo.findFamilyMemberInfoByUuid(oldUUID)).get().orElse(new FamilyMemberInfo());
        else fmiFromBase = new FamilyMemberInfo();
        fmi.setId(fmiFromBase.getId());
        fmi.setUuid(familyMemberDto.getUuid());
// Установка и проверка Emails
        if (fmi.getMainEmail() != null || fmi.getEmails() != null ||  fmiFromBase.getMainEmail() != null || fmiFromBase.getEmails() != null) {
            emailService.checkMergeAndSetUp(fmi, fmiFromBase);
        }
// Установка и проверка телефонов
        if ( fmi.getMainPhone() != null||fmi.getPhones() != null ||  fmiFromBase.getMainPhone() != null || fmiFromBase.getPhones() != null ){
            phoneService.checkMergeAndSetUp(fmi, fmiFromBase);
        }
// Установка и проверка адресов
        if ( fmi.getMainAddress() != null||fmi.getAddresses() != null ||  fmiFromBase.getMainAddress() != null || fmiFromBase.getAddresses() != null ){
            addressService.checkMergeAndSetUp(fmi, fmiFromBase);
        }
        return fmi;
    }
    public FamilyMemberInfoDto getMemberInfo(FamilyMember familyMember){return familyMemberInfoMapper.entityToDto(familyMember.getFamilyMemberInfo());}
}