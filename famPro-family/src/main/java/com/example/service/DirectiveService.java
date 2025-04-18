package com.example.service;

import com.example.dtos.Directive;
import com.example.dtos.DirectiveGuards;
import com.example.dtos.FamilyDirective;
import com.example.entity.DeferredDirective;
import com.example.entity.Family;
import com.example.entity.Guard;
import com.example.entity.ShortFamilyMember;
import com.example.enums.*;
import com.example.repository.DirectiveRepo;
import com.example.repository.FamilyRepo;
import com.example.repository.MainFamilyRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
public class DirectiveService {
    private final GuardService guardService;
    private final List<DirectiveGuards> directiveGuardsList;
    private final FamilyRepo familyRepo;
    private final MainFamilyRepo mainFamilyRepo;
    private final FamilyServiceImp familyService;
    private final MemberService memberService;
    private final DirectiveRepo directiveRepo;
    private final GlobalFamilyService globalFamilyService;
    private final List<FamilyDirective> storageDirective;
    private final List<DirectiveGuards> contactDirective;
    private final List<Directive> cloakDirective;

    public DirectiveService(GuardService guardService,
                            @Qualifier("directiveGuards") List<DirectiveGuards> directiveGuardsList,
                            FamilyRepo familyRepo,
                            MainFamilyRepo mainFamilyRepo,
                            FamilyServiceImp familyService,
                            MemberService memberService,
                            DirectiveRepo directiveRepo,
                            GlobalFamilyService globalFamilyService,
                            List<FamilyDirective> storageDirective,
                            @Qualifier("contactDirective") List<DirectiveGuards> contactDirective, List<Directive> cloakDirective) {
        this.guardService = guardService;
        this.directiveGuardsList = directiveGuardsList;
        this.familyRepo = familyRepo;
        this.mainFamilyRepo = mainFamilyRepo;
        this.familyService = familyService;
        this.memberService = memberService;
        this.directiveRepo = directiveRepo;
        this.globalFamilyService = globalFamilyService;
        this.storageDirective = storageDirective;
        this.contactDirective = contactDirective;
        this.cloakDirective = cloakDirective;
    }

    public void formGuardDirective(List<DeferredDirective> directiveList) {

        for (DeferredDirective directive : directiveList) {
            Set<String> guards = guardService.findFamilyGuards(directive.getProcessFamily())
                    .stream()
                    .map(Guard::getTokenUser)
                    .collect(Collectors.toSet());
            DirectiveGuards directiveGuards = DirectiveGuards.builder()
                    .id(directive.getId().toString())
                    .created(directive.getCreated())
                    .guards(guards)
                    .tokenUser(directive.getTokenUser())
                    .switchPosition(directive.getSwitchPosition())
                    .info1(directive.getDirectiveMember().getFullName())
                    .info2(directive.getInfo())
                    .build();
            directiveGuards.setGlobalNumber1(directive.getGlobalTo());
            directiveGuards.setGlobalNumber2(directive.getGlobalFor());
            directiveGuardsList.add(directiveGuards);
        }

    }

