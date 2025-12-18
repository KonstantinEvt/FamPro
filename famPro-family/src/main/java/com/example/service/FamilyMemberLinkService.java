package com.example.service;

import com.example.entity.FamilyMemberLink;
import com.example.entity.ShortFamilyMember;
import com.example.repository.FamilyMemberLinkRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
@Getter
@Setter
@Log4j2
public class FamilyMemberLinkService {
    private FamilyMemberLinkRepository familyMemberLinkRepository;

    public void changeFamilyMemberLinksByMemberUuid(Set<FamilyMemberLink> familyMemberLinks, ShortFamilyMember member) {
        for (FamilyMemberLink link :
                familyMemberLinks) {
            link.setCausePerson(member.getUuid());
            familyMemberLinkRepository.update(link);
        }
    }

}
