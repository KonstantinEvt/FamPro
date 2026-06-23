package com.example.service;

import com.example.checks.CheckFamilyMember;
import com.example.dtos.FamilyMemberDto;
import com.example.dtos.SecurityDto;
import com.example.enums.Localisation;
import com.example.enums.SecretLevel;
import com.example.feign.FamilyMemberClient;
import com.example.feign.MembersClient;
import com.example.holders.TranscriptHolder;
import com.example.transcriters.AbstractTranscripter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Collection;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FamilyMembersValidationService {
    private final MembersClient membersClient;
    private final FamilyMemberClient familyMemberClient;
    private final CheckFamilyMember checkFamilyMember;
    private final TranscritFamilyMember transcritFamilyMember;
    private final TokenService tokenService;
    private final TranscriptHolder transcriptHolder;

    public FamilyMemberDto getFamilyMember(FamilyMemberDto familyMemberDto) throws ParseException {

        AbstractTranscripter transcripter = transcriptHolder.getTranscript(familyMemberDto,familyMemberDto.getLocalisation().name());
        checkFamilyMember.check(transcripter, familyMemberDto);
        if (familyMemberDto.getUuid()==null &&
         (familyMemberDto.getFirstName() == null
                || familyMemberDto.getMiddleName() == null
                || familyMemberDto.getLastName() == null
                || familyMemberDto.getBirthday() == null)) throw new RuntimeException("Info not fully");
        if (familyMemberDto.getUuid()==null) transcritFamilyMember.from(transcripter,familyMemberDto);
        FamilyMemberDto dto= membersClient.getFamilyMember(familyMemberDto);
        dto.setLocalisation(familyMemberDto.getLocalisation());
        transcritFamilyMember.to(transcripter,dto);
        transcritFamilyMember.toGet(transcripter,dto);
        return dto;
    }
public  FamilyMemberDto getExtendedInfoFamilyMember(SecurityDto securityDto) throws ParseException {
    FamilyMemberDto dto= membersClient.getExtendedInfoFamilyMember(securityDto);
    AbstractTranscripter transcripter=transcriptHolder.getTranscript(securityDto.getLocalisation());
    dto.setLocalisation(securityDto.getLocalisation());
    transcritFamilyMember.toGetInfo(transcripter,dto);
    transcritFamilyMember.toGetOtherNames(transcripter,dto);
    System.out.println(dto);
    return dto;
}
    public FamilyMemberDto getFamilyMemberById(Long id, Localisation localisation) throws ParseException {
        FamilyMemberDto dto= membersClient.getFamilyMemberById(id);
        dto.setLocalisation(localisation);
        AbstractTranscripter transcripter=transcriptHolder.getTranscript(localisation);

        transcritFamilyMember.to(transcripter,dto);
        transcritFamilyMember.toGet(transcripter,dto);
        System.out.println(dto);
        return dto;

    }
    public FamilyMemberDto getYourself(Localisation localisation) throws ParseException {
        FamilyMemberDto dto= membersClient.getYourself();
        dto.setLocalisation(localisation);
        AbstractTranscripter transcripter=transcriptHolder.getTranscript(localisation);
        transcritFamilyMember.to(transcripter,dto);
        transcritFamilyMember.toGet(transcripter,dto);
        System.out.println(dto);
        return dto;

    }
    public FamilyMemberDto addFamilyMember(FamilyMemberDto familyMemberDto) {
        System.out.println("к нам пришел:" + familyMemberDto);
        AbstractTranscripter transcripter=transcriptHolder.getTranscript(familyMemberDto, familyMemberDto.getLocalisation().name());


        System.out.println("переводчик установлен");
        checkFamilyMember.check(transcripter, familyMemberDto);
        System.out.println("проверка пройдена");
        transcritFamilyMember.from(transcripter,familyMemberDto);
        System.out.println("переводчик выполнил работу");
        FamilyMemberDto dto= membersClient.addFamilyMember(familyMemberDto);
        System.out.println("пришел ответ с базы");
        System.out.println(dto);
        return dto;

    }

    public FamilyMemberDto editFamilyMember(FamilyMemberDto familyMemberDto) {
        AbstractTranscripter transcripter=transcriptHolder.getTranscript(familyMemberDto, familyMemberDto.getLocalisation().name());
        checkFamilyMember.check(transcripter,familyMemberDto);
        transcritFamilyMember.from(transcripter, familyMemberDto);
        membersClient.editFamilyMember(familyMemberDto);
        return familyMemberDto;
    }
    public Collection<FamilyMemberDto> getMembersByFirstCreator(Localisation localisation){
        AbstractTranscripter transcripter=transcriptHolder.getTranscript(localisation);

        Collection<FamilyMemberDto> memberDtos=familyMemberClient.getMembersByFirstCreator();
        for (FamilyMemberDto member :
                memberDtos) {
                        checkFamilyMember.check(transcripter,member);
            transcritFamilyMember.transcritToFio(transcripter, member);
        }
        return memberDtos;
    }
    public Collection<FamilyMemberDto> getFamilyTreeOfMember(UUID uuid, SecretLevel choice, Localisation localisation) throws ParseException {
        AbstractTranscripter transcripter=transcriptHolder.getTranscript(localisation);

        Collection<FamilyMemberDto> memberDtos=familyMemberClient.getFamilyTreeOfMember(uuid, choice);
        for (FamilyMemberDto member :
                memberDtos) {
            member.setLocalisation(localisation);
            checkFamilyMember.check(transcripter,member);
            transcritFamilyMember.transcritToFio(transcripter, member);
            transcritFamilyMember.toGet(transcripter, member);
        }
        return memberDtos;
    }

}