    @Transactional
    public void setChangesFromVotingDirective(String directiveUuid) {
        Set<Family> grandChildFamilies = new HashSet<>();
        Family familyToRemove = null;
        DeferredDirective deferredDirective = mainFamilyRepo.findDirectiveWithAllLinks(UUID.fromString(directiveUuid));
        if (deferredDirective == null) throw new RuntimeException("Deferred directive is missing or corrupt");

        ShortFamilyMember mainMember = deferredDirective.getDirectiveMember();
        ShortFamilyMember processMember = deferredDirective.getShortFamilyMemberLink();
        Family mainFamily;
        Family processFamily;
        if (deferredDirective.getProcessFamily() == null)
            processFamily = mainFamilyRepo.findFamilyWithGlobal(processMember);
        else processFamily = deferredDirective.getProcessFamily();
        if (deferredDirective.getDirectiveFamily() == null)
            mainFamily = mainFamilyRepo.findFamilyWithGlobal(mainMember);
        else mainFamily = deferredDirective.getDirectiveFamily();
        SwitchPosition switchPosition = deferredDirective.getSwitchPosition();

        directiveRepo.delete(deferredDirective);

        DeferredDirective existByMain = directiveRepo.findFirstByDirectiveMember(mainMember);
        DeferredDirective existByProcess = directiveRepo.findFirstByDirectiveMember(processMember);
        if (existByMain == null) {
            if (mainMember.getLinkedGuard() != null && !mainMember.getLinkedGuard().isEmpty())
                mainMember.setCheckStatus(CheckStatus.LINKED);
            else mainMember.setCheckStatus(CheckStatus.CHECKED);
        }
        if (existByProcess == null) {
            if (processMember.getLinkedGuard() != null && !processMember.getLinkedGuard().isEmpty())
                processMember.setCheckStatus(CheckStatus.LINKED);
            else processMember.setCheckStatus(CheckStatus.CHECKED);
        }

        switch (switchPosition) {
            case MAIN -> {
                familyService.mergeFamilies(mainFamily, processFamily);
                mainMember.setFamilyWhereChild(processFamily);
                familyToRemove = mainFamily;
                familyRepo.save(processFamily);
                log.info("merge family is done");
            }
            case FATHER -> {
                familyService.addChangesFromFather(mainFamily,
                        processFamily,
                        mainMember,
                        processMember);
                grandChildFamilies.addAll(familyService.addGrandLinks(mainMember.getChilds(), processMember));
                familyToRemove = checkForUnique(processMember, mainMember, mainFamily);
                log.info("father is setup");
            }
            case MOTHER -> {
                familyService.addChangesFromMother(mainFamily,
                        processFamily,
                        mainMember,
                        processMember);
                grandChildFamilies.addAll(familyService.addGrandLinks(mainMember.getChilds(), processMember));
                familyToRemove = checkForUnique(processMember, mainMember, mainFamily);
                log.info("mother is setup");
            }
            case CHILD -> {
                if (mainMember.getSex() == Sex.MALE)
                    familyService.addChangesFromFather(
                            processFamily,
                            mainFamily,
                            processMember,
                            mainMember);
                else
                    familyService.addChangesFromMother(
                            processFamily,
                            mainFamily,
                            processMember,
                            mainMember);
                grandChildFamilies.addAll(familyService.addGrandLinks(processMember.getChilds(), mainMember));
                familyToRemove = checkForUnique(mainMember, processMember, processFamily);
                memberService.addChildToFamilyMember(mainMember, processMember);
                memberService.getShortMemberRepo().save(processMember);
                log.info("child is setup");
            }
            default -> log.warn("Обнаружена неизвестная директива");
        }
        if (!grandChildFamilies.isEmpty()) familyRepo.saveAll(grandChildFamilies);
        memberService.getShortMemberRepo().save(mainMember);
        memberService.getShortMemberRepo().save(processMember);

        if (familyToRemove != null) {
            Set<DeferredDirective> deferredDirectives = mainFamilyRepo.findDirectivesConsistFamilyToRemove(familyToRemove);
            if (!deferredDirectives.isEmpty()) {
                for (DeferredDirective dd :
                        deferredDirectives) {
                    if (dd.getDirectiveFamily() == familyToRemove) dd.setDirectiveFamily(null);
                    else dd.setProcessFamily(null);
                }
                directiveRepo.saveAll(deferredDirectives);
            }
            familyRepo.delete(familyToRemove);
        }

        if (mainFamily.getGlobalFamily().getNumber() > processFamily.getGlobalFamily().getNumber())
            globalFamilyService.mergeGlobalFamilies(mainFamily.getGlobalFamily(), processFamily.getGlobalFamily());
        else
            globalFamilyService.mergeGlobalFamilies(processFamily.getGlobalFamily(), mainFamily.getGlobalFamily());

        if (existByMain == null) storageDirective.add(FamilyDirective.builder()
                .person(mainMember.getUuid().toString())
                .switchPosition((mainMember.getCheckStatus() == CheckStatus.LINKED) ? SwitchPosition.FATHER : SwitchPosition.MOTHER)
                .operation(KafkaOperation.RENAME)
                .build());
        if (existByProcess == null) storageDirective.add(FamilyDirective.builder()
                .person(processMember.getUuid().toString())
                .switchPosition((processMember.getCheckStatus() == CheckStatus.LINKED) ? SwitchPosition.FATHER : SwitchPosition.MOTHER)
                .operation(KafkaOperation.RENAME)
                .build());
        contactDirective.add(DirectiveGuards.builder()
                .operation(KafkaOperation.ADD)
                .switchPosition(SwitchPosition.MAIN)
                .guards(mainFamily.getGlobalFamily().getGuard().stream().map(Guard::getTokenUser).collect(Collectors.toSet()))
                .build());
    }

