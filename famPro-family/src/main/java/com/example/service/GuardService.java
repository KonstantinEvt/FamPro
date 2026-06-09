package com.example.service;

import com.example.entity.*;
import com.example.enums.*;
import com.example.repository.FamilyMemberLinkRepository;
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
    private MemberService memberService;
    private SendAndFormService sendAndFormService;
    private MemberRepository memberRepository;
    private FamilyMemberLinkRepository familyMemberLinkRepository;

    @Transactional
    public Optional<Guard> findGuard(String uuid) {
        return guardRepository.findGuard(uuid);
    }

    @Transactional
    public String getLinkingPersonOfGuard(String uuid) {
        return guardRepository.findGuardWithLinkingPerson(uuid).orElseThrow(() -> new RuntimeException("user not Linking")).getLinkedPerson().getUuid().toString();
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
        Set<String> changingStatus = memberService.repairGeneticTreeCheckStatus(memberService.getAllTopAncestors(shortFamilyMember),true);
        Set<FamilyMemberLink> links = familyMemberLinkRepository.getAllFamilyMemberLinksOfPerson(shortFamilyMember);
        UUID uuid = UUID.fromString(token);
        links.forEach(x -> x.setLinkGuard(uuid));
        Family primeFamily = shortFamilyMember.getFamilyWhereChild();
        primeFamily.setActiveGuard(addUuidToInfo(primeFamily.getActiveGuard(), token));
        if (shortFamilyMember.getActiveFamily() != null && !Objects.equals(primeFamily.getId(), shortFamilyMember.getActiveFamily().getId()))
            shortFamilyMember.getActiveFamily().setActiveGuard(addUuidToInfo(shortFamilyMember.getActiveFamily().getActiveGuard(), token));
        if (shortFamilyMember.getLogicPrimary() != null && !Objects.equals(primeFamily.getId(), shortFamilyMember.getLogicPrimary().getId())
                && (shortFamilyMember.getActiveFamily() == null || !Objects.equals(shortFamilyMember.getActiveFamily().getId(), shortFamilyMember.getLogicPrimary().getId())))
            shortFamilyMember.getLogicPrimary().setActiveGuard(addUuidToInfo(shortFamilyMember.getLogicPrimary().getActiveGuard(), token));

//        for (ShortFamilyMember child :
//                primeFamily.getChildren()) {
//            if (!Objects.equals(child.getUuid(),uuid)) child.setPrimaryGuard(primeFamily.getActiveGuard());
//            if (child.getCheckStatus() == CheckStatus.UNCHECKED) {
//                child.setCheckStatus(CheckStatus.CHECKED);
//                child.setCreator(null);
//                changingStatus.add(child.getUuid().toString());
//            }
//        }

//        if (!Objects.equals(primeFamily.getId(), activeFamily.getId())) {
//            String activeGuard = addUuidToInfo(activeFamily.getActiveGuard(), token);
//            activeFamily.setActiveGuard(activeGuard);
//            activeFamily.getFamilyMemberLinks().stream()
//                    .filter(x -> x.getRoleInFamily() == RoleInFamily.FATHER
//                            || x.getRoleInFamily() == RoleInFamily.MOTHER
//                            || x.getRoleInFamily() == RoleInFamily.CHILD)
//                    .map(FamilyMemberLink::getMember)
//                    .distinct()
//                    .peek(x -> {
//                        if (x.getCheckStatus() == CheckStatus.UNCHECKED) {
//                            x.setCheckStatus(CheckStatus.CHECKED);
//                            x.setCreator(null);
//                            changingStatus.add(x.getUuid().toString());
//                        }
//                    })
//                    .forEach(x -> x.setActiveGuard(activeGuard));
//        }
        shortFamilyMember.setLastUpdate(new Timestamp(System.currentTimeMillis()));
        memberService.updateMember(shortFamilyMember);
        memberRepository.flush();
        log.info("New linking guard is created");
        sendAndFormService.formDirectiveToStorageForChangeStatus(token, shortFamilyMember.getUuid().toString(), SwitchPosition.FATHER, KafkaOperation.RENAME, changingStatus, CheckStatus.CHECKED);
        sendAndFormService.sendAttentionToUser(token, shortFamilyMember.getFullName(), shortFamilyMember, Attention.LINK);
    }

    @Transactional
    public void mergeActiveGuards(Family donor, Family merged) {
        if (donor.getActiveGuard() != null && !donor.getActiveGuard().isBlank()) {
            merged.setActiveGuard(mergeInfo(donor.getActiveGuard(), merged.getActiveGuard()));
//            for (ShortFamilyMember member :
//                    merged.getChildren()) {
//                member.setActiveGuard(merged.getActiveGuard());
        }
//            if (merged.getWife() != null) merged.getWife().setActiveGuard(merged.getActiveGuard());
//            if (merged.getHusband() != null) merged.getHusband().setActiveGuard(merged.getActiveGuard());
//        } else if (merged.getActiveGuard() != null && !merged.getActiveGuard().isBlank()) {
//            for (ShortFamilyMember member :
//                    donor.getChildren()) {
//                member.setActiveGuard(merged.getActiveGuard());
//            }
//        }
    }

    @Transactional
    public void mergePrimaryGuards(Set<ShortFamilyMember> donor, Set<ShortFamilyMember> merged) {
        ShortFamilyMember firstDonor = donor.stream().findFirst().orElseThrow(() -> new RuntimeException("Set of Children Empty"));
        ShortFamilyMember firstMerged = merged.stream().findFirst().orElseThrow(() -> new RuntimeException("Set of Children Empty"));

        String d = mergeInfo(firstDonor.getPrimaryGuard(), firstDonor.getLinkGuard());
        String m = mergeInfo(firstMerged.getPrimaryGuard(), firstMerged.getLinkGuard());

        if (d == null && m == null) return;
        if (d != null && m != null) {
            String result = mergeInfo(d, m);
            for (ShortFamilyMember mer :
                    merged) {
                if (mer.getLinkGuard() != null && !mer.getLinkGuard().isBlank())
                    mer.setPrimaryGuard(removeUuidFromInfo(result, mer.getLinkGuard()).orElse(null));
                else mer.setPrimaryGuard(result);
            }
            for (ShortFamilyMember don :
                    donor) {
                if (don.getLinkGuard() != null && !don.getLinkGuard().isBlank())
                    don.setPrimaryGuard(removeUuidFromInfo(result, don.getLinkGuard()).orElse(null));
                else don.setPrimaryGuard(result);
            }
        } else if (m == null) for (ShortFamilyMember mer :
                merged) {
            mer.setPrimaryGuard(d);
        }
        else for (ShortFamilyMember don :
                    donor) {
                don.setPrimaryGuard(m);
            }
    }

    public Set<String> getMaxLevelGuards(ShortFamilyMember member) {
        if (member.getLinkGuard() != null && !member.getLinkGuard().isBlank()) return Set.of(member.getLinkGuard());
        if (member.getActiveGuard() != null && !member.getActiveGuard().isBlank())
            return getAllUuidFromInfo(member.getActiveGuard()).stream().filter(Objects::nonNull).map(UUID::toString).collect(Collectors.toSet());
        if (member.getLogicGuard() != null && !member.getLogicGuard().isBlank())
            return getAllUuidFromInfo(member.getLogicGuard()).stream().filter(Objects::nonNull).map(UUID::toString).collect(Collectors.toSet());
        if (member.getAncestorsGuard() != null && !member.getAncestorsGuard().isBlank())
            return getAllUuidFromInfo(member.getAncestorsGuard()).stream().filter(Objects::nonNull).map(UUID::toString).collect(Collectors.toSet());
        if (member.getDescendantsGuard() != null && !member.getDescendantsGuard().isBlank())
            return getAllUuidFromInfo(member.getDescendantsGuard()).stream().filter(Objects::nonNull).map(UUID::toString).collect(Collectors.toSet());
        if (member.getPrimaryGuard() != null && !member.getPrimaryGuard().isBlank())
            return getAllUuidFromInfo(member.getPrimaryGuard()).stream().filter(Objects::nonNull).map(UUID::toString).collect(Collectors.toSet());
        Set<ShortFamilyMember> topAncestors = memberService.getAllTopAncestors(member);
        return memberService.getGeneticTreeGuards(topAncestors,true).stream().filter(Objects::nonNull).map(UUID::toString).collect(Collectors.toSet());
    }

    @Transactional
    public boolean findAnyKinGuard(ShortFamilyMember member) {
        Set<ShortFamilyMember> ancTop = memberService.getAllTopAncestors(member);
        for (ShortFamilyMember familyMember : ancTop)
            if ((familyMember.getLinkGuard() != null && !familyMember.getLinkGuard().isBlank())
                    || (familyMember.getDescendantsGuard() != null && !familyMember.getDescendantsGuard().isBlank())
                    || (familyMember.getPrimaryGuard() != null && !familyMember.getPrimaryGuard().isBlank()))
                return true;
        if (memberService.getExtendedTopAncestors(ancTop))
            for (ShortFamilyMember familyMember : ancTop)
                if (familyMember.getDescendantsGuard() != null && !familyMember.getDescendantsGuard().isBlank())
                    return true;
        return false;
    }

    @Transactional
    public void changeFamilyForGuard(Family oldFamily, Family newFamily, ShortFamilyMember member, FamilyLevel level) {
        switch (level) {
            case ACTIVE -> {
            }
            case PRIMARY -> {
                if (member.getLinkGuard() != null && !member.getLinkGuard().isBlank()) {

                    oldFamily.setActiveGuard(removeUuidFromInfo(oldFamily.getActiveGuard(), member.getLinkGuard()).orElse(null));
                    newFamily.setActiveGuard(addUuidToInfo(newFamily.getActiveGuard(), member.getLinkGuard()));

                }
            }
            case LOGIC -> {
                log.info("its logic change");
            }
        }
    }
}
