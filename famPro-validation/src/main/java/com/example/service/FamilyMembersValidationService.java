package com.example.service;

import com.example.checks.CheckFamilyMember;
import com.example.dtos.FamilyMemberDto;
import com.example.feign.FamilyMemberClient;
import com.example.transcripters.TranscriterHolder;
import com.example.transcripters.TranscritFamilyMember;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FamilyMembersValidationService {
    private final FamilyMemberClient familyMemberClient;
    private final CheckFamilyMember checkFamilyMember;
    private final TokenService tokenService;
    private final TranscriterHolder transcriterHolder;
    private final TranscritFamilyMember transcritFamilyMember;

    public FamilyMemberDto getFamilyMember(FamilyMemberDto familyMemberDto) {
        transcriterHolder.setTranscriter(familyMemberDto.getLocalisation());
        return transcritFamilyMember.to(familyMemberClient.getFamilyMember(transcritFamilyMember.from(checkFamilyMember.check(familyMemberDto))));
    }

    public FamilyMemberDto getFamilyMemberById(Long id) {
        return familyMemberClient.getFamilyMemberById(id);

    }

    public FamilyMemberDto addFamilyMember(FamilyMemberDto familyMemberDto) {
        transcriterHolder.setTranscriter(familyMemberDto.getLocalisation());
        System.out.println(familyMemberDto.getFirstName());
        System.out.println(familyMemberDto.getMiddleName());
        System.out.println(familyMemberDto.getLastName());
        System.out.println(familyMemberDto.getBirthday());
        return familyMemberClient.addFamilyMember(transcritFamilyMember.from(checkFamilyMember.check(familyMemberDto)));

    }

    public FamilyMemberDto editFamilyMember(FamilyMemberDto familyMemberDto) {
        transcriterHolder.setTranscriter(familyMemberDto.getLocalisation());
        System.out.println(familyMemberDto.getFirstName());
        System.out.println(familyMemberDto.getMiddleName());
        System.out.println(familyMemberDto.getLastName());
        System.out.println(familyMemberDto.getBirthday());
        return familyMemberClient.editFamilyMember(transcritFamilyMember.from(checkFamilyMember.check(familyMemberDto)));

    }
}