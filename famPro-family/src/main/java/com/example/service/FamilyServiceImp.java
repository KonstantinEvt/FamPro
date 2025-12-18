package com.example.service;

import com.example.dtos.FamilyMemberDto;
import com.example.entity.Changing;
import com.example.entity.Family;
import com.example.entity.FamilyMemberLink;
import com.example.entity.ShortFamilyMember;
import com.example.enums.ChangingStatus;
import com.example.enums.RoleInFamily;
import com.example.enums.SecretLevel;
import com.example.enums.Sex;
import com.example.repository.FamilyMemberLinkRepository;
import com.example.repository.FamilyRepo;
import com.example.repository.FamilyRepository;
import com.example.repository.ShortMemberRepo;
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
public class FamilyServiceImp {
    private FamilyRepo familyRepo;
    private ShortMemberRepo memberRepo;
    private GuardService guardService;
    private MemberService memberService;
    private FamilyRepository familyRepository;
    private FamilyMemberLinkRepository familyMemberLinkRepository;


    @Transactional
    public Family creatFreeFamily(String fatherInfo, String motherInfo, UUID externId, Date date) {
        Family primeFamily = new Family();
        primeFamily.setGuard(new HashSet<>());
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
    public void changeMainFamilyIdentification(Family family, UUID newUuid, ShortFamilyMember member, Optional<ShortFamilyMember> father, Optional<ShortFamilyMember> mother) {
        family.setUuid(newUuid);
        family.setHusbandInfo(member.getFatherInfo());
        family.setWifeInfo(member.getMotherInfo());
        father.ifPresent(family::setHusband);
        mother.ifPresent(family::setWife);
        setFamilySecretLevels(family, member);
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
        Family newFamily = creatFreeFamily(mainDto.getFatherInfo(), mainDto.getMotherInfo(), mainMember.getUuid(),mainDto.getBirthday());
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
        Set<FamilyMemberLink> familyMemberLinks = familyMemberLinkRepository.getFamilyMemberLinks(oldFamily, member, member.getUuid());
        if (!familyMemberLinks.isEmpty()) {
            for (FamilyMemberLink link :
                    familyMemberLinks) {
                link.setFamily(newFamily);
                log.info("PRIG_prig_prig {}", link.getCausePerson());
            }

        } else addPersonToFamily(newFamily, member, RoleInFamily.CHILD, member.getUuid(), null);

        setFamilySecretLevels(newFamily, member);
    }

    @Transactional
    public Optional<FamilyMemberLink> addPersonToFamily(Family family, ShortFamilyMember member, RoleInFamily roleInFamily, UUID linkPerson, String description) {
        FamilyMemberLink newMember = FamilyMemberLink.builder()
                .member(member)
                .family(family)
                .roleInFamily(roleInFamily)
                .causePerson(linkPerson)
                .description(description)
                .build();
        familyMemberLinkRepository.addFamilyMember(newMember);
        setFamilySecretLevels(family, member);
        if (roleInFamily==RoleInFamily.CHILD) setAutoFamilyBirthday(family);
        return Optional.of(newMember);
    }

    public void setAutoFamilyBirthday(Family family){
        Optional<Date> date=family.getChildren().stream().map(ShortFamilyMember::getBirthday).reduce((x, y)->((x.toLocalDate().isAfter(y.toLocalDate()))?y:x));
    if (date.isPresent()&&!Objects.equals(family.getBirthday(),date.get())) family.setBirthday(date.get());
    }
    @Transactional
    public void mergeFamilies(Family donor, Family merged) {
        Set<FamilyMemberLink> donorMembers = familyMemberLinkRepository.getAllFamilyMemberLinks(donor);
        for (FamilyMemberLink fmDonor :
                donorMembers) {
            fmDonor.setFamily(merged);
        }
        merged.getChildren().addAll(donor.getChildren());
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
//        Set<Family> familiesOfBrothersByFather = familyRepo.findAllByHusband(member);
//        if (!familiesOfBrothersByFather.isEmpty()) {
//            if (primeFamily.getHalfChildrenByFather() == null)
//                primeFamily.setHalfChildrenByFather(new HashSet<>());
//            for (Family fam :
//                    familiesOfBrothersByFather) {
//                if (!Objects.equals(fam.getUuid(), primeFamily.getUuid())) {
//                    primeFamily.getHalfChildrenByFather().addAll(fam.getChildren());
//                    Set<FamilyMemberLink> familyMemberLinks = fam.getChildren().stream().map(x -> FamilyMemberLink.builder().member(x).family(primeFamily).build()).collect(Collectors.toSet());
//
//                    if (fam.getHalfChildrenByFather() == null)
//                        fam.setHalfChildrenByFather(new HashSet<>());
//                    fam.getHalfChildrenByFather().add(mainMember);
//                    familyMemberLinks.add(FamilyMemberLink.builder().member(mainMember).family(primeFamily).build());
//                    familyMemberLinkRepository.addAllFamilyMember(familyMemberLinks);
//                } else mergeFamilies(primeFamily, fam);
//            }
//        }
        memberService.addChildToFamilyMember(mainMember, member, Sex.MALE);
//        familyRepo.saveAll(familiesOfBrothersByFather);
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
//        Set<Family> familiesOfBrothersByMother = familyRepo.findAllByWife(member);
//
//        if (!familiesOfBrothersByMother.isEmpty()) {
//            if (primeFamily.getHalfChildrenByMother() == null)
//                primeFamily.setHalfChildrenByMother(new HashSet<>());
//            for (Family fam :
//                    familiesOfBrothersByMother) {
//                if (!Objects.equals(fam.getUuid(), primeFamily.getUuid())) {
//                    Set<FamilyMemberLink> familyMemberLinks = fam.getChildren().stream().map(x -> FamilyMemberLink.builder().member(x).family(primeFamily).build()).collect(Collectors.toSet());
//                    if (fam.getHalfChildrenByMother() == null)
//                        fam.setHalfChildrenByMother(new HashSet<>());
//                    primeFamily.getHalfChildrenByMother().addAll(fam.getChildren());
//                    fam.getHalfChildrenByMother().add(mainMember);
//                    familyMemberLinks.add(FamilyMemberLink.builder().member(mainMember).family(primeFamily).build());
//                    familyMemberLinkRepository.addAllFamilyMember(familyMemberLinks);
//                } else mergeFamilies(primeFamily, fam);
//            }
//        }
        memberService.addChildToFamilyMember(mainMember, member, Sex.FEMALE);
//        familyRepo.saveAll(familiesOfBrothersByMother);
        return externId;
    }

//    @Transactional
//    public void addPersonParentsToFamilyParents(Family family, ShortFamilyMember member) {
//        if (member.getFather() != null) {
//            if (family.getParents() == null) family.setParents(new HashSet<>());
//            family.getParents().add(member.getFather());
//            family.getFamilyMembers().add(member.getFather());
//            if (member.getFather().getLinkedGuard() != null && !member.getFather().getLinkedGuard().isEmpty())
//                guardService.addGuardToFamily(member.getFather().getLinkedGuard().get(0), family);
//        }
//        if (member.getMother() != null) {
//            if (family.getParents() == null) family.setParents(new HashSet<>());
//            family.getParents().add(member.getMother());
//            family.getFamilyMembers().add(member.getMother());
//            if (member.getMother().getLinkedGuard() != null && !member.getMother().getLinkedGuard().isEmpty())
//                guardService.addGuardToFamily(member.getMother().getLinkedGuard().get(0), family);
//        }
//
//    }

//    @Transactional
//    public Set<Family> addGrandLinks(Set<ShortFamilyMember> grandChilds, ShortFamilyMember primeMember) {
//        Set<Family> grandChildFamilies = new HashSet<>();
//        if (grandChilds != null && !grandChilds.isEmpty()) {
//            for (ShortFamilyMember grandChild : grandChilds) {
//                Family grand = grandChild.getFamilyWhereChild();
//                if (grand.getParents() == null) grand.setParents(new HashSet<>());
//                grand.getParents().add(primeMember);
//                grand.getFamilyMembers().add(primeMember);
//                primeMember.getFamilies().add(grand);
//                if (primeMember.getLinkedGuard() != null && !primeMember.getLinkedGuard().isEmpty())
//                    guardService.addGuardToFamily(primeMember.getLinkedGuard().get(0), grand);
//                grandChildFamilies.add(grand);
//            }
//        }
//        return grandChildFamilies;
//    }

//    @Transactional
//    public void ejectionPersonFromFamily(ShortFamilyMember familyMember, Family family) {
//        Family newFamily = creatFreeFamily(familyMember.getFatherInfo(), familyMember.getMotherInfo(), familyMember.getUuid().toString());
//        family.getChildren().remove(familyMember);
//        newFamily.getChildren().add(familyMember);
//        if (familyMember.getLinkedGuard() != null && !familyMember.getLinkedGuard().isEmpty()) {
//            family.getGuard().remove(familyMember.getLinkedGuard().get(0));
//            newFamily.getGuard().add(familyMember.getLinkedGuard().get(0));
//        }
//        addPersonParentsToFamilyParents(newFamily, familyMember);
//        if (familyMember.getMother() != null) {
//            addChangesFromMother(newFamily, familyMember.getMother().getFamilyWhereChild(), familyMember, familyMember.getMother());
//            newFamily.setGlobalFamily(family.getGlobalFamily());
//        }
//        if (familyMember.getFather() != null) {
//            addChangesFromFather(newFamily, familyMember.getFather().getFamilyWhereChild(), familyMember, familyMember.getFather());
//            newFamily.setGlobalFamily(family.getGlobalFamily());
//        }
//        if (familyMember.getMother() == null && familyMember.getFather() == null && familyMember.getMotherInfo() == null && familyMember.getFatherInfo() == null) {
//            globalFamilyService.creatNewGlobalFamily(newFamily);
//            fullEjectionPersonWithKin(familyMember);
//        }
//        familyRepo.save(newFamily);
//        memberRepo.save(familyMember);
//
//
//    }

    @Transactional
    public void fullEjectionPersonWithKin(ShortFamilyMember familyMember) {
        // Реализаия полного удаления члена семьи из рода, включая разделение на 2 глобальных семьи и полный разрыв связи по роду
    }
}
