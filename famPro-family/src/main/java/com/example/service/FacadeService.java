package com.example.service;


import com.example.dtos.*;
import com.example.entity.*;
import com.example.enums.*;
import com.example.repository.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service

@Log4j2
public class FacadeService implements SimpleFamilyService {
    private final FamilyRepo familyRepo;
    private final MemberService memberService;
    private final DirectiveRepo directiveRepo;
    private final GuardService guardService;
    private final FamilyServiceImp familyService;
    private final DirectiveService directiveService;
    private final List<DirectiveGuards> directiveGuardsList;
    private final List<DirectiveGuards> contactDirective;
    private final MemberRepository memberRepository;
    private final FamilyRepository familyRepository;
    private final SendAndFormService sendAndFormService;


    public FacadeService(FamilyRepo familyRepo,
                         MemberService memberService,
                         DirectiveRepo directiveRepo,
                         GuardService guardService,
                         FamilyServiceImp familyService,
                         DirectiveService directiveService,
                         @Qualifier("directiveGuards") List<DirectiveGuards> directiveGuardsList,
                         List<DirectiveGuards> contactDirective, MemberRepository memberRepository, FamilyRepository familyRepository, SendAndFormService sendAndFormService) {
        this.familyRepo = familyRepo;
        this.memberService = memberService;
        this.directiveRepo = directiveRepo;
        this.guardService = guardService;
        this.familyService = familyService;
        this.directiveService = directiveService;
        this.directiveGuardsList = directiveGuardsList;
        this.contactDirective = contactDirective;
        this.memberRepository = memberRepository;
        this.familyRepository = familyRepository;
        this.sendAndFormService = sendAndFormService;
    }

