package com.example.service;

import com.example.dtos.FamilyMemberDto;
import com.example.entity.*;
import com.example.enums.ChangingStatus;
import com.example.enums.RoleInFamily;
import com.example.enums.SecretLevel;
import com.example.enums.Sex;
import com.example.repository.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.*;

@Service
@AllArgsConstructor
@Getter
@Setter
@Log4j2
public class FamilyServiceImp implements SimpleFamilyService {
    private FamilyRepo familyRepo;
    private ShortMemberRepo memberRepo;
    private GuardService guardService;
    private MemberService memberService;
    private FamilyRepository familyRepository;
    private FamilyMemberLinkRepository familyMemberLinkRepository;


    @Transactional
    public Family creatFreeFamily(String fatherInfo, String motherInfo, UUID externId, Date date) {
        Family primeFamily = new Family();
        primeFamily.setFamilyMemberLinks(new HashSet<>());
        primeFamily.setUuid(externId);
        primeFamily.setBirthday(date);
        primeFamily.setChildren(new HashSet<>());
        primeFamily.setSecretLevelPhoto(SecretLevel.OPEN);
        primeFamily.setSecretLevelEdit(SecretLevel.OPEN);
        primeFamily.setSecretLevelGet(SecretLevel.OPEN);
        primeFamily.setSecretLevelRemove(SecretLevel.OPEN);
        if (fatherInfo != null) primeFamily.setHusbandInfo(fatherInfo);
        if (motherInfo != null) primeFamily.setWifeInfo(motherInfo);
        familyRepository.saveNewFamily(primeFamily);
        return primeFamily;
    }

    @Transactional
    public void changeMainFamilyIdentification(Family family, UUID newUuid, ShortFamilyMember member) {
        family.setUuid(newUuid);
        family.setHusbandInfo(member.getFatherInfo());
        family.setWifeInfo(member.getMotherInfo());
        if (member.getFatherUuid() != null && member.getMotherUuid() != null) {
            Set<ShortFamilyMember> parents = memberService.getAllMembersByUuids(Set.of(member.getFatherUuid(), member.getMotherUuid()));
            for (ShortFamilyMember parent :
                    parents) {
                if (parent.getSex() == Sex.MALE) family.setHusband(parent);
                else family.setWife(parent);
            }
        } else {
            if (member.getFatherUuid() != null) {
                Optional<ShortFamilyMember> father = memberService.getMemberByUuid(member.getFatherUuid());
                father.ifPresent(family::setHusband);
            }
            if (member.getMotherUuid() != null) {
                Optional<ShortFamilyMember> mother = memberService.getMemberByUuid(member.getMotherUuid());
                mother.ifPresent(family::setWife);
            }
        }
        setFamilySecretLevels(family, member);
        if (!Objects.equals(family.getBirthday().toLocalDate(), member.getBirthday().toLocalDate()))
            setAutoFamilyBirthday(family);
        familyRepository.updateFamily(family);
    }

    @Transactional
    public void setFamilySecretLevels(Family family, ShortFamilyMember member) {
        if (family.getSecretLevelEdit() == null || family.getSecretLevelEdit().ordinal() < member.getSecretLevelEdit().ordinal())
            family.setSecretLevelEdit(member.getSecretLevelEdit());
        if (family.getSecretLevelGet() == null || family.getSecretLevelGet().ordinal() < member.getSecretLevelMainInfo().ordinal())
            family.setSecretLevelGet(member.getSecretLevelMainInfo());
        if (family.getSecretLevelRemove() == null || family.getSecretLevelRemove().ordinal() < member.getSecretLevelRemove().ordinal())
            family.setSecretLevelRemove(member.getSecretLevelRemove());
        if (family.getSecretLevelPhoto() == null || family.getSecretLevelPhoto().ordinal() < member.getSecretLevelPhoto().ordinal())
            family.setSecretLevelPhoto(member.getSecretLevelPhoto());
    }

