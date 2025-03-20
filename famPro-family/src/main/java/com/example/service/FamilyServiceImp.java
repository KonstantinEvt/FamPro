package com.example.service;

import com.example.dtos.Directive;
import com.example.dtos.FamilyDirective;
import com.example.entity.Family;
import com.example.entity.Guard;
import com.example.entity.ShortFamilyMember;
import com.example.repository.DirectiveRepo;
import com.example.repository.FamilyRepo;
import com.example.repository.MainFamilyRepo;
import com.example.repository.ShortMemberRepo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

@Service
@AllArgsConstructor
@Getter
@Setter
public class FamilyServiceImp  {
    private FamilyRepo familyRepo;
    private ShortMemberRepo memberRepo;
    private GlobalFamilyService globalFamilyService;
    private GuardService guardService;
    private MainFamilyRepo mainFamilyRepo;

    public void addChangesToFamilyInfo(Family family, String husbandInfo, String
            wifeInfo) {
        if (husbandInfo != null) family.setHusbandInfo(husbandInfo);
        if (wifeInfo != null) family.setWifeInfo(wifeInfo);
    }
@Transactional
    public Family creatFreeFamily(String fatherInfo, String motherInfo, String externId) {
        Family primeFamily = new Family();
        primeFamily.setGuard(new HashSet<>());
        primeFamily.setFamilyMembers(new HashSet<>());
        primeFamily.setExternID(externId);
        primeFamily.setChildren(new HashSet<>());
        if (fatherInfo != null) primeFamily.setHusbandInfo(fatherInfo);
        if (motherInfo != null) primeFamily.setWifeInfo(motherInfo);
        mainFamilyRepo.persistNewFamily(primeFamily);
        return primeFamily;
    }

    @Transactional
    public Family creatOrFindFamilyByInfo(ShortFamilyMember member) {
        if ((member.getFatherInfo() != null
                && member.getMotherInfo() != null) &&
                ((member.getFatherInfo().charAt(0) != '(' || member.getFatherInfo().charAt(1) == 'A') ||
                        (member.getMotherInfo().charAt(0) != '(' || member.getMotherInfo().charAt(1) == 'A'))) {
            String externId = member.getFatherInfo().concat(member.getMotherInfo());
            System.out.println("Поиск семьи по инфо");
            Family family=mainFamilyRepo.findFamilyWithAllGuardsByExternId(externId);
            return (family==null)?creatFreeFamily(member.getFatherInfo(), member.getMotherInfo(), externId):family;
        } else
            return creatFreeFamily(member.getFatherInfo(), member.getMotherInfo(), member.getUuid().toString());
    }

    @Transactional
    public void mergeFamilies(Family donor, Family merged) {
        if (donor.getGlobalFamily() != merged.getGlobalFamily()) {
            if (donor.getGlobalFamily().getNumber() > merged.getGlobalFamily().getNumber())
                merged.setGlobalFamily(globalFamilyService.mergeGlobalFamilies(donor.getGlobalFamily(), merged.getGlobalFamily()));
            else
                merged.setGlobalFamily(globalFamilyService.mergeGlobalFamilies(merged.getGlobalFamily(), donor.getGlobalFamily()));
        }
        System.out.println(merged.getGlobalFamily().getNumber());
        merged.getFamilyMembers().addAll(donor.getFamilyMembers());
        if (donor.getGuard() != null) merged.getGuard().addAll(donor.getGuard());
        merged.getChildren().addAll(donor.getChildren());
        if (donor.getHalfChildrenByMother() != null && !donor.getHalfChildrenByMother().isEmpty()) {
            if (merged.getHalfChildrenByMother() != null)
                merged.getHalfChildrenByMother().addAll(donor.getHalfChildrenByMother());
            else merged.setHalfChildrenByMother(donor.getHalfChildrenByMother());
        }
        if (donor.getHalfChildrenByFather() != null && !donor.getHalfChildrenByFather().isEmpty()) {
            if (merged.getHalfChildrenByFather() != null)
                merged.getHalfChildrenByFather().addAll(donor.getHalfChildrenByFather());
            else merged.setHalfChildrenByFather(donor.getHalfChildrenByFather());
        }
        System.out.println("111");

        for (ShortFamilyMember child :
                merged.getChildren()) {
            if (merged.getHalfChildrenByFather() != null)
                merged.getHalfChildrenByFather().remove(child);
            if (merged.getHalfChildrenByMother() != null)
                merged.getHalfChildrenByMother().remove(child);
        }
        System.out.println("222");
        if (donor.getChildrenInLow() != null && !donor.getChildrenInLow().isEmpty()) {
            if (merged.getChildrenInLow() != null)
                merged.getChildrenInLow().addAll(donor.getChildrenInLow());
            else merged.setChildrenInLow(donor.getChildrenInLow());
        }
        System.out.println("333");
    }

    @Transactional
    public void addChangesFromFather(Family primeFamily,
                                     Family family,
                                     ShortFamilyMember mainMember,
                                     ShortFamilyMember member) {
        primeFamily.setHusband(member);
        primeFamily.getFamilyMembers().add(member);
        primeFamily.setHusbandInfo(member.getFullName());
        if (primeFamily.getWifeInfo() != null)
            primeFamily.setExternID(primeFamily.getHusbandInfo().concat(primeFamily.getWifeInfo()));
        addPersonParentsToFamilyParents(primeFamily, member);
        Set<Family> familiesOfBrothersByFather = familyRepo.findAllByHusband(member);
        if (!familiesOfBrothersByFather.isEmpty()) {
            if (primeFamily.getHalfChildrenByFather() == null)
                primeFamily.setHalfChildrenByFather(new HashSet<>());
            for (Family fam :
                    familiesOfBrothersByFather) {
                if (!fam.equals(primeFamily)) {
                    primeFamily.getHalfChildrenByFather().addAll(fam.getChildren());
                    primeFamily.getFamilyMembers().addAll(fam.getChildren());
                    if (fam.getHalfChildrenByFather() == null)
                        fam.setHalfChildrenByFather(new HashSet<>());
                    fam.getHalfChildrenByFather().add(mainMember);
                    fam.getFamilyMembers().add(mainMember);
                }
            }
        }
        guardService.addGuardParentsFamilyToFamily(family,primeFamily);
        mainMember.setFather(member);
        if (member.getChilds() == null) member.setChilds(new HashSet<>());
        member.getChilds().add(mainMember);

        familyRepo.saveAll(familiesOfBrothersByFather);
    }

