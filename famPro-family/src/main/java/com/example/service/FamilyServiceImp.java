package com.example.service;

import com.example.dtos.FamilyMemberDto;
import com.example.entity.Changing;
import com.example.entity.Family;
import com.example.entity.ShortFamilyMember;
import com.example.enums.ChangingStatus;
import com.example.enums.Sex;
import com.example.repository.FamilyRepo;
import com.example.repository.FamilyRepository;
import com.example.repository.ShortMemberRepo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
@Getter
@Setter
public class FamilyServiceImp {
    private FamilyRepo familyRepo;
    private ShortMemberRepo memberRepo;
    private GuardService guardService;
    private MemberService memberService;
    private FamilyRepository familyRepository;


    @Transactional
    public Family creatFreeFamily(String fatherInfo, String motherInfo, UUID externId) {
        Family primeFamily = new Family();
        primeFamily.setGuard(new HashSet<>());
        primeFamily.setFamilyMembers(new HashSet<>());
        primeFamily.setUuid(externId);
        primeFamily.setChildren(new HashSet<>());
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
        setFamilySecretLevels(family,member);
        familyRepository.updateFamily(family);
    }
@Transactional
public void setFamilySecretLevels(Family family,ShortFamilyMember member){
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
        } else {
            if (changing.getChangingMother() != ChangingStatus.LIGHT_FREE
                    && changing.getChangingMother() != ChangingStatus.HARD_FREE
                    && changing.getChangingMother() != ChangingStatus.CHANGE)
                memberService.removeLinkWithParent(primeFamily, mainMember, primeFamily.getWife());
            changing.setChangingMother(changeChangingStatusAfterRemove(changing.getChangingMother()));
            mainMember.setMotherUuid(null);
        }
        if (!changing.isOneChildInFamily()) {
            primeFamily.getChildren().remove(mainMember);
            changing.setOneChildInFamily(true);
            return creatFreeFamily(mainDto.getFatherInfo(), mainDto.getMotherInfo(), mainMember.getUuid());
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
        }
        changing.setOneChildInFamily(true);
        return primeFamily;
    }

    ChangingStatus changeChangingStatusAfterRemove(ChangingStatus changingStatus) {
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
    public void mergeFamilies(Family donor, Family merged) {
        merged.getFamilyMembers().addAll(donor.getFamilyMembers());
        merged.getChildren().addAll(donor.getChildren());
//        if (donor.getHalfChildrenByMother() != null && !donor.getHalfChildrenByMother().isEmpty()) {
//            if (merged.getHalfChildrenByMother() != null)
//                merged.getHalfChildrenByMother().addAll(donor.getHalfChildrenByMother());
//            else merged.setHalfChildrenByMother(donor.getHalfChildrenByMother());
//        }
//        if (donor.getHalfChildrenByFather() != null && !donor.getHalfChildrenByFather().isEmpty()) {
//            if (merged.getHalfChildrenByFather() != null)
//                merged.getHalfChildrenByFather().addAll(donor.getHalfChildrenByFather());
//            else merged.setHalfChildrenByFather(donor.getHalfChildrenByFather());
//        }
        for (ShortFamilyMember child :
                merged.getChildren()) {
            if (merged.getHalfChildrenByFather() != null)
                merged.getHalfChildrenByFather().remove(child);
            if (merged.getHalfChildrenByMother() != null)
                merged.getHalfChildrenByMother().remove(child);
        }
        if (donor.getChildrenInLow() != null && !donor.getChildrenInLow().isEmpty()) {
            if (merged.getChildrenInLow() != null)
                merged.getChildrenInLow().addAll(donor.getChildrenInLow());
            else merged.setChildrenInLow(donor.getChildrenInLow());
        }
        familyRepository.updateFamily(merged);
        familyRepository.removeFamily(donor);
    }

    @Transactional
    public void addChangesFromFather(Family primeFamily,
                                     ShortFamilyMember mainMember,
                                     ShortFamilyMember member) {
        primeFamily.setHusband(member);
        primeFamily.getFamilyMembers().add(member);
        primeFamily.setHusbandInfo(member.getFullName());
        if (primeFamily.getWifeInfo() != null && (primeFamily.getWifeInfo().charAt(0) != '(' || primeFamily.getWifeInfo().charAt(1) == 'A'))
            primeFamily.setUuid(UUID.nameUUIDFromBytes(primeFamily.getHusbandInfo().concat(primeFamily.getWifeInfo()).getBytes()));
        Set<Family> familiesOfBrothersByFather = familyRepo.findAllByHusband(member);
        if (!familiesOfBrothersByFather.isEmpty()) {
            if (primeFamily.getHalfChildrenByFather() == null)
                primeFamily.setHalfChildrenByFather(new HashSet<>());
            for (Family fam :
                    familiesOfBrothersByFather) {
                if (!Objects.equals(fam.getUuid(),primeFamily.getUuid())) {
                    primeFamily.getHalfChildrenByFather().addAll(fam.getChildren());
                    primeFamily.getFamilyMembers().addAll(fam.getChildren());
                    if (fam.getHalfChildrenByFather() == null)
                        fam.setHalfChildrenByFather(new HashSet<>());
                    fam.getHalfChildrenByFather().add(mainMember);
                    fam.getFamilyMembers().add(mainMember);
                }mergeFamilies(primeFamily,fam);
            }
        }
        memberService.addChildToFamilyMember(mainMember, member, Sex.MALE);
        familyRepo.saveAll(familiesOfBrothersByFather);
    }

    @Transactional
    public void addChangesFromMother(Family primeFamily,
                                     ShortFamilyMember mainMember,
                                     ShortFamilyMember member) {
        primeFamily.setWife(member);
        primeFamily.getFamilyMembers().add(member);
        primeFamily.setWifeInfo(member.getFullName());
        if (primeFamily.getHusbandInfo() != null && (primeFamily.getHusbandInfo().charAt(0) != '(' || primeFamily.getHusbandInfo().charAt(1) == 'A'))
            primeFamily.setUuid(UUID.nameUUIDFromBytes(primeFamily.getHusbandInfo().concat(primeFamily.getWifeInfo()).getBytes()));
        Set<Family> familiesOfBrothersByMother = familyRepo.findAllByWife(member);

        if (!familiesOfBrothersByMother.isEmpty()) {
            if (primeFamily.getHalfChildrenByMother() == null)
                primeFamily.setHalfChildrenByMother(new HashSet<>());
            for (Family fam :
                    familiesOfBrothersByMother) {
                if (!Objects.equals(fam.getUuid(),primeFamily.getUuid())) {
                    if (fam.getHalfChildrenByMother() == null)
                        fam.setHalfChildrenByMother(new HashSet<>());
                    primeFamily.getHalfChildrenByMother().addAll(fam.getChildren());
                    primeFamily.getFamilyMembers().addAll(fam.getChildren());
                    fam.getHalfChildrenByMother().add(mainMember);
                    fam.getFamilyMembers().add(mainMember);
                }else mergeFamilies(primeFamily,fam);
            }
        }
        memberService.addChildToFamilyMember(mainMember, member,Sex.FEMALE);
        familyRepo.saveAll(familiesOfBrothersByMother);
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
