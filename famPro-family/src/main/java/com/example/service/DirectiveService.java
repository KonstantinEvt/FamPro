package com.example.service;

import com.example.dtos.Directive;
import com.example.dtos.DirectiveGuards;
import com.example.dtos.FamilyDirective;
import com.example.entity.*;
import com.example.enums.*;
import com.example.repository.DirectiveRepo;
import com.example.repository.DirectiveRepository;
import com.example.repository.FamilyRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class DirectiveService {
    private final DirectiveRepository directiveRepository;
    private final GuardService guardService;
    private final FamilyServiceImp familyService;
    private final MemberService memberService;
    private final DirectiveRepo directiveRepo;
    private final List<Directive> cloakDirective;
    private final SendAndFormService sendAndFormService;
    private final Map<UUID, Localisation> tempLocalisation;
    private final FamilyRepository familyRepository;

    public DirectiveService(DirectiveRepository directiveRepository, GuardService guardService,
                            FamilyServiceImp familyService,
                            MemberService memberService,
                            DirectiveRepo directiveRepo,
                            List<Directive> cloakDirective,
                            SendAndFormService sendAndFormService, Map<UUID, Localisation> tempLocalisation, FamilyRepository familyRepository) {
        this.directiveRepository = directiveRepository;
        this.guardService = guardService;
        this.familyService = familyService;
        this.memberService = memberService;
        this.directiveRepo = directiveRepo;
        this.cloakDirective = cloakDirective;
        this.sendAndFormService = sendAndFormService;
        this.tempLocalisation = tempLocalisation;
        this.familyRepository = familyRepository;
    }

    @Transactional
    public void checkSaveAndSendVotingDirective(List<DeferredDirective> directiveList) {
        boolean remove = false;
        Set<DirectiveMember> directiveMembersFromMain = new HashSet<>();
        if (directiveList.size() > 1 && directiveList.get(0).getSwitchPosition() == SwitchPosition.MAIN)
            for (DeferredDirective dd :
                    directiveList) {
                if (dd.getSwitchPosition() == SwitchPosition.FATHER || dd.getSwitchPosition() == SwitchPosition.MOTHER) {
                    remove = true;
                    break;
                }
            }
        if (directiveList.get(0).getSwitchPosition() == SwitchPosition.MAIN)
            directiveMembersFromMain = directiveList.get(0).getShortFamilyMemberLink();
        if (remove) directiveList.remove(0);
        directiveRepo.saveAll(directiveList);

        List<DirectiveMember> list = new ArrayList<>();

        for (DeferredDirective dd :
                directiveList) {
            DirectiveMember mainMember = DirectiveMember.builder()
                    .directive(dd)
                    .directiveMember(dd.getDirectiveMember())
                    .build();
            if (dd.getSwitchPosition() == SwitchPosition.BIRTH) dd.getShortFamilyMemberLink().add(mainMember);
            else if (dd.getSwitchPosition() != SwitchPosition.MAIN) {
                DirectiveMember directiveLinkPerson = dd.getShortFamilyMemberLink().stream().findFirst().orElseThrow(() -> new RuntimeException("corrupt directive"));
                directiveLinkPerson.setDirective(dd);
                list.add(directiveLinkPerson);
            }
            if (dd.getSwitchPosition() == SwitchPosition.MAIN ||
                    (remove && (dd.getSwitchPosition() == SwitchPosition.MOTHER || dd.getSwitchPosition() == SwitchPosition.FATHER))) {
                for (DirectiveMember dm :
                        directiveMembersFromMain) {
                    dm.setDirective(dd);
                }
                list.addAll(directiveMembersFromMain);
            }
            list.add(mainMember);

        }
        directiveRepository.saveAllDirectiveMembers(list);


        for (DeferredDirective dd :
                directiveList) {

            if (dd.getSwitchPosition() != SwitchPosition.MAIN) {
                sendAndFormService.sendVotingDirective(dd, guardService.getMaxLevelGuards(dd.getShortFamilyMemberLink().stream()
                        .map(DirectiveMember::getDirectiveMember)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("object under guarding not found in directive"))));
            } else {
                sendAndFormService.sendVotingDirective(dd, dd.getShortFamilyMemberLink().stream()
                        .map(DirectiveMember::getDirectiveMember)
                        .flatMap(x -> guardService.getMaxLevelGuards(x).stream())
                        .collect(Collectors.toSet()));
            }
        }
    }

    @Transactional
    public void setChangesFromVotingDirective(String directiveUuid) {
        DeferredDirective deferredDirective = directiveRepository.findDirectiveWithPrimeMember(UUID.fromString(directiveUuid)).orElseThrow(() -> new RuntimeException("Deferred directive is missing or corrupt"));
        Set<Family> familyToRemove = new HashSet<>();
        ShortFamilyMember mainMember = deferredDirective.getDirectiveMember();
        Set<DirectiveMember> directiveMembers = directiveRepository.getListMembersOfDirective(deferredDirective);
        Set<ShortFamilyMember> processMembers = directiveMembers.stream().map(DirectiveMember::getDirectiveMember).collect(Collectors.toSet());
        SwitchPosition switchPosition = deferredDirective.getSwitchPosition();
        UUID directiveKeeper = deferredDirective.getDirectiveKeeper();

        directiveRepo.delete(deferredDirective);

        Set<String> setLinkedStatus = new HashSet<>();
        Set<String> setCheckedStatus = new HashSet<>();
        Set<String> setUncheckedStatus = new HashSet<>();
        ShortFamilyMember processMemberKeeper = processMembers.stream().filter(x -> Objects.equals(x.getUuid(), directiveKeeper)).findFirst().orElseThrow();

        boolean tempMainStatus = ((processMemberKeeper.getLinkGuard() != null && !processMemberKeeper.getLinkGuard().isBlank())
                || (processMemberKeeper.getDescendantsGuard() != null && !processMemberKeeper.getDescendantsGuard().isBlank()));
        for (ShortFamilyMember processMember :
                processMembers) {
            List<DirectiveMember> existOtherDirective = directiveRepository.checkForExistDirectiveMember(processMember);
            if (existOtherDirective.isEmpty()) {
                CheckStatus checkStatus = memberService.getCheckStatus(processMember, switchPosition == SwitchPosition.MAIN || !tempMainStatus);
                processMember.setCheckStatus(checkStatus);
                switch (checkStatus) {
                    case LINKED -> setLinkedStatus.add(processMember.getUuid().toString());
                    case CHECKED -> {
                        setCheckedStatus.add(processMember.getUuid().toString());
                        processMember.setCreator(null);
                    }
                    case UNCHECKED -> setUncheckedStatus.add(processMember.getUuid().toString());
                    default -> log.warn("something wrong with CheckStatus");
                }
            }
        }
        processMembers.remove(mainMember);
        Family mainFamily = memberService.getPrimeFamily(mainMember);
        if (switchPosition == SwitchPosition.MAIN) {
            Family processFamily = memberService.getPrimeFamily(processMemberKeeper);
            processFamily.getChildren().add(mainMember);
            familyService.mergeFamilies(mainFamily, processFamily);
            mainMember.setFamilyWhereChild(processFamily);
            if (Objects.equals(mainMember.getActiveFamily().getId(), mainFamily.getId()))
                mainMember.setActiveFamily(processFamily);
            familyService.changeFamilyForPerson(mainFamily, processFamily, mainMember);
            log.info("merge family is done");
            familyToRemove.add(mainFamily);
        } else {
            Set<String> repair = new HashSet<>();
            Family childFamily;
            Optional<UUID> mayMerge = Optional.empty();
            ShortFamilyMember child;
            switch (switchPosition) {
                case FATHER -> {
                    mayMerge = familyService.addChangesFromFather(mainFamily,
                            mainMember,
                            processMemberKeeper);
                    childFamily = mainFamily;
                    child = mainMember;
                    log.info("father is setup");
                    repair.addAll(memberService.repairGeneticTreeCheckStatus(Set.of(processMemberKeeper)));
                }
                case MOTHER -> {
                    mayMerge = familyService.addChangesFromMother(mainFamily,
                            mainMember,
                            processMemberKeeper);
                    childFamily = mainFamily;
                    child = mainMember;
                    log.info("mother is setup");
                    repair.addAll(memberService.repairGeneticTreeCheckStatus(Set.of(processMemberKeeper)));
                }
                case CHILD -> {
                    childFamily = processMemberKeeper.getFamilyWhereChild();
                    if (mainMember.getSex() == Sex.MALE)
                        mayMerge = familyService.addChangesFromFather(
                                childFamily,
                                processMemberKeeper,
                                mainMember);
                    else
                        mayMerge = familyService.addChangesFromMother(
                                childFamily,
                                processMemberKeeper,
                                mainMember);

                    child = processMemberKeeper;
                    log.info("child is setup");
                    if (mainMember.getCheckStatus() != CheckStatus.UNCHECKED && mainMember.getCheckStatus() != CheckStatus.MODERATE) {
                        Set<ShortFamilyMember> newTopAc = memberService.getAllTopAncestors(mainMember);
//                    familyService.addPersonToFamily(childFamily, mainMember, (mainMember.getSex() == Sex.MALE) ? RoleInFamily.FATHER : RoleInFamily.MOTHER, processMemberKeeper.getUuid(), null);
                        repair.addAll(memberService.repairGeneticTreeCheckStatus(newTopAc));
                    }
                }
                default -> {
                    childFamily = new Family();
                    child = new ShortFamilyMember();
                    log.warn("Обнаружена неизвестная директива");
                }
            }
            processMemberKeeper.setLastUpdate(new Timestamp(System.currentTimeMillis()));
            if (mayMerge.isPresent()) {
                Optional<Family> family = familyRepository.findFamilyByUUID(mayMerge.get());
                if (family.isPresent() && !Objects.equals(family.get().getUuid(), childFamily.getUuid())) {
                    familyService.mergeFamilies(childFamily, family.get());
                    child.setFamilyWhereChild(family.get());
                    if (Objects.equals(child.getActiveFamily().getId(), childFamily.getId()))
                        mainMember.setActiveFamily(family.get());
                    family.get().getChildren().add(child);
                    memberService.updateMember(child);
                    familyToRemove.add(childFamily);
                } else childFamily.setUuid(mayMerge.get());
            }
            setCheckedStatus.addAll(repair);
        }
        memberService.flush();
        if (!familyToRemove.isEmpty()) for (Family f :
                familyToRemove) {
            familyService.removeFamily(f);
        }

        mainMember.setLastUpdate(new Timestamp(System.currentTimeMillis()));
        memberService.flush();


        if (!setCheckedStatus.isEmpty())
            sendAndFormService.formDirectiveToStorageForChangeStatus(deferredDirective.getTokenUser(), null, null, KafkaOperation.RENAME, setCheckedStatus, CheckStatus.CHECKED);
        if (!setLinkedStatus.isEmpty())
            sendAndFormService.formDirectiveToStorageForChangeStatus(deferredDirective.getTokenUser(), null, null, KafkaOperation.RENAME, setLinkedStatus, CheckStatus.LINKED);
        if (!setUncheckedStatus.isEmpty())
            sendAndFormService.formDirectiveToStorageForChangeStatus(deferredDirective.getTokenUser(), null, null, KafkaOperation.RENAME, setUncheckedStatus, CheckStatus.UNCHECKED);


        sendAndFormService.sendAddingNewContacts(memberService.getGeneticTreeGuards(memberService.getAllTopAncestors(mainMember)).stream().map(UUID::toString).collect(Collectors.toSet()));

    }

    @Transactional
    public void negativeVoting(String directiveUuid) {
        DeferredDirective deferredDirective = directiveRepository.findDirectiveWithPrimeMember(UUID.fromString(directiveUuid)).orElseThrow();
        ShortFamilyMember mainMember = deferredDirective.getDirectiveMember();
        Set<DirectiveMember> directiveMembers = directiveRepository.getListMembersOfDirective(deferredDirective);
        Set<ShortFamilyMember> processMembers = directiveMembers.stream().map(DirectiveMember::getDirectiveMember).collect(Collectors.toSet());
        SwitchPosition switchPosition = deferredDirective.getSwitchPosition();
        directiveRepo.delete(deferredDirective);

        Set<String> setLinkedStatus = new HashSet<>();
        Set<String> setCheckedStatus = new HashSet<>();
        Set<String> setUnCheckedStatus = new HashSet<>();

        for (ShortFamilyMember processMember :
                processMembers) {

            List<DirectiveMember> existOtherDirective = directiveRepository.checkForExistDirectiveMember(processMember);
            if (existOtherDirective.isEmpty()) {
                CheckStatus checkStatus = memberService.getCheckStatus(processMember, switchPosition == SwitchPosition.MAIN || switchPosition == SwitchPosition.CHILD);
                processMember.setCheckStatus(checkStatus);
                switch (checkStatus) {
                    case LINKED -> setLinkedStatus.add(processMember.getUuid().toString());
                    case CHECKED -> setCheckedStatus.add(processMember.getUuid().toString());
                    case UNCHECKED -> setUnCheckedStatus.add(processMember.getUuid().toString());
                    default -> log.warn("something wrong with CheckStatus");
                }

            }
        }
        processMembers.remove(mainMember);
        switch (switchPosition) {
            case MAIN ->
                    sendAndFormService.sendChangeInStorageByNegative(deferredDirective, mainMember.getUuid().toString());

            case MOTHER -> {
                mainMember.setMotherInfo(null);
                sendAndFormService.sendChangeInStorageByNegative(deferredDirective, mainMember.getUuid().toString());
            }
            case FATHER -> {
                mainMember.setFatherInfo(null);
                sendAndFormService.sendChangeInStorageByNegative(deferredDirective, mainMember.getUuid().toString());
            }
            case CHILD -> {
                for (ShortFamilyMember child :
                        processMembers) {
                    Family family = memberService.getPrimeFamily(child);

                    if (family.getChildren().size() == 1) {
                        family.setUuid(child.getUuid());
                        if (mainMember.getSex() == Sex.MALE) {
                            child.setFatherInfo(null);
                            family.setHusbandInfo(null);
                        } else {
                            child.setMotherInfo(null);
                            family.setWifeInfo(null);
                        }
                    } else {
                        if (mainMember.getSex() == Sex.MALE) {
                            child.setFatherInfo(null);
                        } else {
                            child.setMotherInfo(null);
                        }
                        Family newFamily = familyService.creatFreeFamily(child.getFatherInfo(), child.getMotherInfo(), child.getUuid(), child.getBirthday());
                        family.getChildren().remove(mainMember);
                        newFamily.getChildren().add(mainMember);
                    }
                    sendAndFormService.sendChangeInStorageByNegative(deferredDirective, child.getUuid().toString());
                }
            }
            default -> log.warn("unknown directive");
        }
        if (!setUnCheckedStatus.isEmpty())
            sendAndFormService.formDirectiveToStorageForChangeStatus(deferredDirective.getTokenUser(), null, null, KafkaOperation.RENAME, setUnCheckedStatus, CheckStatus.UNCHECKED);
        if (!setCheckedStatus.isEmpty())
            sendAndFormService.formDirectiveToStorageForChangeStatus(deferredDirective.getTokenUser(), null, null, KafkaOperation.RENAME, setCheckedStatus, CheckStatus.CHECKED);
        if (!setLinkedStatus.isEmpty())
            sendAndFormService.formDirectiveToStorageForChangeStatus(deferredDirective.getTokenUser(), null, null, KafkaOperation.RENAME, setLinkedStatus, CheckStatus.LINKED);

    }

    @Transactional
    public void setLinkGuardFromVotingDirective(String directiveUuid) {
        DeferredDirective directive = directiveRepository.getLinkingDirective(UUID.fromString(directiveUuid)).orElseThrow(() -> new RuntimeException("directive is missing"));
        guardService.creatLinkingGuard(directive.getDirectiveMember(), directive.getTokenUser());
        log.info("New guard is created");
        cloakDirective.add(Directive.builder()
                .operation(KafkaOperation.EDIT)
                .tokenUser(directive.getInfo())
                .person(directive.getTokenUser())
                .build());
        sendAndFormService.sendAddingNewContacts(memberService.getGeneticTreeGuards(memberService.getAllTopAncestors(directive.getDirectiveMember())).stream().map(UUID::toString).collect(Collectors.toSet()));
        directiveRepo.delete(directive);

    }

    @Transactional
    public void rejectLinkGuard(String directiveUuid) {
        DeferredDirective directive = directiveRepo.findById(UUID.fromString(directiveUuid)).orElseThrow(() -> new RuntimeException("directive is missing"));
        ShortFamilyMember shortFamilyMember = memberService.getMemberByUuid(directive.getDirectiveMember().getUuid()).orElseThrow(() -> new RuntimeException("не найден человек"));
        shortFamilyMember.setCheckStatus(CheckStatus.CHECKED);
        shortFamilyMember.setLastUpdate(new Timestamp(System.currentTimeMillis()));
        memberService.updateMember(shortFamilyMember);
        sendAndFormService.formDirectiveToStorageForChangeStatus(directive.getTokenUser(), shortFamilyMember.getUuid().toString(), null, KafkaOperation.RENAME, null, CheckStatus.CHECKED);
        sendAndFormService.sendAttentionToUser(directive.getTokenUser(), shortFamilyMember.getFullName(), null, Attention.LINK);
        log.info("reject request for linking");
        directiveRepo.delete(directive);
    }

    public void setLanguish(DirectiveGuards directive) {
        tempLocalisation.put(UUID.fromString(directive.getTokenUser()), directive.getLocalisation());
    }

    public void setLanguish(FamilyDirective directive) {
        tempLocalisation.put(UUID.fromString(directive.getTokenUser()), directive.getLocalisation());
    }
}