    private Family checkForUnique(ShortFamilyMember parent, ShortFamilyMember child, Family childFamily) {
        boolean unique = true;
        Set<Family> childFamilies = parent.getChilds().stream().map(ShortFamilyMember::getFamilyWhereChild).collect(Collectors.toSet());
        for (Family fam : childFamilies) {
            if (unique && fam.getExternID().equals(childFamily.getExternID()) && fam != childFamily) {
                familyService.mergeFamilies(childFamily, fam);
                child.setFamilyWhereChild(fam);
                familyRepo.save(fam);
                unique = false;
            }
        }
        if (unique) {
            familyRepo.save(childFamily);
            return null;
        } else
            return childFamily;
    }

    @Transactional
    public void negativeVoting(String directiveUuid) {
        DeferredDirective deferredDirective = mainFamilyRepo.findDirectiveWithAllLinks(UUID.fromString(directiveUuid));
        if (deferredDirective == null) throw new RuntimeException("Deferred directive is missing or corrupt");
        if (deferredDirective.getProcessFamily() == null)
            deferredDirective.setProcessFamily(mainFamilyRepo.findFamilyWithGlobal(deferredDirective.getShortFamilyMemberLink()));
        if (deferredDirective.getDirectiveFamily() == null)
            deferredDirective.setDirectiveFamily(mainFamilyRepo.findFamilyWithGlobal(deferredDirective.getDirectiveMember()));
        switch (deferredDirective.getSwitchPosition()) {
            case MAIN -> {
                deferredDirective.getDirectiveMember().setMotherInfo(null);
                deferredDirective.getDirectiveMember().setFatherInfo(null);
                deferredDirective.getDirectiveFamily().setWifeInfo(null);
                deferredDirective.getDirectiveFamily().setHusbandInfo(null);
                deferredDirective.getDirectiveFamily().setExternID(deferredDirective.getDirectiveMember().getUuid().toString());
                storageDirective.add(FamilyDirective.builder()
                        .person(deferredDirective.getDirectiveMember().getUuid().toString())
                        .switchPosition(SwitchPosition.MAIN)
                        .operation(KafkaOperation.EDIT)
                        .build());
            }
            case MOTHER -> {
                deferredDirective.getDirectiveMember().setMotherInfo(null);
                if (deferredDirective.getDirectiveFamily().getChildren().size() == 1) {
                    deferredDirective.getDirectiveFamily().setWifeInfo(null);
                    deferredDirective.getDirectiveFamily().setExternID(deferredDirective.getDirectiveMember().getUuid().toString());
                } else
                    familyService.ejectionPersonFromFamily(deferredDirective.getDirectiveMember(), deferredDirective.getDirectiveFamily());
                storageDirective.add(FamilyDirective.builder()
                        .person(deferredDirective.getDirectiveMember().getUuid().toString())
                        .switchPosition(SwitchPosition.MOTHER)
                        .operation(KafkaOperation.EDIT)
                        .build());
            }
            case FATHER -> {
                deferredDirective.getDirectiveMember().setFatherInfo(null);
                if (deferredDirective.getDirectiveFamily().getChildren().size() == 1) {
                    deferredDirective.getDirectiveFamily().setHusbandInfo(null);
                    deferredDirective.getDirectiveFamily().setExternID(deferredDirective.getDirectiveMember().getUuid().toString());
                } else
                    familyService.ejectionPersonFromFamily(deferredDirective.getDirectiveMember(), deferredDirective.getDirectiveFamily());
                storageDirective.add(FamilyDirective.builder()
                        .person(deferredDirective.getDirectiveMember().getUuid().toString())
                        .switchPosition(SwitchPosition.FATHER)
                        .operation(KafkaOperation.EDIT)
                        .build());
            }
            case CHILD -> {
                if (deferredDirective.getDirectiveMember().getSex() == Sex.MALE) {
                    deferredDirective.getShortFamilyMemberLink().setFatherInfo(null);
                    if (deferredDirective.getProcessFamily().getChildren().size() == 1) {
                        deferredDirective.getProcessFamily().setHusbandInfo(null);
                        deferredDirective.getProcessFamily().setExternID(deferredDirective.getShortFamilyMemberLink().getUuid().toString());
                    } else
                        familyService.ejectionPersonFromFamily(deferredDirective.getShortFamilyMemberLink(), deferredDirective.getProcessFamily());
                    storageDirective.add(FamilyDirective.builder()
                            .person(deferredDirective.getShortFamilyMemberLink().getUuid().toString())
                            .switchPosition(SwitchPosition.FATHER)
                            .operation(KafkaOperation.EDIT)
                            .build());
                } else {
                    deferredDirective.getShortFamilyMemberLink().setMotherInfo(null);
                    if (deferredDirective.getProcessFamily().getChildren().size() == 1) {
                        deferredDirective.getProcessFamily().setWifeInfo(null);
                        deferredDirective.getProcessFamily().setExternID(deferredDirective.getShortFamilyMemberLink().getUuid().toString());
                    } else
                        familyService.ejectionPersonFromFamily(deferredDirective.getShortFamilyMemberLink(), deferredDirective.getProcessFamily());
                    storageDirective.add(FamilyDirective.builder()
                            .person(deferredDirective.getShortFamilyMemberLink().getUuid().toString())
                            .switchPosition(SwitchPosition.MOTHER)
                            .operation(KafkaOperation.EDIT)
                            .build());
                }
            }
            default -> log.warn("Обнаружена неизвестная директива");
        }
        directiveRepo.delete(deferredDirective);
    }

