package com.example.service;

import com.example.entity.Family;
import com.example.entity.FamilyMemberLink;
import com.example.entity.Guard;
import com.example.entity.ShortFamilyMember;
import com.example.enums.RoleInFamily;
import com.example.repository.FamilyMemberLinkRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
@Getter
@Setter
@Log4j2
public class FamilyMemberLinkService {
    private FamilyMemberLinkRepository familyMemberLinkRepository;

    public void changeFamilyMemberLinksByChangeMemberUuid(Set<FamilyMemberLink> familyMemberLinks, ShortFamilyMember member) {
        for (FamilyMemberLink link :
                familyMemberLinks) {
            link.setCausePerson(member.getUuid());
            familyMemberLinkRepository.updateFamilyMemberLink(link);
        }
    }

    @Transactional(readOnly = true)
    public Set<FamilyMemberLink> getAllMemberLinksByMemberUuid(UUID memberUuid) {
        return familyMemberLinkRepository.getAllFamilyMemberLinksByCausePerson(memberUuid);
    }

    public void changeFamilyMemberLinksBySetGuard(Set<FamilyMemberLink> familyMemberLinks, ShortFamilyMember member) {
        Guard guard = new Guard();

        for (FamilyMemberLink link :
                familyMemberLinks) {
            link.setCausePerson(member.getUuid());
            familyMemberLinkRepository.updateFamilyMemberLink(link);
        }
    }

    @Transactional
    public void createFamilyLink(ShortFamilyMember member, Family family, RoleInFamily roleInFamily, UUID causePerson, String description) {
        FamilyMemberLink newMember = FamilyMemberLink.builder()
                .member(member)
                .family(family)
                .roleInFamily(roleInFamily)
                .causePerson(causePerson)
                .description(description)
                .build();
        if (member.getLinkGuard() != null && !member.getLinkGuard().isBlank())
            newMember.setLinkGuard(UUID.fromString(member.getLinkGuard()));
        familyMemberLinkRepository.addFamilyMemberLink(newMember);
    }

    @Transactional
    public void changeFamilyLink(ShortFamilyMember member, Family oldFamily, Family newFamily) {
        Set<FamilyMemberLink> familyMemberLinks = familyMemberLinkRepository.getFamilyMemberLinksByFamilyAndCausePerson(oldFamily, member.getUuid());
        for (FamilyMemberLink link :
                familyMemberLinks) {
            link.setFamily(newFamily);
            log.info("PRIG_prig_prig {}", link.getCausePerson());
        }
    }

    @Transactional
    public void removeAllFamilyLinksBetweenMembers(ShortFamilyMember member, ShortFamilyMember causePerson) {
        System.out.println("delete");
        System.out.println(causePerson.getUuid().toString());
        familyMemberLinkRepository.removeFamilyMemberLinkByMemberAndCausePerson(member, causePerson.getUuid());
    }
}
