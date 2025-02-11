package com.example.service;


import com.example.dtos.FamilyDirective;
import com.example.dtos.FamilyMemberDto;
import com.example.dtos.TokenUser;
import com.example.entity.DeferredDirective;
import com.example.entity.Family;
import com.example.entity.Guard;
import com.example.entity.ShortFamilyMember;
import com.example.enums.CheckStatus;
import com.example.enums.KafkaOperation;
import com.example.enums.Sex;
import com.example.enums.SwitchPosition;
import com.example.repository.DirectiveRepo;
import com.example.repository.FamilyRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
@Log4j2
public class IncomingService {
    private final FamilyRepo familyRepo;
    private final MemberService memberService;
    private final GlobalFamilyService globalFamilyService;
    private final DirectiveRepo directiveRepo;
    private final GuardService guardService;
    private final FamilyService familyService;


    @Transactional
    public void checkFamilyDirectives(LinkedList<FamilyDirective> directiveLinkedList) {
        Family primeFamily = null;
        String possibleId;
        Optional<Family> possiblePrime;
        FamilyDirective mainDirective = directiveLinkedList.pollLast();
        System.out.println(mainDirective);
        assert mainDirective != null;
        FamilyMemberDto mainDto = mainDirective.getFamilyMemberDto();
        Optional<Guard> guardFromDirective = guardService.findGuard(mainDirective.getTokenUser());
        System.out.println("Straza naidena");
        ShortFamilyMember mainMember = null;
        Set<Family> childFamilies = new HashSet<>();
        Set<Family> grandChildFamilies = new HashSet<>();
        Set<ShortFamilyMember> acceptedChild = new HashSet<>();
        List<Family> familiesToRemove = new ArrayList<>();
        List<DeferredDirective> directiveList = new ArrayList<>();

        if (mainDirective.getOperation() == KafkaOperation.RENAME) {
            mainMember = memberService.getShortMemberRepo().findByUuid(UUID.fromString(mainDirective.getPerson())).orElseThrow(() -> new RuntimeException("не найден"));
            primeFamily = mainMember.getFamilyWhereChild();
            if (checkFamilyForGuard(primeFamily, guardFromDirective) || mainDto.getCheckStatus() == CheckStatus.CHECKED) {
                memberService.editFamilyMember(mainDto, mainMember);
                if ((mainDto.getFatherInfo() != null
                        && mainDto.getMotherInfo() != null) &&
                        ((mainDto.getFatherInfo().charAt(0) != '(' || mainDto.getFatherInfo().charAt(1) == 'A') ||
                                (mainDto.getMotherInfo().charAt(0) != '(' || mainDto.getMotherInfo().charAt(1) == 'A'))
                        && (!Objects.equals(mainDto.getFatherInfo(), primeFamily.getHusbandInfo()) || !Objects.equals(mainDto.getMotherInfo(), primeFamily.getWifeInfo()))) {
                    possibleId = mainDto.getFatherInfo().concat(mainDto.getMotherInfo());
                    System.out.println("попытка найти существующую семью");
                    possiblePrime = familyRepo.findFirstByExternID(possibleId);
                    if (possiblePrime.isPresent()) {
                        System.out.println("И мы ее нашли");
                        if (checkFamilyForGuard(possiblePrime.get(), guardFromDirective)) {
                            System.out.println("Сейчас будем мержить семьи");
                            possiblePrime.get().getChildren().add(mainMember);
                            familyService.mergeFamilies(primeFamily, possiblePrime.get());
                            familyRepo.delete(primeFamily);
                            System.out.println("444");
                            primeFamily = possiblePrime.get();
                        } else {
                            directiveList.add(DeferredDirective
                                    .builder()
                                    .directiveFamily(primeFamily)
                                    .directiveMember(mainMember)
                                    .enternId(possibleId)
                                    .processFamily(possiblePrime.get())
                                    .directiveGuard(guardFromDirective.orElse(null))
                                    .switchPosition(SwitchPosition.MAIN)
                                    .globalFor(primeFamily.getGlobalFamily())
                                    .globalTo(possiblePrime.get().getGlobalFamily())
                                    .build());
                        }// запрос на слияние с кровными братьями
                    } else {
                        primeFamily.setExternID(possibleId);
                        System.out.println("Но мы ее не нашли");
                    }
                }
                familyService.addChangesToFamilyInfo(primeFamily, mainDto.getFatherInfo(), mainMember.getMotherInfo());
            } else throw new RuntimeException("редактирование запрещено");
        }
        if (mainDirective.getOperation() == KafkaOperation.ADD) {
            primeFamily = familyService.creatOrFindFamilyByInfo(mainDto.getFatherInfo(), mainDto.getMotherInfo(), mainDto.getUuid().toString());
            System.out.println("прайм семья определена");
            mainMember = memberService.addFamilyMember(mainDto);
            System.out.println("OOOOOOOOOOOOOOOOJJJJJJJJ1");
            if (!checkFamilyForGuard(primeFamily, guardFromDirective)) {
                System.out.println("OOOOOOOOOOOOOOOOJJJJJJJJ3");
                possiblePrime = Optional.of(primeFamily);
                primeFamily = familyService.creatFreeFamily(mainDto.getFatherInfo(), mainDto.getMotherInfo(), mainDto.getUuid().toString());
                directiveList.add(DeferredDirective
                        .builder()
                        .directiveFamily(primeFamily)
                        .directiveMember(mainMember)
                        .enternId(possiblePrime.get().getExternID())
                        .processFamily(possiblePrime.get())
                        .directiveGuard(guardFromDirective.orElse(null))
                        .switchPosition(SwitchPosition.MAIN)
                        .globalFor(primeFamily.getGlobalFamily())
                        .globalTo(possiblePrime.get().getGlobalFamily())
                        .build());
            }
//            if (primeFamily.getChildren().isEmpty()) {
//                primeFamily.getGlobalFamily().setNumber(primeFamily.getGlobalFamily().getNumber() + 1);
//            }

        }
        System.out.println("555");
        assert primeFamily != null;

        primeFamily.getFamilyMembers().add(mainMember);
        mainMember.getFamilies().add(primeFamily);
        mainMember.setFamilyWhereChild(primeFamily);
        familyRepo.save(primeFamily);

        if (primeFamily.getGlobalFamily() == null) {
            globalFamilyService.creatNewGlobalFamily(primeFamily);
        }
        if (guardFromDirective.isPresent()
                && guardFromDirective.get().getLinkedPerson().getUuid().equals(mainDto.getUuid())) {
            guardService.addGuardToFamily(guardFromDirective.get(), primeFamily);
            guardService.addGuardToGlobalFamily(guardFromDirective.get(), primeFamily.getGlobalFamily());
        }

        while (!directiveLinkedList.isEmpty()) {
            System.out.println("обработка едит директив");
            FamilyDirective processDirective = directiveLinkedList.pollFirst();
            assert processDirective != null;
            ShortFamilyMember member = memberService.getShortMemberRepo().findByUuid(processDirective.getFamilyMemberDto().getUuid())
                    .orElseThrow(() -> new RuntimeException("Anomaly"));
            Family family = member.getFamilyWhereChild();
            boolean existBloodBrother = !childFamilies.isEmpty() && childFamilies.contains(family);
            if (existBloodBrother || checkFamilyForGuard(family, guardFromDirective)) {
                if (!existBloodBrother) {
//                    if (primeFamily.getGlobalFamily() != null) {
                    if (family.getGlobalFamily().getNumber() > primeFamily.getGlobalFamily().getNumber())
                        primeFamily.setGlobalFamily(globalFamilyService.mergeGlobalFamilies(family.getGlobalFamily(), primeFamily.getGlobalFamily()));
                    else
                        family.setGlobalFamily(globalFamilyService.mergeGlobalFamilies(primeFamily.getGlobalFamily(), family.getGlobalFamily()));
//                    } else {
//                        globalFamilyService.addFreeFamilyToGlobalFamily(primeFamily, family.getGlobalFamily());
//                    }
                }
                switch (processDirective.getSwitchPosition()) {
                    case CHILD -> {
                        acceptedChild.add(member);
                        if (!existBloodBrother) {
                            childFamilies.add(family);
                            if (mainMember.getSex() == Sex.MALE) {
                                family.setHusbandInfo(mainMember.getFullName());
                                family.setHusband(mainMember);
                                family.getFamilyMembers().add(mainMember);
                            } else {
                                family.setWifeInfo(mainMember.getFullName());
                                family.setWife(mainMember);
                                family.getFamilyMembers().add(mainMember);
                            }

                            if (primeFamily.getGuard() != null) for (Guard guard :
                                    primeFamily.getGuard()) {
                                guardService.addGuardToFamily(guard, family);
                            }

                            if (family.getHusbandInfo() != null && family.getWifeInfo() != null)
                                family.setExternID(family.getHusbandInfo().concat(family.getWifeInfo()));
                            addPersonParentsToFamilyParents(family, mainMember);
                            for (Family fam :
                                    childFamilies) {
                                if (fam.getExternID().equals(family.getExternID()) && fam != family) {
                                    familyService.mergeFamilies(family, fam);
                                    familiesToRemove.add(family);
                                }
                            }
                            familyRepo.save(family);
                        }
                        if (member.getChilds() != null) {
                            for (ShortFamilyMember grandChild :
                                    member.getChilds()) {
                                Family grand = grandChild.getFamilyWhereChild();
                                if (grand.getParents() == null) grand.setParents(new HashSet<>());
                                grand.getParents().add(mainMember);
                                grand.getFamilyMembers().add(mainMember);
                                mainMember.getFamilies().add(grand);
                                if (mainMember.getLinkedGuard() != null)
                                    guardService.addGuardToFamily(mainMember.getLinkedGuard(), grand);
                                grandChildFamilies.add(grand);
                            }
                        }
                        memberService.addChildToFamilyMember(mainMember, member);
                        memberService.getShortMemberRepo().save(member);

                    }
                    case FATHER -> {
                        primeFamily.setHusband(member);
                        primeFamily.getFamilyMembers().add(member);
                        primeFamily.setHusbandInfo(member.getFullName());
                        if (primeFamily.getWifeInfo() != null)
                            primeFamily.setExternID(primeFamily.getHusbandInfo().concat(primeFamily.getWifeInfo()));
                        addPersonParentsToFamilyParents(primeFamily, member);
                        Set<Family> familiesOfBrothersByFather = familyRepo.findAllByHusband(member);
                        if (familiesOfBrothersByFather.size() > 1) {
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
                        if (family.getGuard() != null) for (Guard guard :
                                family.getGuard()) {
                            guardService.addGuardToFamily(guard, primeFamily);
                        }
                        mainMember.setFather(member);
                        if (member.getChilds() == null) member.setChilds(new HashSet<>());
                        member.getChilds().add(mainMember);

                        familyRepo.saveAll(familiesOfBrothersByFather);
                    }
                    case MOTHER -> {
                        primeFamily.setWife(member);
                        primeFamily.getFamilyMembers().add(member);
                        primeFamily.setWifeInfo(member.getFullName());
                        if (primeFamily.getHusbandInfo() != null)
                            primeFamily.setExternID(primeFamily.getHusbandInfo().concat(primeFamily.getWifeInfo()));
                        addPersonParentsToFamilyParents(primeFamily, member);
                        Set<Family> familiesOfBrothersByMother = familyRepo.findAllByWife(member);
                        if (familiesOfBrothersByMother.size() > 1) {
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
                        if (family.getGuard() != null) for (Guard guard :
                                family.getGuard()) {
                            guardService.addGuardToFamily(guard, primeFamily);
                        }
                        mainMember.setMother(member);
                        if (member.getChilds() == null) member.setChilds(new HashSet<>());
                        member.getChilds().add(mainMember);
                        familyRepo.saveAll(familiesOfBrothersByMother);
                    }
                    default -> log.warn("Обнаружена нераспознанная директива");
                }
            } else {
                directiveList.add(DeferredDirective
                        .builder()
                        .directiveFamily(primeFamily)
                        .directiveMember(mainMember)
                        .enternId(family.getExternID())
                        .processFamily(family)
                        .directiveGuard(guardFromDirective.orElse(null))
                        .switchPosition(processDirective.getSwitchPosition())
                        .globalFor(primeFamily.getGlobalFamily())
                        .globalTo(family.getGlobalFamily())
                        .build());
            }
        }
        if (!acceptedChild.isEmpty()) for (ShortFamilyMember child :
                acceptedChild) {
            for (Family childFM :
                    childFamilies) {
                if (!childFM.getChildren().contains(child))
                    if (mainDto.getSex() == Sex.MALE) childFM.getHalfChildrenByFather().add(child);
                    else childFM.getHalfChildrenByMother().add(child);
            }
        }
        familyRepo.deleteAll(familiesToRemove);
        memberService.getShortMemberRepo().save(mainMember);
        if (!childFamilies.isEmpty()) familyRepo.saveAll(childFamilies);
        System.out.println(grandChildFamilies.size());
        if (!grandChildFamilies.isEmpty()) familyRepo.saveAll(grandChildFamilies);
        familyRepo.save(primeFamily);
        directiveRepo.saveAll(directiveList);
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

    public boolean checkFamilyForGuard(Family family, Optional<Guard> directiveGuard) {
        System.out.println("OOOOOOOOOOOOOOOOJJJJJJJJ2");
        return (directiveGuard.isPresent() && (family.getGuard() == null || family.getGuard().contains(directiveGuard.get()))
                || ((family.getGuard() == null || family.getGuard().isEmpty()) && (family.getGlobalFamily() == null || family.getGlobalFamily().getGuard() == null || family.getGlobalFamily().getGuard().isEmpty()))
                || ((family.getGuard() == null || family.getGuard().isEmpty())
                && directiveGuard.isPresent()
                && family.getGlobalFamily().getGuard().contains(directiveGuard.get())));
    }

    @Transactional
    public void addGuardByLink(FamilyMemberDto familyMemberDto, TokenUser tokenUser) {
        ShortFamilyMember shortFamilyMember = memberService.getShortMemberRepo().findByUuid(familyMemberDto.getUuid()).orElseThrow(() -> new RuntimeException("не найден человек"));
        Guard guard = guardService.creatGuard(shortFamilyMember, (String) tokenUser.getClaims().get("sub"));
        System.out.println("Стража создана");
        guardService.addGuardToFamilies(shortFamilyMember.getFamilies(), guard);
        guardService.addGuardToGlobalFamily(guard, shortFamilyMember.getFamilyWhereChild().getGlobalFamily());
        familyRepo.saveAll(shortFamilyMember.getFamilies());
    }

}