    @Transactional
    public void setLinkGuardFromVotingDirective(String directiveUuid) {
        DeferredDirective directive = mainFamilyRepo.getLinkingDirective(UUID.fromString(directiveUuid)).orElseThrow(() -> new RuntimeException("directive is missing"));
        if (directive.getDirectiveMember() == null || !directive.getDirectiveMember().getLinkedGuard().isEmpty())
            throw new RuntimeException("directive is corrupt");
        ShortFamilyMember shortFamilyMember = mainFamilyRepo.getPersonForLinking(directive.getDirectiveMember().getUuid());
        if (shortFamilyMember == null) throw new RuntimeException("человек не найден");
        Guard linkGuard = guardService.creatGuard(shortFamilyMember, directive.getTokenUser());
        guardService.addGuardToFamilies(shortFamilyMember.getFamilies(), linkGuard);
        guardService.addGuardToGlobalFamily(linkGuard, shortFamilyMember.getFamilyWhereChild().getGlobalFamily());
        globalFamilyService.getGlobalFamilyRepo().save(shortFamilyMember.getFamilyWhereChild().getGlobalFamily());
        memberService.getShortMemberRepo().save(shortFamilyMember);
//        familyRepo.saveAll(shortFamilyMember.getFamilies());

        log.info("New guard is created");
        cloakDirective.add(Directive.builder()
                .operation(KafkaOperation.EDIT)
                .tokenUser(directive.getInfo())
                .person(directive.getTokenUser())
                .build());
        storageDirective.add(FamilyDirective.builder()
                .tokenUser(directive.getTokenUser())
                .person(shortFamilyMember.getUuid().toString())
                .switchPosition(SwitchPosition.FATHER)
                .operation(KafkaOperation.RENAME)
                .build());
        contactDirective.add(DirectiveGuards.builder()
                .operation(KafkaOperation.ADD)
                .switchPosition(SwitchPosition.MAIN)
                .guards(shortFamilyMember.getFamilyWhereChild().getGlobalFamily().getGuard().stream().map(Guard::getTokenUser).collect(Collectors.toSet()))
                .build());
        directiveRepo.delete(directive);

    }

    @Transactional
    public void rejectLinkGuard(String directiveUuid) {
        DeferredDirective directive = directiveRepo.findById(UUID.fromString(directiveUuid)).orElseThrow(() -> new RuntimeException("directive is missing"));
        ShortFamilyMember shortFamilyMember = memberService.getShortMemberRepo().findByUuid(directive.getDirectiveMember().getUuid()).orElseThrow(() -> new RuntimeException("не найден человек"));
        shortFamilyMember.setCheckStatus(CheckStatus.CHECKED);
        memberService.getShortMemberRepo().save(shortFamilyMember);
        storageDirective.add(FamilyDirective.builder()
                .tokenUser(directive.getTokenUser())
                .person(shortFamilyMember.getUuid().toString())
                .switchPosition(SwitchPosition.MOTHER)
                .operation(KafkaOperation.RENAME)
                .build());
        directiveRepo.delete(directive);
    }
}