    @Transactional
    public Family changeFamilyByRemoveParentLink(Changing changing,
                                                 Family primeFamily,
                                                 ShortFamilyMember mainMember,
                                                 FamilyMemberDto mainDto) {

        if (changing.getChangingFather().ordinal() > 4) {
            if (changing.getChangingFather() != ChangingStatus.LIGHT_FREE
                    && changing.getChangingFather() != ChangingStatus.HARD_FREE
                    && changing.getChangingFather() != ChangingStatus.CHANGE)
                memberService.removeLinkWithParent(primeFamily, mainMember, primeFamily.getHusband());
            changing.setChangingFather(changeChangingStatusAfterRemove(changing.getChangingFather()));
            mainMember.setFatherUuid(null);
        }
        if (changing.getChangingMother().ordinal() > 4) {
            if (changing.getChangingMother() != ChangingStatus.LIGHT_FREE
                    && changing.getChangingMother() != ChangingStatus.HARD_FREE
                    && changing.getChangingMother() != ChangingStatus.CHANGE)
                memberService.removeLinkWithParent(primeFamily, mainMember, primeFamily.getWife());
            changing.setChangingMother(changeChangingStatusAfterRemove(changing.getChangingMother()));
            mainMember.setMotherUuid(null);
        }
        if (!changing.isOneChildInFamily()) {
            changing.setOneChildInFamily(true);
            return ejectChildInNewFamily(primeFamily, mainMember, mainDto);
        } else {
            if (mainDto.getMotherInfo() == null || mainDto.getMotherInfo().isBlank()) {
                primeFamily.setWifeInfo(null);
                primeFamily.setWife(null);
            }
            if (mainDto.getFatherInfo() == null || mainDto.getFatherInfo().isBlank()) {
                primeFamily.setHusbandInfo(null);
                primeFamily.setHusband(null);
            }
            primeFamily.setUuid(mainMember.getUuid());
            log.info("family identification changed");
        }
        return primeFamily;
    }

    @Transactional
    public Family ejectChildInNewFamily(Family family,
                                        ShortFamilyMember mainMember,
                                        FamilyMemberDto mainDto) {
        Family newFamily = creatFreeFamily(mainDto.getFatherInfo(), mainDto.getMotherInfo(), mainMember.getUuid(), mainDto.getBirthday());
        family.getChildren().remove(mainMember);
        changeFamilyForPerson(family, newFamily, mainMember);
        newFamily.getChildren().add(mainMember);
        if (Objects.equals(family.getBirthday(), mainDto.getBirthday())) setAutoFamilyBirthday(family);
        mainMember.setFamilyWhereChild(newFamily);
        return newFamily;
    }

    private ChangingStatus changeChangingStatusAfterRemove(ChangingStatus changingStatus) {
        switch (changingStatus) {
            case MINOR_CHANGE, HARD_FREE -> {
                return ChangingStatus.FREE;
            }
            case CHANGE, MAJOR_CHANGE -> {
                return ChangingStatus.SET;
            }
            default -> {
                return ChangingStatus.NONE;
            }
        }
    }

    @Transactional
    public void changeFamilyForPerson(Family oldFamily, Family newFamily, ShortFamilyMember member) {
        Set<FamilyMemberLink> familyMemberLinks = familyMemberLinkRepository.getFamilyMemberLinksByFamilyAndCausePerson(oldFamily, member.getUuid());
        if (!familyMemberLinks.isEmpty()) {
            if ((member.getLinkGuard() != null && !member.getLinkGuard().isBlank())
                    || (member.getActiveGuard() != null && !member.getActiveGuard().isBlank())) {
                Set<UUID> guards = getAllUuidFromInfo(member.getActiveGuard());
                if (member.getLinkGuard() != null && !member.getLinkGuard().isBlank())
                    guards.add(UUID.fromString(member.getLinkGuard()));
                for (UUID guardUuid :
                        guards) {
                    removeUuidFromInfo(oldFamily.getActiveGuard(), guardUuid);
                    addUuidToInfo(newFamily.getActiveGuard(), guardUuid);
                }
            }
            for (FamilyMemberLink link :
                    familyMemberLinks) {
                link.setFamily(newFamily);
                log.info("PRIG_prig_prig {}", link.getCausePerson());
            }

        } else addPersonToFamily(newFamily, member, RoleInFamily.CHILD, member.getUuid(), null);

        setFamilySecretLevels(newFamily, member);
    }

