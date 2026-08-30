package ru.memman.services;

import ru.memman.dtos.Directive;
import ru.memman.dtos.FamilyMemberDto;
import ru.memman.dtos.SecurityDto;
import ru.memman.enums.CheckStatus;
import ru.memman.enums.Localisation;
import ru.memman.enums.SecretLevel;
import ru.memman.feign.BaseClient;
import ru.memman.feign.BaseOverClient;
import ru.memman.feign.FamilyClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

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

    public FamilyMemberDto getYourself(Localisation localisation) {
        return baseClient.getYourself(localisation);
    }

    public FamilyMemberDto getExtendedInfoFamilyMember(SecurityDto securityDto) {
        return baseClient.getExtendedInfoFamilyMember(securityDto);
    }

    public long addFamilyMember(FamilyMemberDto familyMemberDto) {
        return baseClient.addFamilyMember(familyMemberDto).getId();
    }

    public void editFamilyMember(FamilyMemberDto familyMemberDto) {
        baseClient.editFamilyMember(familyMemberDto);
    }

    public CheckStatus linkFamilyMember(FamilyMemberDto familyMemberDto) {
        return familyClient.addGuard(familyMemberDto);
    }

    public Collection<FamilyMemberDto> getMembersByFirstCreator(Localisation localisation) {
        return baseClient.getMembersByFirstCreator(localisation);
    }
    public Collection<FamilyMemberDto> getFamilyTreeOfMember(UUID uuid, SecretLevel choice, Localisation localisation) {
        return baseClient.getFamilyTreeOfMember(uuid, choice, localisation);
    }

}