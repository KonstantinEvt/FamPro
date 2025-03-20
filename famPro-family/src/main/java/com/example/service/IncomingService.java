package com.example.service;


import com.example.dtos.DirectiveGuards;
import com.example.dtos.FamilyDirective;
import com.example.dtos.FamilyMemberDto;
import com.example.dtos.TokenUser;
import com.example.entity.*;
import com.example.enums.*;
import com.example.repository.DirectiveRepo;
import com.example.repository.FamilyRepo;
import com.example.repository.MainFamilyRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class IncomingService {
    private final FamilyRepo familyRepo;
    private final MemberService memberService;
    private final GlobalFamilyService globalFamilyService;
    private final DirectiveRepo directiveRepo;
    private final GuardService guardService;
    private final FamilyServiceImp familyService;
    private final DirectiveService directiveService;
    private List<DirectiveGuards> directiveGuardsList;
    private List<FamilyDirective> storageDirective;
    private final MainFamilyRepo mainFamilyRepo;

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
        ShortFamilyMember anyBrother = null;

        if (mainDirective.getOperation() == KafkaOperation.RENAME) {
            mainMember = memberService.getShortMemberRepo().findByUuid(UUID.fromString(mainDirective.getPerson())).orElseThrow(() -> new RuntimeException("не найден"));
            if (mainMember.getCheckStatus() == CheckStatus.MODERATE && !mainDirective.getTokenUser().equals("moderator")) {
                log.warn("Попытка изменения человека находящимся под голосованием или модерацией");
                directiveGuardsList.add(DirectiveGuards.builder()
                        .created(new Timestamp(System.currentTimeMillis()))
                        .tokenUser(mainDirective.getTokenUser())
                        .switchPosition(mainDirective.getSwitchPosition())
                        .info1("trying changing person under voting")
                        .info2(mainMember.getFullName())
                        .build());
                return;
            }
            primeFamily = mainMember.getFamilyWhereChild();
            if (checkFamilyForGuard(primeFamily, guardFromDirective) || mainDto.getCheckStatus() == CheckStatus.MODERATE || mainDto.getCheckStatus() == CheckStatus.CHECKED) {
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
                        if (checkFamilyForGuard(possiblePrime.get(), guardFromDirective) || mainDto.getCheckStatus() == CheckStatus.MODERATE) {
                            System.out.println("Сейчас будем мержить семьи");
                            possiblePrime.get().getChildren().add(mainMember);
                            familyService.mergeFamilies(primeFamily, possiblePrime.get());
                            familyRepo.delete(primeFamily);
                            System.out.println("444");
                            primeFamily = possiblePrime.get();
                        } else {
                            anyBrother = possiblePrime.get().getChildren().stream().findFirst().orElseThrow(() -> new RuntimeException("Brother not found"));
                            directiveList.add(DeferredDirective
                                    .builder()
                                    .created(new Timestamp(System.currentTimeMillis()))
                                    .directiveFamily(primeFamily)
                                    .directiveMember(mainMember)
                                    .shortFamilyMemberLink(anyBrother)
                                    .info(possibleId)
                                    .processFamily(possiblePrime.get())
                                    .tokenUser(mainDirective.getTokenUser())
                                    .switchPosition(SwitchPosition.MAIN)
                                    .globalFor(primeFamily.getGlobalFamily().getNumber())
                                    .globalTo(possiblePrime.get().getGlobalFamily().getNumber())
                                    .build());
                        }// запрос на слияние с кровными братьями
                    } else {
                        primeFamily.setExternID(possibleId);
                        System.out.println("Но мы ее не нашли");
                    }
                }
                familyService.addChangesToFamilyInfo(primeFamily, mainDto.getFatherInfo(), mainMember.getMotherInfo());
            } else {
                log.warn("Попытка изменения человека под защитой");
                directiveGuardsList.add(DirectiveGuards.builder()
                        .created(new Timestamp(System.currentTimeMillis()))
                        .tokenUser(mainDirective.getTokenUser())
                        .switchPosition(mainDirective.getSwitchPosition())
                        .info1("trying changing person without rights")
                        .info2(mainMember.getFullName())
                        .build());
                return;
            }
        }
        if (mainDirective.getOperation() == KafkaOperation.ADD) {
            mainMember = memberService.addFamilyMember(mainDto);
            primeFamily = familyService.creatOrFindFamilyByInfo(mainMember);
            System.out.println("прайм семья определена");

            System.out.println("OOOOOOOOOOOOOOOOJJJJJJJJ1");
            if (!primeFamily.getChildren().isEmpty() && !checkFamilyForGuard(primeFamily, guardFromDirective)) {
                System.out.println("OOOOOOOOOOOOOOOOJJJJJJJJ3");
                possiblePrime = Optional.of(primeFamily);
                primeFamily = familyService.creatFreeFamily(mainDto.getFatherInfo(), mainDto.getMotherInfo(), mainDto.getUuid().toString());
                anyBrother = possiblePrime.get().getChildren().stream().findFirst().orElseThrow(() -> new RuntimeException("Brother not found"));
                directiveList.add(DeferredDirective
                        .builder()
                        .directiveFamily(primeFamily)
                        .directiveMember(mainMember)
                        .shortFamilyMemberLink(anyBrother)
                        .created(new Timestamp(System.currentTimeMillis()))
                        .info(possiblePrime.get().getExternID())
                        .processFamily(possiblePrime.get())
                        .tokenUser(mainDirective.getTokenUser())
                        .switchPosition(SwitchPosition.MAIN)
                        .globalFor(1)
                        .globalTo(possiblePrime.get().getGlobalFamily().getNumber())
                        .build());
            }
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


        while (!directiveLinkedList.isEmpty()) {
            System.out.println("обработка едит директив");
            FamilyDirective processDirective = directiveLinkedList.pollFirst();
            assert processDirective != null;
            System.out.println(processDirective);
//            ShortFamilyMember member = memberService.getShortMemberRepo().findByUuid(processDirective.getFamilyMemberDto().getUuid())
//                    .orElseThrow(() -> new RuntimeException("Anomaly"));
            ShortFamilyMember member = mainFamilyRepo.findMemberWithFamilyWithAllGuardsByUuid(processDirective.getFamilyMemberDto().getUuid());
            if (member == null) {
                log.warn("This link is {}", processDirective.getSwitchPosition().getInfo());
                continue;
            }
            Family family = member.getFamilyWhereChild();
            boolean existBloodBrother = !childFamilies.isEmpty() && childFamilies.contains(family);
            if (checkFamilyForGuard(family, guardFromDirective)) {
                if (!existBloodBrother) {
                    log.info("merging global families");
                    if (family.getGlobalFamily().getNumber() > primeFamily.getGlobalFamily().getNumber())
                        primeFamily.setGlobalFamily(globalFamilyService.mergeGlobalFamilies(family.getGlobalFamily(), primeFamily.getGlobalFamily()));
                    else
                        family.setGlobalFamily(globalFamilyService.mergeGlobalFamilies(primeFamily.getGlobalFamily(), family.getGlobalFamily()));
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
                            guardService.addGuardParentsFamilyToFamily(primeFamily, family);

                            if (family.getHusbandInfo() != null && family.getWifeInfo() != null)
                                family.setExternID(family.getHusbandInfo().concat(family.getWifeInfo()));
                            familyService.addPersonParentsToFamilyParents(family, mainMember);
                            for (Family fam :
                                    childFamilies) {
                                if (fam.getExternID().equals(family.getExternID()) && fam != family) {
                                    familyService.mergeFamilies(family, fam);
                                    familiesToRemove.add(family);
                                }
                            }
                            familyRepo.save(family);
                        }
                        grandChildFamilies.addAll(familyService.addGrandLinks(member.getChilds(), mainMember));
                        memberService.addChildToFamilyMember(mainMember, member);
                        memberService.getShortMemberRepo().save(member);
                        log.info("child is setup");

                    }
                    case FATHER -> {
                        familyService.addChangesFromFather(primeFamily, family, mainMember, member);
                        grandChildFamilies.addAll(familyService.addGrandLinks(mainMember.getChilds(), member));
                        log.info("father is setup");
                    }
                    case MOTHER -> {
                        familyService.addChangesFromMother(primeFamily, family, mainMember, member);
                        grandChildFamilies.addAll(familyService.addGrandLinks(mainMember.getChilds(), member));
                    }
                    default -> log.warn("Обнаружена нераспознанная директива");
                }
            } else {
                if (member.getCheckStatus() != CheckStatus.MODERATE) {
                    member.setCheckStatus(CheckStatus.MODERATE);
                    memberService.getShortMemberRepo().save(member);
                    storageDirective.add(FamilyDirective.builder()
                            .person(member.getUuid().toString())
                            .switchPosition(SwitchPosition.MAIN)
                            .operation(KafkaOperation.RENAME)
                            .build());
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
                        .globalFor(primeFamily.getGlobalFamily().getNumber())
                        .globalTo(family.getGlobalFamily().getNumber())
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
        System.out.println("Отправка статусных директив");
        if (!directiveList.isEmpty()) {
            mainMember.setCheckStatus(CheckStatus.MODERATE);
            storageDirective.add(FamilyDirective.builder()
                    .person(mainMember.getUuid().toString())
                    .switchPosition(SwitchPosition.MAIN)
                    .operation(KafkaOperation.RENAME)
                    .build());
            log.info("moderate directive is send");
        } else if ((primeFamily.getGuard() != null && !primeFamily.getGuard().isEmpty())
                || (primeFamily.getGlobalFamily().getGuard() != null && !primeFamily.getGlobalFamily().getGuard().isEmpty())) {
            if (mainMember.getCheckStatus() != CheckStatus.LINKED && guardFromDirective.isPresent() && Objects.equals(mainMember.getLinkedGuard(), guardFromDirective.get())) {
                mainMember.setCheckStatus(CheckStatus.LINKED);
                storageDirective.add(FamilyDirective.builder()
                        .tokenUser(mainDirective.getTokenUser())
                        .person(mainMember.getUuid().toString())
                        .switchPosition(SwitchPosition.FATHER)
                        .operation(KafkaOperation.RENAME)
                        .build());
                log.info("person receive link status and directive is send");
            } else if (mainMember.getCheckStatus() != CheckStatus.CHECKED) {
                mainMember.setCheckStatus(CheckStatus.CHECKED);
                storageDirective.add(FamilyDirective.builder()
                        .tokenUser(mainDirective.getTokenUser())
                        .person(mainMember.getUuid().toString())
                        .switchPosition(SwitchPosition.MOTHER)
                        .operation(KafkaOperation.RENAME)
                        .build());
                log.info("person receive checked status and directive is send");
            }
        }
        memberService.getShortMemberRepo().save(mainMember);
        if (!childFamilies.isEmpty()) familyRepo.saveAll(childFamilies);
        if (!grandChildFamilies.isEmpty()) familyRepo.saveAll(grandChildFamilies);
        familyRepo.save(primeFamily);
        log.info("Acceptable changes is done. Directives will be send");
        if (directiveList.size() > 1 && directiveList.get(0).getSwitchPosition() == SwitchPosition.MAIN && (directiveList.get(1).getSwitchPosition() == SwitchPosition.FATHER || directiveList.get(1).getSwitchPosition() == SwitchPosition.MOTHER))
            directiveList.remove(0);
        else {
            if (anyBrother != null && anyBrother.getCheckStatus() != CheckStatus.MODERATE) {
                anyBrother.setCheckStatus(CheckStatus.MODERATE);
                memberService.getShortMemberRepo().save(anyBrother);
                storageDirective.add(FamilyDirective.builder()
                        .person(anyBrother.getUuid().toString())
                        .switchPosition(SwitchPosition.MAIN)
                        .operation(KafkaOperation.RENAME)
                        .build());
            }
        }
        directiveRepo.saveAll(directiveList);
        directiveService.formGuardDirective(directiveList);
    }

    public boolean checkFamilyForGuard(Family family, Optional<Guard> directiveGuard) {
        System.out.println("OOOOOOOOOOOOOOOOJJJJJJJJ2");
        return ((family.getGuard() == null || family.getGuard().isEmpty()) &&
                (family.getGlobalFamily() == null || family.getGlobalFamily().getGuard() == null || family.getGlobalFamily().getGuard().isEmpty()))
                || (directiveGuard.isPresent() &&
                ((family.getGuard() != null && !family.getGuard().isEmpty() && family.getGuard().contains(directiveGuard.get()))
                        || (family.getGlobalFamily().getGuard() != null && !family.getGlobalFamily().getGuard().isEmpty() && family.getGlobalFamily().getGuard().contains(directiveGuard.get())
                )));
    }

    @Transactional
    public void addGuardByLink(FamilyMemberDto familyMemberDto, TokenUser tokenUser) {
        if (tokenUser.getRoles().contains(UserRoles.LINKED_USER.getNameSSO())) {
            log.warn("this user is already linked");
            throw new RuntimeException("you are already linked");
        }
        ShortFamilyMember shortFamilyMember = mainFamilyRepo.getPersonForLinking(familyMemberDto.getUuid());
        if (shortFamilyMember == null) throw new RuntimeException("человек не найден");
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
                shortFamilyMember.setCheckStatus(CheckStatus.LINKED);
                guardService.addGuardToFamilies(shortFamilyMember.getFamilies(), guard);
                guardService.addGuardToGlobalFamily(guard, shortFamilyMember.getFamilyWhereChild().getGlobalFamily());
                familyRepo.saveAll(shortFamilyMember.getFamilies());
//                memberService.shortMemberRepo.save(shortFamilyMember);
                log.info("New guard is created");

                storageDirective.add(FamilyDirective.builder()
                        .tokenUser((String) tokenUser.getClaims().get("sub"))
                        .person(shortFamilyMember.getUuid().toString())
                        .switchPosition(SwitchPosition.FATHER)
                        .operation(KafkaOperation.RENAME)
                        .build());
            }
            default -> {
                shortFamilyMember.setCheckStatus(CheckStatus.MODERATE);
                log.info("New guard is prepare to voting");
                DeferredDirective directive = DeferredDirective
                        .builder()
                        .directiveFamily(shortFamilyMember.getFamilyWhereChild())
                        .directiveMember(shortFamilyMember)
                        .created(new Timestamp(System.currentTimeMillis()))
                        .info("linking directive")
                        .tokenUser((String) tokenUser.getClaims().get("sub"))
                        .switchPosition(SwitchPosition.MAIN)
                        .globalFor(shortFamilyMember.getFamilyWhereChild().getGlobalFamily().getNumber())
                        .build();
                directiveRepo.save(directive);
                Set<String> guards;
                if (shortFamilyMember.getFamilyWhereChild().getGuard() != null
                        && !shortFamilyMember.getFamilyWhereChild().getGuard().isEmpty()) {
                    guards = shortFamilyMember.getFamilyWhereChild().getGuard().stream()
                            .map(Guard::getTokenUser)
                            .collect(Collectors.toSet());
                } else if (shortFamilyMember.getFamilyWhereChild().getGlobalFamily().getGuard() != null && !shortFamilyMember.getFamilyWhereChild().getGlobalFamily().getGuard().isEmpty()) {
                    guards = shortFamilyMember.getFamilyWhereChild().getGlobalFamily().getGuard().stream()
                            .map(Guard::getTokenUser)
                            .collect(Collectors.toSet());
                } else throw new RuntimeException("check status off person is wrong");
                DirectiveGuards directiveGuards = DirectiveGuards.builder()
                        .id(directive.getId().toString())
                        .created(directive.getCreated())
                        .guards(guards)
                        .tokenUser(directive.getTokenUser())
                        .switchPosition(directive.getSwitchPosition())
                        .info1(StringUtils.join("User: ", "<br>",
                                tokenUser.getUsername(), "<br>",
                                " with NickName: ",
                                tokenUser.getNickName(),
                                " with Email: ",
                                tokenUser.getEmail(), "<br>",
                                "want to be linking with ", "<br>",
                                directive.getDirectiveMember().getFullName(),
                                " "))
                        .build();
                directiveGuards.setGlobalNumber1(directive.getGlobalTo());
                directiveGuardsList.add(directiveGuards);

                storageDirective.add(FamilyDirective.builder()
                        .person(shortFamilyMember.getUuid().toString())
                        .switchPosition(SwitchPosition.MAIN)
                        .operation(KafkaOperation.RENAME)
                        .build());
            }
        }

    }

    @Transactional
    public boolean checkStatusCheck(UUID uuid, String tokenUser) {
        Optional<ShortFamilyMember> mainMember = memberService.getShortMemberRepo().findByUuid(uuid);
        Optional<Guard> guardFromToken = guardService.findGuard(tokenUser);
        if (mainMember.isEmpty() || guardFromToken.isEmpty()) return false;
        boolean rights = checkFamilyForGuard(mainFamilyRepo.findFamilyWithAllGuards(mainMember.get()), guardFromToken);
        if (!rights) directiveGuardsList.add(DirectiveGuards.builder()
                .created(new Timestamp(System.currentTimeMillis()))
                .tokenUser(tokenUser)
                .switchPosition(SwitchPosition.MAIN)
                .info1("trying changing person without rights")
                .info2(mainMember.get().getFullName())
                .build());
        return rights;
    }
}

