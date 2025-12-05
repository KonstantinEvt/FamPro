package com.example.service;

import com.example.entity.Family;
import com.example.entity.Guard;
import com.example.entity.ShortFamilyMember;
import com.example.enums.Attention;
import com.example.enums.CheckStatus;
import com.example.enums.KafkaOperation;
import com.example.enums.SwitchPosition;
import com.example.repository.FamilyRepository;
import com.example.repository.GuardRepository;
import com.example.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class GuardService implements SimpleFamilyService {
    private GuardRepository guardRepository;
    private FamilyRepository familyRepository;
    private MemberService memberService;
    private SendAndFormService sendAndFormService;
    private MemberRepository memberRepository;

    @Transactional
    public Optional<Guard> findGuard(String uuid) {
        return guardRepository.findGuard(uuid);
    }

    @Transactional
    public Optional<Guard> findGuardWithLinkingPerson(String uuid) {
        return guardRepository.findGuardWithLinkingPerson(uuid);
    }

    @Transactional
    public void addGuardToFamily(Guard guard, Family family) {
        if (family.getGuard() == null) family.setGuard(new HashSet<>());
        family.getGuard().add(guard);
    }

    @Transactional
    public String getLinkGuard(String uuid) {
        return guardRepository.findGuardWithLinkingPerson(uuid).orElseThrow(()->new RuntimeException("user not Linking")).getLinkedPerson().getUuid().toString();
    }

    @Transactional
    public Guard creatGuard(ShortFamilyMember familyMember, String uuid) {
        Guard linkGuard = Guard.builder()
                .linkedPerson(familyMember)
                .tokenUser(uuid)
                .build();
        return guardRepository.persistGuard(linkGuard);
    }

    @Transactional
    public void creatLinkingGuard(ShortFamilyMember shortFamilyMember, String token) {
        Guard guard = creatGuard(shortFamilyMember, token);
        memberService.addGuardToMemberByLinking(shortFamilyMember, guard);
        memberService.addGuardToMemberKin(shortFamilyMember, guard);
        Set<String> changingStatus = memberService.repairGeneticTreeCheckStatus(memberService.getAllTopAncestors(shortFamilyMember));
//                guardService.addGuardToFamilies(shortFamilyMember.getFamilies(), guard);
        shortFamilyMember.setLastUpdate(new Timestamp(System.currentTimeMillis()));
        memberService.updateMember(shortFamilyMember);
        memberRepository.flush();
        log.info("New guard is created");
        sendAndFormService.formDirectiveToStorageForChangeStatus(token, shortFamilyMember.getUuid().toString(), SwitchPosition.FATHER, KafkaOperation.RENAME, changingStatus, CheckStatus.CHECKED);
        sendAndFormService.sendAttentionToUser(token, shortFamilyMember.getFullName(), shortFamilyMember, Attention.LINK);
    }

    @Transactional
    public void addGuardToFamilies(Set<Family> families, Guard guard) {
        for (Family family :
                families) {
            addGuardToFamily(guard, family);
            familyRepository.updateFamily(family);
        }
    }

    public Set<String> getMaxLevelGuards(ShortFamilyMember member) {
        if (member.getLinkGuard() != null && !member.getLinkGuard().isBlank()) return Set.of(member.getLinkGuard());
        if (member.getAncestorsGuard() != null && !member.getAncestorsGuard().isBlank())
            return getAllUuidFromInfo(member.getAncestorsGuard()).stream().filter(Objects::nonNull).map(UUID::toString).collect(Collectors.toSet());
        if (member.getActiveGuard() != null && !member.getActiveGuard().isBlank())
            return getAllUuidFromInfo(member.getActiveGuard()).stream().filter(Objects::nonNull).map(UUID::toString).collect(Collectors.toSet());
        if (member.getDescendantsGuard() != null && !member.getDescendantsGuard().isBlank())
            return getAllUuidFromInfo(member.getDescendantsGuard()).stream().filter(Objects::nonNull).map(UUID::toString).collect(Collectors.toSet());
        Set<ShortFamilyMember> topAncestors = memberService.getAllTopAncestors(member);
        return memberService.getGeneticTreeGuards(topAncestors).stream().filter(Objects::nonNull).map(UUID::toString).collect(Collectors.toSet());
    }
}
