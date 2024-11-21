package org.example.services;

import com.example.dtos.FamilyMemberDto;
import lombok.AllArgsConstructor;
import org.example.feign.BaseClient;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BaseService {
    private final BaseClient baseClient;

    public FamilyMemberDto getFamilyMember(FamilyMemberDto familyMemberDto) {
        FamilyMemberDto dto=baseClient.getFamilyMember(familyMemberDto);
        System.out.println(dto.getFirstName());
        System.out.println(dto.getMiddleName());
        System.out.println(dto.getLastName());
        System.out.println(dto.getBirthday());
        return dto;
    }

    public FamilyMemberDto getFamilyMemberById(Long id) {
        return baseClient.getFamilyMemberById(id);

    }
    public void addFamilyMember(FamilyMemberDto familyMemberDto) {
        System.out.println(familyMemberDto.getLocalisation());
        System.out.println(familyMemberDto.getFirstName());
        System.out.println(familyMemberDto.getMiddleName());
        System.out.println(familyMemberDto.getLastName());
        System.out.println(familyMemberDto.getBirthday());
        baseClient.addFamilyMember(familyMemberDto);

    }
    public void editFamilyMember(FamilyMemberDto familyMemberDto) {
        System.out.println(familyMemberDto.getLocalisation());
        System.out.println(familyMemberDto.getFirstName());
        System.out.println(familyMemberDto.getMiddleName());
        System.out.println(familyMemberDto.getLastName());
        System.out.println(familyMemberDto.getBirthday());
        baseClient.editFamilyMember(familyMemberDto);
}}