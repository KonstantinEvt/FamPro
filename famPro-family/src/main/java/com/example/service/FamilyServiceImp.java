package com.example.service;

import com.example.dtos.Directive;
import com.example.dtos.FamilyDirective;
import com.example.entity.Family;
import com.example.entity.ShortFamilyMember;
import com.example.repository.DirectiveRepo;
import com.example.repository.FamilyRepo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.LinkedList;

@Service
@AllArgsConstructor
@Getter
@Setter
public class FamilyServiceImp implements FamilyService {
    private FamilyRepo familyRepo;
    private GlobalFamilyService globalFamilyService;

    @Override
    public void addChangesToFamilyInfo(Family family, String husbandInfo, String
            wifeInfo) {
        if (husbandInfo != null) family.setHusbandInfo(husbandInfo);
        if (wifeInfo != null) family.setWifeInfo(wifeInfo);
    }

    public Family creatFreeFamily(String fatherInfo, String motherInfo, String externId) {
        Family primeFamily = new Family();
        primeFamily.setGuard(new HashSet<>());
        primeFamily.setFamilyMembers(new HashSet<>());
        primeFamily.setExternID(externId);
        primeFamily.setChildren(new HashSet<>());
        if (fatherInfo != null) primeFamily.setHusbandInfo(fatherInfo);
        if (motherInfo != null) primeFamily.setWifeInfo(motherInfo);
        System.out.println("Новая семья создана");
        return primeFamily;
    }

    @Transactional
    @Override
    public Family creatOrFindFamilyByInfo(String fatherInfo, String motherInfo, String uuidChild) {
        if ((fatherInfo != null
                && motherInfo != null) &&
                ((fatherInfo.charAt(0) != '(' || fatherInfo.charAt(1) == 'A') ||
                        (motherInfo.charAt(0) != '(' || motherInfo.charAt(1) == 'A'))) {
            String externId = fatherInfo.concat(motherInfo);
            System.out.println("Поиск семьи по инфо");
            return familyRepo.findFirstByExternID(externId).orElseGet(() -> creatFreeFamily(fatherInfo, motherInfo, externId));
        } else
            return familyRepo.findFirstByExternID(uuidChild).orElseGet(() -> creatFreeFamily(fatherInfo, motherInfo, uuidChild));
    }

    @Transactional
    public void mergeFamilies(Family donor, Family merged) {
        if (donor.getGlobalFamily() != merged.getGlobalFamily()) {
            if (donor.getGlobalFamily().getNumber() > merged.getGlobalFamily().getNumber())
                merged.setGlobalFamily(globalFamilyService.mergeGlobalFamilies(donor.getGlobalFamily(), merged.getGlobalFamily()));
            else
                merged.setGlobalFamily(globalFamilyService.mergeGlobalFamilies(merged.getGlobalFamily(), donor.getGlobalFamily()));
        }
        merged.getGlobalFamily().setNumber(merged.getGlobalFamily().getNumber() - 1);
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
}
