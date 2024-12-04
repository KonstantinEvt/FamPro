package org.example.services;

import com.example.dtos.FamilyMemberDto;
import lombok.AllArgsConstructor;
import org.example.feign.BaseClient;
import org.example.feign.BaseOverClient;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BaseService {
    private final BaseClient baseClient;
    private final BaseOverClient baseOverClient;

    public FamilyMemberDto getFamilyMember(FamilyMemberDto familyMemberDto) {
        return baseClient.getFamilyMember(familyMemberDto);
    }

    public FamilyMemberDto getFamilyMemberById(Long id, String localisation) {
        return baseClient.getFamilyMemberById(id, localisation);

    }

    public void addFamilyMember(FamilyMemberDto familyMemberDto) {
        baseClient.addFamilyMember(familyMemberDto);

    }

    public void editFamilyMember(FamilyMemberDto familyMemberDto) {
        baseClient.editFamilyMember(familyMemberDto);
    }

    public void linkFamilyMember(Long id) {
        baseOverClient.linkFamilyMember(id);
    }
}