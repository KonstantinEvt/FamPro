//package com.example.service;
//
//import com.example.dtos.FamilyDirective;
//import com.example.entity.Family;
//import com.example.entity.GlobalFamily;
//import com.example.entity.Guard;
//import com.example.entity.ShortFamilyMember;
//import com.example.enums.CheckStatus;
//import com.example.enums.KafkaOperation;
//import com.example.enums.SwitchPosition;
//import com.example.repository.GlobalFamilyRepo;
//import com.example.repository.MainFamilyRepo;
//import com.example.repository.ShortMemberRepo;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@Service
//@Setter
//@Getter
//public class GlobalFamilyService {
//    private GlobalFamilyRepo globalFamilyRepo;
//    private GuardService guardService;
//    private MainFamilyRepo mainFamilyRepo;
//    private final List<FamilyDirective> storageDirective;
//    private ShortMemberRepo shortMemberRepo;
//
//    public GlobalFamilyService(GlobalFamilyRepo globalFamilyRepo, GuardService guardService, MainFamilyRepo mainFamilyRepo, List<FamilyDirective> storageDirective, ShortMemberRepo shortMemberRepo) {
//        this.globalFamilyRepo = globalFamilyRepo;
//        this.guardService = guardService;
//        this.mainFamilyRepo = mainFamilyRepo;
//        this.storageDirective = storageDirective;
//        this.shortMemberRepo = shortMemberRepo;
//    }
//
//    public void creatNewGlobalFamily(Family primeFamily) {
//        GlobalFamily globalFamily = new GlobalFamily();
//        globalFamily.setGuard(new HashSet<>());
//        globalFamily.setMembers(new HashSet<>());
//        globalFamily.getMembers().add(primeFamily);
//        globalFamily.setNumber(1);
//        primeFamily.setGlobalFamily(globalFamily);
//    }
//
//    @Transactional
//    public GlobalFamily mergeGlobalFamilies(GlobalFamily family1, GlobalFamily family2) {
//        if (family1 != family2) {
//            System.out.println("Слияние глобальных семей");
//
//            System.out.println("Счас бум изменять семьи");
//            if (family2.getGuard() != null && !family2.getGuard().isEmpty()) {
//                for (Guard guard :
//                        family2.getGuard()) {
//                    guardService.addGuardToGlobalFamily(guard, family1);
//                }
//            }
//            System.out.println("Добавили Стражу");
//            for (Family f :
//                    family2.getMembers()) {
//                f.setGlobalFamily(family1);
//                family1.getMembers().add(f);
//            }
//            System.out.println("Добавили членов");
//            globalFamilyRepo.delete(family2);
//            System.out.println("Удалили старую. Счас бум сохранять изменения");
//            family1.setNumber(family1.getMembers().size());
//            return globalFamilyRepo.save(family1);
//        } else return family1;
//    }
//
//    public void changeStatusFamiliesMembersByGuarding(GlobalFamily globalFamily) {
//        Set<ShortFamilyMember> memberSet = mainFamilyRepo.findFamilyMembersOfGlobal(globalFamily);
//        Set<ShortFamilyMember> membersToChange = new HashSet<>();
//        for (ShortFamilyMember member :
//                memberSet) {
//            if (member.getCheckStatus() == CheckStatus.UNCHECKED) {
//                member.setCheckStatus(CheckStatus.CHECKED);
//                storageDirective.add(FamilyDirective.builder()
//                        .person(member.getUuid().toString())
//                        .switchPosition(SwitchPosition.MOTHER)
//                        .operation(KafkaOperation.RENAME)
//                        .build());
//                membersToChange.add(member);
//            }
//        }
//        if (!membersToChange.isEmpty()) shortMemberRepo.saveAll(membersToChange);
//    }
//}