    @Transactional
    public void checkFamilyDirectives(LinkedList<FamilyDirective> directiveLinkedList) {
        FamilyDirective mainDirective = directiveLinkedList.pollLast();
        if (mainDirective == null || mainDirective.getFamilyMemberDto() == null)
            throw new RuntimeException("Directive is corrupt");
        ShortFamilyMember mainMember;
        FamilyMemberDto mainDto = mainDirective.getFamilyMemberDto();
        if (mainDirective.getOperation() != KafkaOperation.ADD)
            mainMember = memberRepository.findMemberWithPrimeFamily(UUID.fromString(mainDirective.getPerson())).orElseThrow(() -> new RuntimeException("Person not found"));
        else mainMember = memberService.addFamilyMember(mainDto);
        Family primeFamily = null;
        UUID possibleId;
        Optional<Family> possiblePrime;
        Changing changing = new Changing();
        Optional<Guard> guardFromDirective = guardService.findGuardWithLinkingPerson(mainDirective.getTokenUser());
        log.info("finding guard is exist: {}", guardFromDirective.isPresent() ? guardFromDirective.get().getTokenUser() : "not linking user");
        if (guardFromDirective.isEmpty() && Objects.equals(mainMember.getCreator(), mainDirective.getTokenUser()))
            guardFromDirective = Optional.of(new Guard(null, mainMember.getCreator(), mainMember));
        Set<ShortFamilyMember> ancestors;
        Set<ShortFamilyMember> potentialBloodBrothers;

        Set<Family> childFamilies = new HashSet<>();
        Set<ShortFamilyMember> acceptedChild = new HashSet<>();

        List<DeferredDirective> directiveList = new ArrayList<>();
        Set<String> tempModerate = new HashSet<>();
        CheckStatus tempChecked = CheckStatus.MODERATE;
        boolean primeFamilySetup = false;

        if (mainDirective.getOperation() == KafkaOperation.RENAME) {
            log.info("changing member with uuid: {}", mainMember.getUuid().toString());
            if (mainMember.getCheckStatus() == CheckStatus.MODERATE && !mainDirective.getTokenUser().equals("moderator")) {
                log.warn("Попытка изменения человека находящимся под голосованием или модерацией");
                sendAndFormService.sendAttentionOfModerate(mainDirective.getTokenUser(), mainMember.getFullName(), mainDirective.getSwitchPosition());
// тут нужен откат в storage модуль и смена статуса там на Moderate
                return;
            } else {
                tempChecked = mainMember.getCheckStatus();
                mainMember.setCheckStatus(CheckStatus.MODERATE);
            }
            primeFamily = mainMember.getFamilyWhereChild();
            Set<ShortFamilyMember> topAncestors;
            if (mainMember.getTopAncestors() == null || mainMember.getTopAncestors().isBlank()) {
                topAncestors = new HashSet<>();
                topAncestors.add(mainMember);
            } else {
                ancestors = memberService.getAllAncestors(mainMember);
                topAncestors = ancestors.stream().filter(x -> x.getAncestors() == null || x.getAncestors().isBlank()).collect(Collectors.toSet());
            }
            if (mainDirective.getTokenUser().equals("moderator") || memberService.checkThemeSecretForSecretLevel(mainMember.getSecretLevelEdit(), mainMember, guardFromDirective, topAncestors)) {
                changing.setChangeIsPresent(true);
                if (primeFamily == null) {
                    changing.setChangingMain(true);
                    changing.setChangingFather(getChangingStatus(mainDto.getFatherInfo(), mainMember.getFatherInfo()));
                    if (changing.getChangingFather().ordinal() > 4) {
                        if (changing.getChangingFather() != ChangingStatus.LIGHT_FREE)
                            memberService.removeLinkWithParent(null, mainMember, memberService.getMemberByUuid(mainMember.getFatherUuid()).orElseThrow(() -> new RuntimeException("Strong error in database")));
                        changing.setChangingFather(familyService.changeChangingStatusAfterRemove(changing.getChangingFather()));

                    }
                    changing.setChangingMother(getChangingStatus(mainDto.getMotherInfo(), mainMember.getMotherInfo()));
                    if (changing.getChangingMother().ordinal() > 4) {
                        if (changing.getChangingMother() != ChangingStatus.LIGHT_FREE)
                            memberService.removeLinkWithParent(null, mainMember, memberService.getMemberByUuid(mainMember.getMotherUuid()).orElseThrow(() -> new RuntimeException("Strong error in database")));
                        changing.setChangingMother(familyService.changeChangingStatusAfterRemove(changing.getChangingMother()));
                    }
                    memberService.editFamilyMember(mainDto, mainMember);
                    memberService.changeUuidInBloodTree(UUID.fromString(mainDirective.getPerson()), mainMember);
                    mainMember.setMotherUuid(null);
                    mainMember.setFatherUuid(null);
                    changing.setOneChildInFamily(true);
                    mainDirective.setOperation(KafkaOperation.ADD);
                    log.warn("Error database in: member <-> primeFamily. Trying aid.");
                } else {
                    changing.setChangingMain(mainMember.getUuid() != mainDto.getUuid());
                    changing.setChangingFather(getChangingStatus(mainDto.getFatherInfo(), primeFamily.getHusbandInfo()));
                    changing.setChangingMother(getChangingStatus(mainDto.getMotherInfo(), primeFamily.getWifeInfo()));
                    memberService.editFamilyMember(mainDto, mainMember);
                    if (changing.isChangingMain())
                        memberService.changeUuidInBloodTree(UUID.fromString(mainDirective.getPerson()), mainMember);
                    if (primeFamily.getChildren().size() == 1) changing.setOneChildInFamily(true);

                    if (changing.getChangingFather().ordinal() > 4 || changing.getChangingMother().ordinal() > 4) {
                        if (mainDirective.getTokenUser().equals("moderator") || memberService.checkThemeSecretForSecretLevel(mainMember.getSecretLevelRemove(), mainMember, guardFromDirective, topAncestors)) {
                            primeFamily = familyService.changeFamilyByRemoveParentLink(changing, primeFamily, mainMember, mainDto);
                        } else throw new RuntimeException("Haven't rights to change/remove parents");
//                        тут нужен откат в модуль Storage
                    }
                    if ((changing.getChangingMother().ordinal() < 4 && changing.getChangingFather().ordinal() < 2) ||
                            (changing.getChangingMother().ordinal() < 2 && changing.getChangingFather().ordinal() < 4) ||
                            (changing.getChangingMother().ordinal() == 2 && changing.getChangingFather().ordinal() == 2)) {

                        primeFamilySetup = true;
                        log.info("member {} is success updated", mainMember.getUuid().toString());
                    }
                    if (!primeFamilySetup && changing.getChangingFather().ordinal() > 1 && changing.getChangingMother().ordinal() > 1) {
                        possibleId = UUID.nameUUIDFromBytes(mainDto.getFatherInfo().concat(mainDto.getMotherInfo()).getBytes());
                        possiblePrime = familyRepository.findFamilyWithChildrenByUUID(possibleId);
                        if (possiblePrime.isPresent()) {
                            log.info("prime family is found");
                            potentialBloodBrothers = possiblePrime.get().getChildren();
                            if (mainDirective.getTokenUser().equals("moderator") || checkFamilyForGuard(changing, possiblePrime.get(), guardFromDirective, potentialBloodBrothers)) {
                                log.info("check successful happened");
                                familyService.mergeFamilies(primeFamily, possiblePrime.get());
                                primeFamily = possiblePrime.get();
                                changing.setOneChildInFamily(false);
                            } else {
                                for (ShortFamilyMember brother :
                                        potentialBloodBrothers)
                                    if (brother.getCheckStatus() != CheckStatus.MODERATE) {
                                        tempModerate.add(brother.getUuid().toString());
                                        brother.setCheckStatus(CheckStatus.MODERATE);
                                    }
                                if (changing.getChangingMother().ordinal() > 2)
                                    memberService.clearParentInfo(mainMember, SwitchPosition.MOTHER);
                                if (changing.getChangingFather().ordinal() > 2)
                                    memberService.clearParentInfo(mainMember, SwitchPosition.FATHER);
                                directiveList.add(DeferredDirective
                                        .builder()
                                        .created(new Timestamp(System.currentTimeMillis()))
                                        .directiveFamily(primeFamily)
                                        .directiveMember(mainMember)
                                        .shortFamilyMemberLink(potentialBloodBrothers.stream().findFirst().orElseThrow(() -> new RuntimeException("nonsence!!")))
                                        .info(possibleId.toString())
                                        .processFamily(possiblePrime.get())
                                        .tokenUser(mainDirective.getTokenUser())
                                        .switchPosition(SwitchPosition.MAIN)
                                        .build());
                            }
                        } else {
                            log.info("family for merging is not found");
                            familyService.changeMainFamilyIdentification(primeFamily, possibleId, mainMember, Optional.empty(), Optional.empty());

                        }
                    } else if (!primeFamilySetup) {
                        log.info("family changing Main Identification");
                        familyService.changeMainFamilyIdentification(primeFamily, mainMember.getUuid(), mainMember, Optional.empty(), Optional.empty());
                    }
                }
            } else {
                if (mainMember.getCheckStatus() == CheckStatus.UNCHECKED) {
                    memberService.uncheckedMergeInfo(mainMember.getUuid(), mainDto.getMemberInfo());
                    return;
                }
                log.warn("Попытка изменения человека под защитой");
                directiveGuardsList.add(DirectiveGuards.builder()
                        .created(new Timestamp(System.currentTimeMillis()))
                        .tokenUser(mainDirective.getTokenUser())
                        .switchPosition(mainDirective.getSwitchPosition())
                        .info1("trying changing person without rights")
                        .info2(mainMember.getFullName())
                        .build());
// тут нужен откат в storage модуль (и смена статуса?)
                return;
            }
        }
        if (mainDirective.getOperation() == KafkaOperation.ADD) {
            if (!changing.isChangeIsPresent()) {
                changing.setChangeIsPresent(true);
                changing.setChangingMain(true);
                changing.setChangingFather(getChangingStatus(mainDto.getFatherInfo(), mainMember.getFatherInfo()));
                changing.setChangingMother(getChangingStatus(mainDto.getMotherInfo(), mainMember.getMotherInfo()));
                changing.setOneChildInFamily(true);
                mainMember.setCheckStatus(CheckStatus.MODERATE);
                tempChecked = CheckStatus.UNCHECKED;
                if (changing.getChangingFather().ordinal() < 2 || changing.getChangingMother().ordinal() < 2) {
                    primeFamily = familyService.creatFreeFamily(mainDto.getFatherInfo(), mainDto.getMotherInfo(), mainDto.getUuid());
                } else {
                    possibleId = UUID.nameUUIDFromBytes(mainDto.getFatherInfo().concat(mainDto.getMotherInfo()).getBytes());
                    possiblePrime = familyRepository.findFamilyWithChildrenByUUID(possibleId);
                    if (possiblePrime.isPresent()) {
                        log.info("prime family is found");
                        potentialBloodBrothers = possiblePrime.get().getChildren();
                        if (mainDirective.getTokenUser().equals("moderator") || checkFamilyForGuard(changing, possiblePrime.get(), guardFromDirective, potentialBloodBrothers)) {
                            log.info("check successful happened");
                            primeFamily = possiblePrime.get();
                            changing.setOneChildInFamily(false);
                        } else {
                            for (ShortFamilyMember brother :
                                    potentialBloodBrothers)
                                if (brother.getCheckStatus() != CheckStatus.MODERATE) {
                                    tempModerate.add(brother.getUuid().toString());
                                    brother.setCheckStatus(CheckStatus.MODERATE);
                                }
                            if (changing.getChangingMother().ordinal() > 2)
                                memberService.clearParentInfo(mainMember, SwitchPosition.MOTHER);
                            if (changing.getChangingFather().ordinal() > 2)
                                memberService.clearParentInfo(mainMember, SwitchPosition.FATHER);
                            primeFamily = familyService.creatFreeFamily(mainDto.getFatherInfo(), mainDto.getMotherInfo(), possibleId);
                            directiveList.add(DeferredDirective
                                    .builder()
                                    .created(new Timestamp(System.currentTimeMillis()))
                                    .directiveFamily(primeFamily)
                                    .directiveMember(mainMember)
                                    .shortFamilyMemberLink(potentialBloodBrothers.stream().findFirst().orElseThrow(() -> new RuntimeException("nonsence!!")))
                                    .info(possibleId.toString())
                                    .processFamily(possiblePrime.get())
                                    .tokenUser(mainDirective.getTokenUser())
                                    .switchPosition(SwitchPosition.MAIN)
                                    .build());
                        }
                    } else {
                        log.info("family for merging is not found");
                        primeFamily = familyService.creatFreeFamily(mainDto.getFatherInfo(), mainDto.getMotherInfo(), possibleId);
                        familyService.setFamilySecretLevels(primeFamily, mainMember);
                    }
                }
                mainMember.setFamilyWhereChild(primeFamily);
            }
        }
        if (mainDirective.getOperation() == KafkaOperation.REMOVE) {
// блок про удаление перса
            log.warn("remove block is absent");
        }

        if (primeFamily == null) throw new RuntimeException("prime family is absent, but can be");
        while (!directiveLinkedList.isEmpty()) {
            FamilyDirective processDirective = directiveLinkedList.pollFirst();
            if (processDirective == null) throw new RuntimeException("corrupt directive");
            else log.info("process {} directive", processDirective.getSwitchPosition().getInfo());

            ShortFamilyMember member;
            try {
                member = memberRepository.findMemberWithPrimeFamily(processDirective.getFamilyMemberDto().getUuid()).orElseThrow(() -> new RuntimeException("member of this directive absent"));
            } catch (RuntimeException e) {
                log.warn(e.getMessage());
                continue;
            }
            Family family = member.getFamilyWhereChild();
            Set<ShortFamilyMember> memberTopAncestors = memberService.getAllTopAncestors(member);
            boolean existBloodBrother = !childFamilies.isEmpty() && childFamilies.contains(family);
            if (mainDirective.getTokenUser().equals("moderator") || Objects.equals(member.getCreator(), mainDirective.getTokenUser()) ||
                    memberService.checkThemeSecretForSecretLevel(member.getSecretLevelEdit(), member, guardFromDirective, memberTopAncestors)) {
                log.info("check successful happened");
                switch (processDirective.getSwitchPosition()) {
                    case CHILD -> {
                        acceptedChild.add(member);
                        if (!existBloodBrother) {
                            childFamilies.add(family);
                            if (mainMember.getSex() == Sex.MALE) {
                                family.setHusbandInfo(mainMember.getFullName());
                                family.setHusband(mainMember);
                            } else {
                                family.setWifeInfo(mainMember.getFullName());
                                family.setWife(mainMember);
                            }
                            family.getFamilyMembers().add(mainMember);
                            if (family.getHusbandInfo() != null && family.getWifeInfo() != null)
                                family.setUuid(UUID.nameUUIDFromBytes(family.getHusbandInfo().concat(family.getWifeInfo()).getBytes()));
                            for (Family fam :
                                    childFamilies) {
                                if (fam.getUuid().equals(family.getUuid()) && fam != family) {
                                    familyService.mergeFamilies(family, fam);
//                                    familiesToRemove.add(family);
                                }
                            }
// проверить нужно ли сохранение
//                            familyRepository.updateFamily(family);
                        }
                        memberService.addChildToFamilyMember(member, mainMember);
                        memberService.updateMember(member);
                        log.info("child is setup");

                    }
                    case FATHER -> {
                        familyService.addChangesFromFather(primeFamily, family, mainMember, member);
                        log.info("father is setup");
                    }
                    case MOTHER -> {
                        familyService.addChangesFromMother(primeFamily, family, mainMember, member);
                        log.info("mother is setup");
                    }
                    default -> log.warn("Обнаружена нераспознанная директива");
                }
            } else {
                if (member.getCheckStatus() != CheckStatus.MODERATE) {
                    member.setCheckStatus(CheckStatus.MODERATE);
                    member.setLastUpdate(new Timestamp(System.currentTimeMillis()));
                    memberService.updateMember(member);
                    tempModerate.add(member.getUuid().toString());
//                    storageDirective.add(FamilyDirective.builder()
//                            .person(member.getUuid().toString())
//                            .switchPosition(SwitchPosition.MAIN)
//                            .operation(KafkaOperation.RENAME)
//                            .build());
                }
                directiveList.add(DeferredDirective
                        .builder()
                        .directiveFamily(primeFamily)
                        .directiveMember(mainMember)
                        .created(new Timestamp(System.currentTimeMillis()))
                        .info(member.getFullName())
                        .shortFamilyMemberLink(member)
                        .processFamily(family)
                        .tokenUser(mainDirective.getTokenUser())
                        .switchPosition(processDirective.getSwitchPosition())
                        .build());
            }
        }
        if (!acceptedChild.isEmpty()) {
            for (ShortFamilyMember child :
                    acceptedChild) {
                child.setLastUpdate(new Timestamp(System.currentTimeMillis()));
                for (Family childFM :
                        childFamilies) {
                    if (!childFM.getChildren().contains(child))
                        if (mainDto.getSex() == Sex.MALE) childFM.getHalfChildrenByFather().add(child);
                        else childFM.getHalfChildrenByMother().add(child);
                }
            }
            log.info("families where {} is parent is updated", mainMember.getUuid().toString());
        }

        log.info("All free links is setup");

        if (!tempModerate.isEmpty())
            sendAndFormService.formDirectiveToStorageForChangeStatus(null, mainMember.getUuid().toString(), null, KafkaOperation.RENAME, tempModerate, CheckStatus.MODERATE);
        else mainMember.setCheckStatus(tempChecked);


        Set<ShortFamilyMember> newTopAc = memberService.getAllTopAncestors(mainMember);
        Set<UUID> geneticTreeGuards = memberService.getGeneticTreeGuards(newTopAc);
        if (!geneticTreeGuards.isEmpty()) {
            Set<String> toCheckStatus = memberService.repairGeneticTreeCheckStatus(newTopAc);
            if (!toCheckStatus.isEmpty()) {
                if (mainMember.getCheckStatus() == CheckStatus.LINKED)
                    sendAndFormService.formDirectiveToStorageForChangeStatus(null, mainMember.getUuid().toString(), SwitchPosition.FATHER, KafkaOperation.RENAME, toCheckStatus, CheckStatus.CHECKED);
                else if (mainMember.getCheckStatus() == CheckStatus.CHECKED)
                    sendAndFormService.formDirectiveToStorageForChangeStatus(null, mainMember.getUuid().toString(), SwitchPosition.MOTHER, KafkaOperation.RENAME, toCheckStatus, CheckStatus.CHECKED);
                else
                    sendAndFormService.formDirectiveToStorageForChangeStatus(null, mainMember.getUuid().toString(), null, KafkaOperation.RENAME, toCheckStatus, CheckStatus.CHECKED);
            } else if (tempModerate.isEmpty())
                sendAndFormService.formDirectiveToStorageForChangeStatus(null, mainMember.getUuid().toString(), null, KafkaOperation.RENAME, null, tempChecked);
        } else if (tempModerate.isEmpty())
            sendAndFormService.formDirectiveToStorageForChangeStatus(null, mainMember.getUuid().toString(), null, KafkaOperation.RENAME, null, tempChecked);


//        if (directiveList.isEmpty()) {
//            if (memberService.getMaxSecretLevelForMember(mainMember, memberService.getGeneticTreeGuards(newTopAc)) != SecretLevel.OPEN) {
//                if (guardFromDirective.isPresent() && Objects.equals(mainMember.getLinkGuard(), guardFromDirective.get().getTokenUser())) {
//                    mainMember.setCheckStatus(CheckStatus.LINKED);
//                    storageDirective.add(FamilyDirective.builder()
//                            .tokenUser(mainDirective.getTokenUser())
//                            .person(mainMember.getUuid().toString())
//                            .switchPosition(SwitchPosition.FATHER)
//                            .operation(KafkaOperation.RENAME)
//                            .build());
//                    log.info("person receive link status and directive will be prepare");
//                } else {
//                    mainMember.setCheckStatus(CheckStatus.CHECKED);
//                    storageDirective.add(FamilyDirective.builder()
//                            .tokenUser(mainDirective.getTokenUser())
//                            .person(mainMember.getUuid().toString())
//                            .switchPosition(SwitchPosition.MOTHER)
//                            .operation(KafkaOperation.RENAME)
//                            .build());
//                    log.info("person receive checked status and directive will be prepare");
//                }
//            } else {
//                mainMember.setCheckStatus(CheckStatus.UNCHECKED);
//                storageDirective.add(FamilyDirective.builder()
//                        .tokenUser(mainDirective.getTokenUser())
//                        .person(mainMember.getUuid().toString())
//                        .switchPosition(SwitchPosition.CHILD)
//                        .operation(KafkaOperation.RENAME)
//                        .build());
//                log.info("person receive unchecked status and directive is send");
//            }
//        }
        mainMember.setLastUpdate(new Timestamp(System.currentTimeMillis()));
        memberService.updateMember(mainMember);
        if (!childFamilies.isEmpty()) familyRepo.saveAll(childFamilies);
        familyRepo.save(primeFamily);

        if (!directiveList.isEmpty()) {
            log.info("Acceptable changes is done. Directives will be send");
            if (directiveList.size() > 1 && directiveList.get(0).getSwitchPosition() == SwitchPosition.MAIN && (directiveList.get(1).getSwitchPosition() == SwitchPosition.FATHER || directiveList.get(1).getSwitchPosition() == SwitchPosition.MOTHER))
                directiveList.remove(0);

            directiveRepo.saveAll(directiveList);
            directiveService.formGuardDirective(directiveList);
        }
        memberRepository.flush();
//need???
        familyRepository.flush();

    }