    @Transactional
    public void addPersonToFamily(Family family, ShortFamilyMember member, RoleInFamily roleInFamily, UUID linkPerson, String description) {
        if ((member.getLinkGuard() != null && !member.getLinkGuard().isBlank())
                || (member.getActiveGuard() != null && !member.getActiveGuard().isBlank())) {
            Set<UUID> guards = getAllUuidFromInfo(member.getActiveGuard());
            if (member.getLinkGuard() != null && !member.getLinkGuard().isBlank())
                guards.add(UUID.fromString(member.getLinkGuard()));
            for (UUID guardUuid :
                    guards) {
                addUuidToInfo(family.getActiveGuard(), guardUuid);
            }
        }
        FamilyMemberLink newMember = FamilyMemberLink.builder()
                .member(member)
                .family(family)
                .roleInFamily(roleInFamily)
                .causePerson(linkPerson)
                .description(description)
                .build();
        if (member.getLinkGuard() != null && !member.getLinkGuard().isBlank())
            newMember.setLinkGuard(UUID.fromString(member.getLinkGuard()));
        familyMemberLinkRepository.addFamilyMemberLink(newMember);
        setFamilySecretLevels(family, member);
        if (roleInFamily == RoleInFamily.CHILD) setAutoFamilyBirthday(family);
    }

    public void setAutoFamilyBirthday(Family family) {
        Optional<Date> date = family.getChildren().stream().map(ShortFamilyMember::getBirthday).reduce((x, y) -> ((x.toLocalDate().isAfter(y.toLocalDate())) ? y : x));
        if (date.isPresent() && !Objects.equals(family.getBirthday(), date.get())) family.setBirthday(date.get());
    }

    @Transactional
    public void mergeFamilies(Family donor, Family merged) {
        Set<FamilyMemberLink> donorMembers = familyMemberLinkRepository.getAllFamilyMemberLinksByFamily(donor);
        for (FamilyMemberLink fmDonor :
                donorMembers) {
            fmDonor.setFamily(merged);
        }
        merged.getChildren().addAll(donor.getChildren());
        setAutoFamilyBirthday(merged);
    }

    @Transactional
    public Optional<UUID> addChangesFromFather(Family primeFamily,
                                               ShortFamilyMember mainMember,
                                               ShortFamilyMember member) {
        Optional<UUID> externId;
        primeFamily.setHusband(member);
        addPersonToFamily(primeFamily, member, RoleInFamily.FATHER, mainMember.getUuid(), null);
        primeFamily.setHusbandInfo(member.getFullName());
        if (primeFamily.getWifeInfo() != null && (primeFamily.getWifeInfo().charAt(0) != '(' || primeFamily.getWifeInfo().charAt(1) == 'A')) {
            externId = Optional.of(UUID.nameUUIDFromBytes(primeFamily.getHusbandInfo().concat(primeFamily.getWifeInfo()).getBytes()));


        } else externId = Optional.empty();
        memberService.addChildToFamilyMember(mainMember, member, Sex.MALE);
        return externId;
    }

    @Transactional
    public void updateFamily(Family family) {
        familyRepository.updateFamily(family);
    }

    @Transactional
    public void detachFamily(Family family) {
        familyRepository.detachFamily(family);
    }

    @Transactional
    public void removeFamily(Family family) {
        familyRepository.removeFamily(family);
    }

    public void refreshFamily(Family family) {
        familyRepository.refreshFamily(family);
    }

    @Transactional
    public Optional<UUID> addChangesFromMother(Family primeFamily,
                                               ShortFamilyMember mainMember,
                                               ShortFamilyMember member) {
        primeFamily.setWife(member);
        Optional<UUID> externId;
        addPersonToFamily(primeFamily, member, RoleInFamily.MOTHER, mainMember.getUuid(), null);
        primeFamily.setWifeInfo(member.getFullName());
        if (primeFamily.getHusbandInfo() != null && (primeFamily.getHusbandInfo().charAt(0) != '(' || primeFamily.getHusbandInfo().charAt(1) == 'A')) {
            externId = Optional.of(UUID.nameUUIDFromBytes(primeFamily.getHusbandInfo().concat(primeFamily.getWifeInfo()).getBytes()));


        } else externId = Optional.empty();
        memberService.addChildToFamilyMember(mainMember, member, Sex.FEMALE);
        return externId;
    }
}
