package com.example.service;

import com.example.entity.Family;
import com.example.entity.GlobalFamily;
import com.example.entity.Guard;
import com.example.entity.ShortFamilyMember;
import com.example.enums.CheckStatus;
import com.example.repository.FamilyRepository;
import com.example.repository.GuardRepo;
import com.example.repository.MainFamilyRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class GuardService {
    private GuardRepo guardRepo;
    private FamilyRepository familyRepository;
    private MainFamilyRepo mainFamilyRepo;

    @Transactional
    public Optional<Guard> findGuard(String uuid) {
        return guardRepo.findByTokenUser(uuid);
    }


    public void addGuardToFamily(Guard guard, Family family) {
        if (family.getGuard() == null) family.setGuard(new HashSet<>());
        family.getGuard().add(guard);
    }

    @Transactional
    public String getLinkGuard(String uuid) {
        return guardRepo.findByTokenUser(uuid).orElseThrow().getLinkedPerson().getUuid().toString();
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

    @Transactional
    public void addGuardToFamilies(Set<Family> families, Guard guard) {
        for (Family family :
                families) {
            addGuardToFamily(guard, family);
            familyRepository.saveFamily(family);
        }
    }

    @Transactional(readOnly = true)
    public Set<Guard> findFamilyGuards(Family family) {
        Family family1 = familyRepository.getFamilyWithAllGuard(family);
        if (family1 == null) throw new RuntimeException("family error");
        if (family1.getGuard() != null && !family1.getGuard().isEmpty()) return family.getGuard();
        if (family1.getGlobalFamily().getGuard() != null && !family1.getGlobalFamily().getGuard().isEmpty())
            return family1.getGlobalFamily().getGuard();
        System.out.println("Guard not found!!!");
        return new HashSet<>();
    }

    @Transactional
    public void addGuardParentsFamilyToFamily(Family parensFamily, Family family) {
        Set<Guard> guards = mainFamilyRepo.getFamilyGuardWithLinkedPerson(parensFamily);
        if (guards != null && !guards.isEmpty())
            for (Guard guard : guards) {
                if ((!parensFamily.getChildren().contains(guard.getLinkedPerson())
                        || family.getWife() == guard.getLinkedPerson()
                        || family.getHusband() == guard.getLinkedPerson()))
                    addGuardToFamily(guard, family);
            }
    }
}
