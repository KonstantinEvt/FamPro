package com.example.service;

import com.example.dtos.FamilyMemberDto;
import com.example.entity.Family;
import com.example.entity.GlobalFamily;
import com.example.entity.ShortFamilyMember;
import com.example.entity.ShortFamilyMemberInfo;
import com.example.enums.CheckStatus;
import com.example.enums.Sex;
import com.example.mappers.FamilyMemberInfoMapper;
import com.example.mappers.FamilyMemberMapper;
import com.example.repository.FamilyMemberInfoRepo;
import com.example.repository.ShortMemberRepo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
@Getter
@Setter
public class MemberService {
    ShortMemberRepo shortMemberRepo;
    FamilyMemberInfoRepo familyMemberInfoRepo;
    FamilyMemberMapper familyMemberMapper;
    FamilyMemberInfoMapper familyMemberInfoMapper;

    @Transactional
    public ShortFamilyMember addFamilyMember(FamilyMemberDto dto) {
        ShortFamilyMember familyMember = familyMemberMapper.dtoToEntity(dto);
        if (dto.getMemberInfo() != null)
            familyMember.setShortFamilyMemberInfo(familyMemberInfoMapper.dtoToEntity(dto.getMemberInfo()));
        familyMember.setFamilies(new HashSet<>());
        familyMember.setChilds(new HashSet<>());
        familyMember.setFamilyWhereChildInLow(new HashSet<>());
        familyMember.setFamilyWhereHalfChildByFather(new HashSet<>());
        familyMember.setFamilyWhereHalfChildByMother(new HashSet<>());
        return shortMemberRepo.save(familyMember);
    }

    @Transactional
    public void editFamilyMember(FamilyMemberDto dto, ShortFamilyMember familyMember) {
        if (familyMember.getUuid() != dto.getUuid()) {
            if (!familyMember.getFirstName().equals(dto.getFirstName())) familyMember.setFirstName(dto.getFirstName());
            if (!familyMember.getMiddleName().equals(dto.getMiddleName()))
                familyMember.setMiddleName(dto.getMiddleName());
            if (!familyMember.getLastName().equals(dto.getLastName())) familyMember.setLastName(dto.getLastName());
            if (!familyMember.getBirthday().equals(dto.getBirthday())) familyMember.setBirthday(dto.getBirthday());
            familyMember.setUuid(dto.getUuid());
        }

        if (!familyMember.getCreator().equals(dto.getCreator())) familyMember.setCreator(dto.getCreator());
        if (familyMember.getCheckStatus() != dto.getCheckStatus()) familyMember.setCheckStatus(dto.getCheckStatus());
        if (dto.getDeathday() != null && (familyMember.getDeathday() == null || !familyMember.getDeathday().equals(dto.getDeathday())))
            familyMember.setDeathday(dto.getDeathday());
        if (dto.getFatherInfo() != null && (familyMember.getFatherInfo() == null || !familyMember.getFatherInfo().equals(dto.getFatherInfo())))
            familyMember.setFatherInfo(dto.getFatherInfo());
        if (dto.getMotherInfo() != null && (familyMember.getMotherInfo() == null || !familyMember.getMotherInfo().equals(dto.getMotherInfo())))
            familyMember.setMotherInfo(dto.getMotherInfo());
        if (dto.getMemberInfo() != null)
            familyMember.setShortFamilyMemberInfo(familyMemberInfoMapper.dtoToEntity(dto.getMemberInfo()));
        shortMemberRepo.save(familyMember);
    }

    @Transactional
    public void addChildToFamilyMember(ShortFamilyMember parent, ShortFamilyMember child) {
        if (parent.getSex() == Sex.MALE) {
            child.setFather(parent);
            child.setFatherInfo(parent.getFullName());
        } else {
            child.setMother(parent);
            child.setMotherInfo(parent.getFullName());
        }
        if (parent.getChilds() == null) parent.setChilds(new HashSet<>());
        parent.getChilds().add(child);
    }
}