    public boolean checkFamilyForGuard(Changing changing, Family family, Optional<Guard> directiveGuard, Set<ShortFamilyMember> potentialBloodBrothers) {
        Set<UUID> familyGuard = getAllUuidFromInfo(family.getActiveGuard());
        if (familyGuard.isEmpty()) {
            if (changing.getChangingMother() == ChangingStatus.SET || changing.getChangingFather() == ChangingStatus.SET) {
                ShortFamilyMember brother = potentialBloodBrothers.stream().findFirst().orElseThrow(() -> new RuntimeException("family exist, but number of children 0"));
                return memberService.checkThemeSecretForSecretLevel(family.getSecretLevelEdit(), brother, directiveGuard, memberService.getAllTopAncestors(brother));
            } else {
                for (ShortFamilyMember fm :
                        potentialBloodBrothers) {
                    if (memberService.checkThemeSecretForSecretLevel(family.getSecretLevelEdit(), fm, directiveGuard, memberService.getAllTopAncestors(fm)))
                        return true;
                }
                return false;
            }
        } else
            return directiveGuard.isPresent() && familyGuard.contains(directiveGuard.get().getLinkedPerson().getUuid());
    }

    @Transactional
    public CheckStatus addGuardByLink(FamilyMemberDto familyMemberDto, TokenUser tokenUser) {
        if (tokenUser.getRoles().contains(UserRoles.LINKED_USER.getNameSSO())) {
            log.warn("this user is already linked");
            throw new RuntimeException("you are already linked");
        }
        ShortFamilyMember shortFamilyMember = memberService.getMemberByUuid(familyMemberDto.getUuid()).orElseThrow(() -> new RuntimeException("Person not found"));
        switch (shortFamilyMember.getCheckStatus()) {
            case LINKED -> {
                log.warn("this person is already linked");
                throw new RuntimeException("person is already linked");
            }
            case MODERATE -> {
                log.warn("person is under voting or moderate");
                throw new RuntimeException("person is under voting or moderate. Try linking later");
            }
            case UNCHECKED -> {
                Guard guard = guardService.creatGuard(shortFamilyMember, (String) tokenUser.getClaims().get("sub"));
                memberService.addGuardToMemberByLinking(shortFamilyMember, guard);
                memberService.addGuardToMemberKin(shortFamilyMember, guard);
                Set<String> changingStatus = memberService.repairGeneticTreeCheckStatus(memberService.getAllTopAncestors(shortFamilyMember));
//                guardService.addGuardToFamilies(shortFamilyMember.getFamilies(), guard);

                memberService.updateMember(shortFamilyMember);
                memberRepository.flush();
                log.info("New guard is created");
                sendAndFormService.formDirectiveToStorageForChangeStatus((String) tokenUser.getClaims().get("sub"), shortFamilyMember.getUuid().toString(), SwitchPosition.FATHER, KafkaOperation.RENAME, changingStatus, CheckStatus.CHECKED);
                DirectiveGuards directiveGuards = DirectiveGuards.builder()
                        .created(new Timestamp(System.currentTimeMillis()))
                        .id((String) tokenUser.getClaims().get("sub"))
                        .tokenUser((String) tokenUser.getClaims().get("sub"))
                        .person(shortFamilyMember.getUuid().toString())
                        .switchPosition(SwitchPosition.MAIN)
                        .operation(KafkaOperation.ADD)
                        .info1(StringUtils.join("You are successful linked with", "<br>",
                                shortFamilyMember.getFullName(),
                                " "))
                        .build();
                directiveGuardsList.add(directiveGuards);
                contactDirective.add(DirectiveGuards.builder()
                        .operation(KafkaOperation.ADD)
                        .switchPosition(SwitchPosition.MAIN)
                        .guards(shortFamilyMember.getFamilyWhereChild().getGuard().stream().map(Guard::getTokenUser).collect(Collectors.toSet()))
                        .build());

                return CheckStatus.LINKED;
            }
            default -> {
//                shortFamilyMember.setCheckStatus(CheckStatus.MODERATE);
//                log.info("New guard is prepare to voting");
//                DeferredDirective directive = DeferredDirective
//                        .builder()
//                        .directiveFamily(shortFamilyMember.getFamilyWhereChild())
//                        .directiveMember(shortFamilyMember)
//                        .created(new Timestamp(System.currentTimeMillis()))
//                        .info(tokenUser.getUsername())
//                        .tokenUser((String) tokenUser.getClaims().get("sub"))
//                        .switchPosition(SwitchPosition.MAIN)
//
//                        .build();
//                directiveRepo.save(directive);
//                Set<String> guards;
//                if (shortFamilyMember.getFamilyWhereChild().getGuard() != null
//                        && !shortFamilyMember.getFamilyWhereChild().getGuard().isEmpty()) {
//                    guards = shortFamilyMember.getFamilyWhereChild().getGuard().stream()
//                            .map(Guard::getTokenUser)
//                            .collect(Collectors.toSet());
//                } else if (shortFamilyMember.getFamilyWhereChild().getGlobalFamily().getGuard() != null && !shortFamilyMember.getFamilyWhereChild().getGlobalFamily().getGuard().isEmpty()) {
//                    guards = shortFamilyMember.getFamilyWhereChild().getGlobalFamily().getGuard().stream()
//                            .map(Guard::getTokenUser)
//                            .collect(Collectors.toSet());
//                } else throw new RuntimeException("check status off person is wrong");
//                DirectiveGuards directiveGuards = DirectiveGuards.builder()
//                        .id(directive.getId().toString())
//                        .created(directive.getCreated())
//                        .person(shortFamilyMember.getUuid().toString())
//                        .guards(guards)
//                        .tokenUser(directive.getTokenUser())
//                        .switchPosition(directive.getSwitchPosition())
//                        .info1(StringUtils.join("User: ", "<br>",
//                                tokenUser.getUsername(), "<br>",
//                                " with NickName: ",
//                                tokenUser.getNickName(),
//                                " with Email: ",
//                                tokenUser.getEmail(), "<br>",
//                                "want to be linking with ", "<br>",
//                                directive.getDirectiveMember().getFullName(),
//                                " "))
//                        .build();
//                directiveGuards.setGlobalNumber1(directive.getGlobalFor());
//                directiveGuards.setGlobalNumber2(directive.getGlobalTo());
//                directiveGuardsList.add(directiveGuards);
//
//                storageDirective.add(FamilyDirective.builder()
//                        .person(shortFamilyMember.getUuid().toString())
//                        .switchPosition(SwitchPosition.MAIN)
//                        .operation(KafkaOperation.RENAME)
//                        .build());
                return CheckStatus.MODERATE;
            }
        }

    }

