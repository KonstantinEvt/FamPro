package com.example.service;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.TokenUser;
import com.example.entity.Family;
import com.example.entity.GlobalFamily;
import com.example.entity.Guard;
import com.example.entity.ShortFamilyMember;
import com.example.enums.CheckStatus;
import com.example.repository.FamilyRepo;
import com.example.repository.GlobalFamilyRepo;
import com.example.repository.GuardRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GuardService {
    private GuardRepo guardRepo;
    private MemberService memberService;
    private FamilyRepo familyRepo;
    private GlobalFamilyRepo globalFamilyRepo;

    @Transactional
    public Optional<Guard> findGuard(String uuid) {
        return guardRepo.findByTokenUser(uuid);
    }


    public void addGuardToFamily(Guard guard, Family family) {
        if (family.getGuard() == null) family.setGuard(new HashSet<>());
        family.getGuard().add(guard);
    }

    public void addGuardToGlobalFamily(Guard guard, GlobalFamily globalFamily) {
        if (globalFamily.getGuard() == null) globalFamily.setGuard(new HashSet<>());
        globalFamily.getGuard().add(guard);
    }

    @Transactional
    public Guard creatGuard(ShortFamilyMember familyMember, String uuid) {
        Guard linkGuard = Guard.builder()
                .linkedPerson(familyMember)
                .tokenUser(uuid)
                .build();
        familyMember.setCheckStatus(CheckStatus.LINKED);
        return guardRepo.save(linkGuard);
    }

    public void addGuardToFamilies(Set<Family> families, Guard guard) {
        for (Family family :
                families) {
            addGuardToFamily(guard,family);
        }
    }
}
