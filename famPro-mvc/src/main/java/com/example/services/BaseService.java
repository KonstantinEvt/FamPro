package com.example.services;

import com.example.dtos.Directive;
import com.example.dtos.FamilyMemberDto;
import com.example.dtos.TokenUser;
import com.example.enums.KafkaOperation;
import com.example.enums.SwitchPosition;
import com.example.feign.FamilyClient;
import lombok.AllArgsConstructor;
import com.example.feign.BaseClient;
import com.example.feign.BaseOverClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
@AllArgsConstructor
public class BaseService {
    private final BaseClient baseClient;
    private final BaseOverClient baseOverClient;
    private final FamilyClient familyClient;
    private LinkedList<Directive> directives;

    public FamilyMemberDto getFamilyMember(FamilyMemberDto familyMemberDto) {
        return baseClient.getFamilyMember(familyMemberDto);
    }

    public FamilyMemberDto getFamilyMemberById(Long id, String localisation) {
        return baseClient.getFamilyMemberById(id, localisation);

    }

    public void addFamilyMember(FamilyMemberDto familyMemberDto) {
        FamilyMemberDto dto=baseClient.addFamilyMember(familyMemberDto);
        if (familyMemberDto.isPrimePhoto()) directives.add(new Directive(dto.getCreator(),dto.getUuid().toString(), SwitchPosition.PRIME_PHOTO, KafkaOperation.ADD));
    }

    public void editFamilyMember(FamilyMemberDto familyMemberDto) {
        baseClient.editFamilyMember(familyMemberDto);
    }

    public void linkFamilyMember(Long id) {
        familyClient.addGuard(baseOverClient.linkFamilyMember(id));
    }
}