    @Transactional
    public SecretLevel getGuardStatus(UUID uuid, String tokenUser) {
        ShortFamilyMember member = memberRepository.findMemberWithPrimeFamily(uuid).orElseThrow(() -> new RuntimeException("person not found"));
        Optional<Guard> guardFromToken = guardService.findGuard(tokenUser);
        if (member == null || guardFromToken.isEmpty()) throw new RuntimeException("Status check is falled");
        Set<ShortFamilyMember> topAncestors = memberService.getAllTopAncestors(member);
        Set<UUID> geneticTreeGuard = memberService.getGeneticTreeGuards(topAncestors);
        SecretLevel tempStatus = memberService.getSecretStatus(member, UUID.fromString(guardFromToken.get().getTokenUser()), geneticTreeGuard);
        SecretLevel max = memberService.getMaxSecretLevelForMember(member, geneticTreeGuard);

        if (tempStatus == max) return SecretLevel.CONFIDENTIAL;
        else return tempStatus;
    }

    private ChangingStatus getChangingStatus(String newInfo, String oldInfo) {
        char firstOld, secondOld, firstNew, secondNew;
        if (oldInfo != null) {
            firstOld = oldInfo.charAt(0);
            secondOld = oldInfo.charAt(1);
        } else {
            firstOld = '(';
            secondOld = ')';
        }
        if (newInfo != null) {
            firstNew = newInfo.charAt(0);
            secondNew = newInfo.charAt(1);
        } else {
            firstNew = '(';
            secondNew = ')';
        }
        if (oldInfo == null && newInfo == null) return ChangingStatus.NONE;
        if ((firstOld == '(' && secondOld != 'A') && firstNew == '(' && secondNew != 'A')
            return ChangingStatus.NOT_IMPORTANT;
        if (Objects.equals(oldInfo, newInfo)) return ChangingStatus.ABSENT;
        if (oldInfo == null && secondNew == 'A') return ChangingStatus.FREE;
        if (oldInfo == null) return ChangingStatus.SET;
        if (firstOld != '(' && newInfo == null) return ChangingStatus.REMOVE;
        if (firstOld != '(' && secondNew == 'A') return ChangingStatus.MINOR_CHANGE;
        if (secondOld == 'A' && newInfo == null) return ChangingStatus.LIGHT_FREE;
        if (secondOld == 'A' && secondNew == 'A') return ChangingStatus.HARD_FREE;
        if (secondOld == 'A' && firstNew != '(') return ChangingStatus.CHANGE;
        return ChangingStatus.MAJOR_CHANGE;
    }
}