    @Transactional
    public void addChangesFromMother(Family primeFamily,
                                     Family family,
                                     ShortFamilyMember mainMember,
                                     ShortFamilyMember member) {
        primeFamily.setWife(member);
        primeFamily.getFamilyMembers().add(member);
        primeFamily.setWifeInfo(member.getFullName());
        if (primeFamily.getHusbandInfo() != null)
            primeFamily.setExternID(primeFamily.getHusbandInfo().concat(primeFamily.getWifeInfo()));
        addPersonParentsToFamilyParents(primeFamily, member);
        Set<Family> familiesOfBrothersByMother = familyRepo.findAllByWife(member);
        if (!familiesOfBrothersByMother.isEmpty()) {
            if (primeFamily.getHalfChildrenByMother() == null)
                primeFamily.setHalfChildrenByMother(new HashSet<>());
            for (Family fam :
                    familiesOfBrothersByMother) {
                if (!fam.equals(primeFamily)) {
                    if (fam.getHalfChildrenByMother() == null)
                        fam.setHalfChildrenByMother(new HashSet<>());
                    primeFamily.getHalfChildrenByMother().addAll(fam.getChildren());
                    primeFamily.getFamilyMembers().addAll(fam.getChildren());
                    fam.getHalfChildrenByMother().add(mainMember);
                    fam.getFamilyMembers().add(mainMember);
                }
            }
        }
        guardService.addGuardParentsFamilyToFamily(family,primeFamily);
        mainMember.setMother(member);
        if (member.getChilds() == null) member.setChilds(new HashSet<>());
        member.getChilds().add(mainMember);
        familyRepo.saveAll(familiesOfBrothersByMother);
    }

    @Transactional
    public void addPersonParentsToFamilyParents(Family family, ShortFamilyMember member) {
        if (member.getFather() != null) {
            if (family.getParents() == null) family.setParents(new HashSet<>());
            family.getParents().add(member.getFather());
            family.getFamilyMembers().add(member.getFather());
            if (member.getFather().getLinkedGuard() != null)
                guardService.addGuardToFamily(member.getFather().getLinkedGuard(), family);
        }
        if (member.getMother() != null) {
            if (family.getParents() == null) family.setParents(new HashSet<>());
            family.getParents().add(member.getMother());
            family.getFamilyMembers().add(member.getMother());
            if (member.getMother().getLinkedGuard() != null)
                guardService.addGuardToFamily(member.getMother().getLinkedGuard(), family);
        }

    }

    @Transactional
    public Set<Family> addGrandLinks(Set<ShortFamilyMember> grandChilds, ShortFamilyMember primeMember) {
        Set<Family> grandChildFamilies = new HashSet<>();
        if (grandChilds != null && !grandChilds.isEmpty()) {
            for (ShortFamilyMember grandChild : grandChilds) {
                Family grand = grandChild.getFamilyWhereChild();
                if (grand.getParents() == null) grand.setParents(new HashSet<>());
                grand.getParents().add(primeMember);
                grand.getFamilyMembers().add(primeMember);
                primeMember.getFamilies().add(grand);
                if (primeMember.getLinkedGuard() != null)
                    guardService.addGuardToFamily(primeMember.getLinkedGuard(), grand);
                grandChildFamilies.add(grand);
            }
        }
        return grandChildFamilies;
    }

    @Transactional
    public void ejectionPersonFromFamily(ShortFamilyMember familyMember, Family family) {
        Family newFamily = creatFreeFamily(familyMember.getFatherInfo(), familyMember.getMotherInfo(), familyMember.getUuid().toString());
        family.getChildren().remove(familyMember);
        newFamily.getChildren().add(familyMember);
        if (familyMember.getLinkedGuard() != null) {
            family.getGuard().remove(familyMember.getLinkedGuard());
            newFamily.getGuard().add(familyMember.getLinkedGuard());
        }
        addPersonParentsToFamilyParents(newFamily, familyMember);
        if (familyMember.getMother() != null) {
            addChangesFromMother(newFamily, familyMember.getMother().getFamilyWhereChild(), familyMember, familyMember.getMother());
            newFamily.setGlobalFamily(family.getGlobalFamily());
        }
        if (familyMember.getFather() != null) {
            addChangesFromFather(newFamily, familyMember.getFather().getFamilyWhereChild(), familyMember, familyMember.getFather());
            newFamily.setGlobalFamily(family.getGlobalFamily());
        }
        if (familyMember.getMother() == null && familyMember.getFather() == null && familyMember.getMotherInfo() == null && familyMember.getFatherInfo() == null) {
            globalFamilyService.creatNewGlobalFamily(newFamily);
            fullEjectionPersonWithKin(familyMember);
        }
        familyRepo.save(newFamily);
        memberRepo.save(familyMember);


    }

    @Transactional
    public void fullEjectionPersonWithKin(ShortFamilyMember familyMember) {
        // Реализаия полного удаления члена семьи из рода, включая разделение на 2 глобальных семьи и полный разрыв связи по роду
    }
}
