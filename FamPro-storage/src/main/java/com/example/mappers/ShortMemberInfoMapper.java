package com.example.mappers;

import com.example.dtos.FamilyMemberInfoDto;
import com.example.entity.FamilyMemberInfo;
import org.springframework.stereotype.Component;

@Component
public class ShortMemberInfoMapper {
    public FamilyMemberInfoDto entityToDto(FamilyMemberInfo arg0) {
        if (arg0 == null) {
            return null;
        } else {
            FamilyMemberInfoDto familyMemberInfoDto = new FamilyMemberInfoDto();
            familyMemberInfoDto.setId(arg0.getId());
            familyMemberInfoDto.setUuid(arg0.getUuid());
            familyMemberInfoDto.setMainEmail(arg0.getMainEmail());
            familyMemberInfoDto.setSecretLevelEmail(arg0.getSecretLevelEmail());
            familyMemberInfoDto.setMainPhone(arg0.getMainPhone());
            familyMemberInfoDto.setSecretLevelPhone(arg0.getSecretLevelPhone());
            familyMemberInfoDto.setMainAddress(arg0.getMainAddress());
            familyMemberInfoDto.setSecretLevelAddress(arg0.getSecretLevelAddress());
            familyMemberInfoDto.setSecretLevelBiometric(arg0.getSecretLevelBiometric());
            return familyMemberInfoDto;
        }
    }
}
