package com.example.service;

import com.example.checks.CheckFamilyMember;
import com.example.dtos.FamilyMemberDto;
import com.example.dtos.SecurityDto;
import com.example.enums.Localisation;
import com.example.feign.FamilyMemberClient;
import com.example.holders.TranscriptHolder;
import com.example.transcriters.AbstractTranscripter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@AllArgsConstructor
public class FamilyMembersValidationService {
    private final FamilyMemberClient familyMemberClient;
    private final CheckFamilyMember checkFamilyMember;
    private final TranscritFamilyMember transcritFamilyMember;
    private final TokenService tokenService;
    private final TranscriptHolder transcriptHolder;

    public FamilyMemberDto getFamilyMember(FamilyMemberDto familyMemberDto) throws ParseException {

        AbstractTranscripter transcripter = transcriptHolder.getTranscript(familyMemberDto,(String) tokenService.getTokenUser().getClaims().get("localisation"));
        checkFamilyMember.check(transcripter, familyMemberDto);
        if (familyMemberDto.getFirstName() == null
                || familyMemberDto.getMiddleName() == null
                || familyMemberDto.getLastName() == null
                || familyMemberDto.getBirthday() == null) throw new RuntimeException("Info not fully");
        transcritFamilyMember.from(transcripter,familyMemberDto);
        FamilyMemberDto dto=familyMemberClient.getFamilyMember(familyMemberDto);
        dto.setLocalisation(familyMemberDto.getLocalisation());
        transcritFamilyMember.toGet(transcripter,dto);
        return dto;
    }
public  FamilyMemberDto getExtendedInfoFamilyMember(SecurityDto securityDto, Localisation localisation) throws ParseException {
    FamilyMemberDto dto=familyMemberClient.getExtendedInfoFamilyMember(securityDto);
    AbstractTranscripter transcripter=transcriptHolder.getTranscript(localisation);dto.setLocalisation(localisation);
    System.out.println(localisation);
    transcritFamilyMember.toGetInfo(transcripter,dto);
    transcritFamilyMember.toGetOtherNames(transcripter,dto);
    System.out.println(dto);
    return dto;
}
    public FamilyMemberDto getFamilyMemberById(Long id, Localisation localisation) throws ParseException {
        FamilyMemberDto dto=familyMemberClient.getFamilyMemberById(id);
        dto.setLocalisation(localisation);
        AbstractTranscripter transcripter=transcriptHolder.getTranscript(localisation);

        transcritFamilyMember.to(transcripter,dto);
        transcritFamilyMember.toGet(transcripter,dto);
        System.out.println(dto);
        return dto;

    }
    public FamilyMemberDto getYourself(Localisation localisation) throws ParseException {
        FamilyMemberDto dto=familyMemberClient.getYourself();
        dto.setLocalisation(localisation);
        AbstractTranscripter transcripter=transcriptHolder.getTranscript(localisation);
        transcritFamilyMember.to(transcripter,dto);
        transcritFamilyMember.toGet(transcripter,dto);
        System.out.println(dto);
        return dto;

    }
    public FamilyMemberDto addFamilyMember(FamilyMemberDto familyMemberDto) {
        System.out.println("к нам пришел:" + familyMemberDto);
        AbstractTranscripter transcripter=transcriptHolder.getTranscript(familyMemberDto, (String) tokenService.getTokenUser().getClaims().get("localisation"));


        System.out.println("переводчик установлен");
        checkFamilyMember.check(transcripter, familyMemberDto);
        System.out.println("проверка пройдена");
        transcritFamilyMember.from(transcripter,familyMemberDto);
        System.out.println("переводчик выполнил работу");
        FamilyMemberDto dto=familyMemberClient.addFamilyMember(familyMemberDto);
        System.out.println("пришел ответ с базы");
        System.out.println(dto);
        return dto;

    }

    public FamilyMemberDto editFamilyMember(FamilyMemberDto familyMemberDto) {
        AbstractTranscripter transcripter=transcriptHolder.getTranscript(familyMemberDto, (String) tokenService.getTokenUser().getClaims().get("localisation"));
        checkFamilyMember.check(transcripter,familyMemberDto);
        transcritFamilyMember.from(transcripter, familyMemberDto);
        familyMemberClient.editFamilyMember(familyMemberDto);
        return familyMemberDto;
    }
}