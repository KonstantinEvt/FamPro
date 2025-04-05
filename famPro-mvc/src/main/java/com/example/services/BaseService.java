package com.example.services;

import com.example.dtos.Directive;
import com.example.dtos.FamilyMemberDto;
import com.example.enums.CheckStatus;
import com.example.enums.Localisation;
import com.example.feign.BaseClient;
import com.example.feign.BaseOverClient;
import com.example.feign.FamilyClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
@AllArgsConstructor
public class BaseService {
    private final BaseClient baseClient;
    private final BaseOverClient baseOverClient;
    private final FamilyClient familyClient;

    public FamilyMemberDto getFamilyMember(FamilyMemberDto familyMemberDto) {
        return baseClient.getFamilyMember(familyMemberDto);
    }

    public FamilyMemberDto getFamilyMemberById(Long id, Localisation localisation) {
        return baseClient.getFamilyMemberById(id, localisation);

    }

    public void addFamilyMember(FamilyMemberDto familyMemberDto) {
        FamilyMemberDto dto=baseClient.addFamilyMember(familyMemberDto);
     }

    public void editFamilyMember(FamilyMemberDto familyMemberDto) {
        baseClient.editFamilyMember(familyMemberDto);
    }

    public CheckStatus linkFamilyMember(FamilyMemberDto familyMemberDto) {
        return familyClient.addGuard(familyMemberDto);
    }
}