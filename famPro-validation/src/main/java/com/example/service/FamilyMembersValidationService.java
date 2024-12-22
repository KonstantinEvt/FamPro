package com.example.service;

import com.example.checks.CheckFamilyMember;
import com.example.dtos.FamilyMemberDto;
import com.example.feign.FamilyMemberClient;
import com.example.transcriters.TranscriterHolder;
import com.example.transcriters.TranscritFamilyMember;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FamilyMembersValidationService {
    private final FamilyMemberClient familyMemberClient;
    private final CheckFamilyMember checkFamilyMember;
    private final TranscriterHolder transcriterHolder;
    private final TranscritFamilyMember transcritFamilyMember;

    public FamilyMemberDto getFamilyMember(FamilyMemberDto familyMemberDto) {
        transcriterHolder.setTranscriter(familyMemberDto);
        checkFamilyMember.check(familyMemberDto);
        if (familyMemberDto.getFirstName() == null
                || familyMemberDto.getMiddleName() == null
                || familyMemberDto.getLastName() == null
                || familyMemberDto.getBirthday() == null) throw new RuntimeException("Info not fully");
        transcritFamilyMember.from(familyMemberDto);
        FamilyMemberDto dto=familyMemberClient.getFamilyMember(familyMemberDto);
        dto.setLocalisation(familyMemberDto.getLocalisation());
        transcritFamilyMember.toGet(dto);
        return dto;
    }

    public FamilyMemberDto getFamilyMemberById(Long id,String localisation) {
        FamilyMemberDto dto=familyMemberClient.getFamilyMemberById(id);
        dto.setLocalisation(localisation);
        transcriterHolder.setTranscriter(dto);
        transcritFamilyMember.to(dto);
        transcritFamilyMember.toGet(dto);
        System.out.println(dto);
        return dto;

    }

    public FamilyMemberDto addFamilyMember(FamilyMemberDto familyMemberDto) {
        System.out.println("к нам пришел:" + familyMemberDto);
        transcriterHolder.setTranscriter(familyMemberDto);
        System.out.println("переводчик установлен");
        checkFamilyMember.check(familyMemberDto);
        System.out.println("проверка пройдена");
        transcritFamilyMember.from(familyMemberDto);
        System.out.println("переводчик выполнил работу");
        FamilyMemberDto dto=familyMemberClient.addFamilyMember(familyMemberDto);
        System.out.println("пришел ответ с базы");
        System.out.println(dto);
        return dto;

    }

    public FamilyMemberDto editFamilyMember(FamilyMemberDto familyMemberDto) {
        transcriterHolder.setTranscriter(familyMemberDto);
        checkFamilyMember.check(familyMemberDto);
        transcritFamilyMember.from(familyMemberDto);
        familyMemberClient.editFamilyMember(familyMemberDto);
        return familyMemberDto;

    }
}