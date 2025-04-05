package com.example.service;

import com.example.checks.CheckFamilyMember;
import com.example.dtos.FamilyMemberDto;
import com.example.enums.Localisation;
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
    private final TranscritFamilyMember transcritFamilyMember;
    private final TokenService tokenService;

    public FamilyMemberDto getFamilyMember(FamilyMemberDto familyMemberDto) {
        TranscriterHolder transcriterHolder=new TranscriterHolder(tokenService.getTokenUser());
        transcriterHolder.setTranscriter(familyMemberDto);
        checkFamilyMember.check(transcriterHolder, familyMemberDto);
        if (familyMemberDto.getFirstName() == null
                || familyMemberDto.getMiddleName() == null
                || familyMemberDto.getLastName() == null
                || familyMemberDto.getBirthday() == null) throw new RuntimeException("Info not fully");
        transcritFamilyMember.from(transcriterHolder,familyMemberDto);
        FamilyMemberDto dto=familyMemberClient.getFamilyMember(familyMemberDto);
        dto.setLocalisation(familyMemberDto.getLocalisation());
        transcritFamilyMember.toGet(transcriterHolder,dto);
        return dto;
    }

    public FamilyMemberDto getFamilyMemberById(Long id, Localisation localisation) {
        FamilyMemberDto dto=familyMemberClient.getFamilyMemberById(id);
        dto.setLocalisation(localisation);
        TranscriterHolder transcriterHolder=new TranscriterHolder(tokenService.getTokenUser());
        transcriterHolder.setTranscriter(dto);
        transcritFamilyMember.to(transcriterHolder,dto);
        transcritFamilyMember.toGet(transcriterHolder,dto);
        System.out.println(dto);
        return dto;

    }

    public FamilyMemberDto addFamilyMember(FamilyMemberDto familyMemberDto) {
        System.out.println("к нам пришел:" + familyMemberDto);
        TranscriterHolder transcriterHolder=new TranscriterHolder(tokenService.getTokenUser());
        transcriterHolder.setTranscriter(familyMemberDto);
        System.out.println("переводчик установлен");
        checkFamilyMember.check(transcriterHolder, familyMemberDto);
        System.out.println("проверка пройдена");
        transcritFamilyMember.from(transcriterHolder,familyMemberDto);
        System.out.println("переводчик выполнил работу");
        FamilyMemberDto dto=familyMemberClient.addFamilyMember(familyMemberDto);
        System.out.println("пришел ответ с базы");
        System.out.println(dto);
        return dto;

    }

    public FamilyMemberDto editFamilyMember(FamilyMemberDto familyMemberDto) {
        TranscriterHolder transcriterHolder=new TranscriterHolder(tokenService.getTokenUser());
        transcriterHolder.setTranscriter(familyMemberDto);
        checkFamilyMember.check(transcriterHolder,familyMemberDto);
        transcritFamilyMember.from(transcriterHolder, familyMemberDto);
        familyMemberClient.editFamilyMember(familyMemberDto);
        return familyMemberDto;

    }
}