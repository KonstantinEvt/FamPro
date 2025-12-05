package com.example.service;

import com.example.dtos.Directive;
import com.example.entity.*;
import com.example.enums.*;
import com.example.repository.DirectiveRepo;
import com.example.repository.DirectiveRepository;
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

    public DirectiveService(DirectiveRepository directiveRepository, GuardService guardService,
                            FamilyServiceImp familyService,
                            MemberService memberService,
                            DirectiveRepo directiveRepo,
                            List<Directive> cloakDirective,
                            SendAndFormService sendAndFormService) {
        this.directiveRepository = directiveRepository;
        this.guardService = guardService;
        this.familyService = familyService;
        this.memberService = memberService;
        this.directiveRepo = directiveRepo;
        this.cloakDirective = cloakDirective;
        this.sendAndFormService = sendAndFormService;
    }

    @Transactional
    public void checkSaveAndSendVotingDirective(List<DeferredDirective> directiveList) {
        boolean remove = false;
        if (directiveList.size() > 1 && directiveList.get(0).getSwitchPosition() == SwitchPosition.MAIN)
            for (DeferredDirective dd :
                    directiveList) {
                if (dd.getSwitchPosition() == SwitchPosition.FATHER || dd.getSwitchPosition() == SwitchPosition.MOTHER) {
                    remove = true;
                    break;
                }
            }
        if (remove) directiveList.remove(0);
        directiveRepo.saveAll(directiveList);

        List<DirectiveMembers> list = new ArrayList<>();
        for (DeferredDirective dd :
                directiveList) {
            list.add(DirectiveMembers.builder().directive(dd).directiveMember(dd.getDirectiveMember()).build());
        }
        directiveRepository.saveAllDirectiveMembers(list);

        for (DeferredDirective dd :
                directiveList) {
            sendAndFormService.sendVotingDirective(dd, guardService.getMaxLevelGuards(dd.getShortFamilyMemberLink().stream()
                    .map(DirectiveMembers::getDirectiveMember)
                    .filter(x -> !Objects.equals(dd.getDirectiveMember(), x) || dd.getSwitchPosition() == SwitchPosition.BIRTH)
                    .findFirst()
                    .orElseThrow(()->new RuntimeException("object under guarding not found in directive"))));
        }
    }

    @Transactional
    public void setChangesFromVotingDirective(String directiveUuid) {
        DeferredDirective deferredDirective = directiveRepository.findDirectiveWithPrimeMember(UUID.fromString(directiveUuid)).orElseThrow(() -> new RuntimeException("Deferred directive is missing or corrupt"));

        ShortFamilyMember mainMember = deferredDirective.getDirectiveMember();
        Set<DirectiveMembers> directiveMembers=directiveRepository.getListMembersOfDirective(deferredDirective);
        Set<ShortFamilyMember> processMembers = directiveMembers.stream().map(DirectiveMembers::getDirectiveMember).collect(Collectors.toSet());
        SwitchPosition switchPosition = deferredDirective.getSwitchPosition();
        directiveRepo.delete(deferredDirective);

        Set<String> setLinkedStatus = new HashSet<>();
        Set<String> setCheckedStatus = new HashSet<>();

        for (ShortFamilyMember processMember :
                processMembers) {
            if (directiveRepository.checkForExistDirectiveMember(processMember) == 0) {
                if (processMember.getLinkGuard() != null && !processMember.getLinkGuard().isBlank()) {
                    processMember.setCheckStatus(CheckStatus.LINKED);
                    setLinkedStatus.add(processMember.getUuid().toString());
                } else {
                    processMember.setCheckStatus(CheckStatus.CHECKED);
                    setCheckedStatus.add(processMember.getUuid().toString());
                }
            }
        }
        processMembers.remove(mainMember);
        Family mainFamily = memberService.getPrimeFamily(mainMember);
        if (switchPosition == SwitchPosition.MAIN) {
            ShortFamilyMember processMember = processMembers.stream().findFirst().orElseThrow();
            Family processFamily = memberService.getPrimeFamily(processMember);
            familyService.mergeFamilies(mainFamily, processFamily);
            mainMember.setFamilyWhereChild(processFamily);
            log.info("merge family is done");
        } else {

            Set<Family> childFamilies = new HashSet<>();
            for (ShortFamilyMember processMember :
                    processMembers) {

                switch (switchPosition) {
                    case FATHER -> {
                        familyService.addChangesFromFather(mainFamily,
                                mainMember,
                                processMember);
                        childFamilies.add(mainFamily);
                        log.info("father is setup");
                    }
                    case MOTHER -> {
                        familyService.addChangesFromMother(mainFamily,
                                mainMember,
                                processMember);
                        childFamilies.add(mainFamily);
                        log.info("mother is setup");
                    }
                    case CHILD -> {
                        Family processFamily = processMember.getFamilyWhereChild();
                        if (mainMember.getSex() == Sex.MALE)
                            familyService.addChangesFromFather(
                                    processFamily,
                                    processMember,
                                    mainMember);
                        else
                            familyService.addChangesFromMother(
                                    processFamily,
                                    processMember,
                                    mainMember);
                        memberService.addChildToFamilyMember(mainMember, processMember, processMember.getSex());
//                        memberService.updateMember(processMember);
                        childFamilies.add(processFamily);
                        log.info("child is setup");
                    }
                    default -> log.warn("Обнаружена неизвестная директива");
                }
                processMember.setLastUpdate(new Timestamp(System.currentTimeMillis()));
                checkForUnique(childFamilies);
            }
        }
        mainMember.setLastUpdate(new Timestamp(System.currentTimeMillis()));

        memberService.flush();


        if (!setCheckedStatus.isEmpty())
            sendAndFormService.formDirectiveToStorageForChangeStatus(deferredDirective.getTokenUser(), null, null, KafkaOperation.RENAME, setCheckedStatus, CheckStatus.CHECKED);
        if (!setLinkedStatus.isEmpty())
            sendAndFormService.formDirectiveToStorageForChangeStatus(deferredDirective.getTokenUser(), null, null, KafkaOperation.RENAME, setLinkedStatus, CheckStatus.LINKED);

        sendAndFormService.sendAddingNewContacts(memberService.getGeneticTreeGuards(memberService.getAllTopAncestors(mainMember)).stream().map(UUID::toString).collect(Collectors.toSet()));

    }

    private void checkForUnique(Set<Family> childrenFamilies) {
        Family family = childrenFamilies.stream().findFirst().orElseThrow(() -> new RuntimeException("nonsense!"));
        for (Family fam : childrenFamilies) {
            if (Objects.equals(fam.getUuid(), family.getUuid()) && fam != family) {
                familyService.mergeFamilies(fam, family);
//                тут проверить на concurrentModi
            }
        }
    }

    @Transactional
    public void negativeVoting(String directiveUuid) {
        DeferredDirective deferredDirective = directiveRepository.findDirectiveWithPrimeMember(UUID.fromString(directiveUuid)).orElseThrow();
        ShortFamilyMember mainMember = deferredDirective.getDirectiveMember();
        Set<DirectiveMembers> directiveMembers=directiveRepository.getListMembersOfDirective(deferredDirective);
        Set<ShortFamilyMember> processMembers = directiveMembers.stream().map(DirectiveMembers::getDirectiveMember).collect(Collectors.toSet());
        SwitchPosition switchPosition = deferredDirective.getSwitchPosition();
        directiveRepo.delete(deferredDirective);

        Set<String> setLinkedStatus = new HashSet<>();
        Set<String> setCheckedStatus = new HashSet<>();
        Set<String> setUnCheckedStatus = new HashSet<>();

        for (ShortFamilyMember processMember :
                processMembers) {
            if (directiveRepository.checkForExistDirectiveMember(processMember) == 0) {
                if (processMember.getLinkGuard() != null && !processMember.getLinkGuard().isBlank()) {
                    processMember.setCheckStatus(CheckStatus.LINKED);
                    setLinkedStatus.add(processMember.getUuid().toString());
                } else {
                    if ((processMember.getDescendantsGuard() != null && !processMember.getDescendantsGuard().isBlank()) ||
                            (processMember.getAncestorsGuard() != null && !processMember.getActiveGuard().isBlank()) || (
                            processMember.getActiveGuard() != null && !processMember.getActiveGuard().isBlank())) {
                        processMember.setCheckStatus(CheckStatus.CHECKED);
                        setCheckedStatus.add(processMember.getUuid().toString());
                    } else {
                        if (memberService.getGeneticTreeGuards(memberService.getAllTopAncestors(processMember)).isEmpty()) {
                            processMember.setCheckStatus(CheckStatus.UNCHECKED);
                            setUnCheckedStatus.add(processMember.getUuid().toString());
                        } else {
                            processMember.setCheckStatus(CheckStatus.CHECKED);
                            setCheckedStatus.add(processMember.getUuid().toString());
                        }
                    }
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
                        Family newFamily = familyService.creatFreeFamily(child.getFatherInfo(), child.getMotherInfo(), child.getUuid());
                        family.getChildren().remove(mainMember);
                        newFamily.getChildren().add(mainMember);
                        if (child.getMotherUuid() != null) {
                            family.getHalfChildrenByMother().add(mainMember);
                            newFamily.getHalfChildrenByMother().addAll(family.getChildren());
                        }
                        if (child.getFatherUuid() != null) {
                            family.getHalfChildrenByFather().add(mainMember);
                            newFamily.getHalfChildrenByFather().addAll(family.getChildren());
                        }
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
        if (directive.getDirectiveMember() == null || !directive.getDirectiveMember().getLinkedGuard().isEmpty())
            throw new RuntimeException("directive is corrupt");
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
}
