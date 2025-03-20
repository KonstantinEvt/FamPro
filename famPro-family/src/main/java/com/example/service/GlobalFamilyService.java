package com.example.service;

import com.example.entity.Family;
import com.example.entity.GlobalFamily;
import com.example.entity.Guard;
import com.example.repository.GlobalFamilyRepo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@AllArgsConstructor
@Setter
@Getter
public class GlobalFamilyService {
    private GlobalFamilyRepo globalFamilyRepo;
    private GuardService guardService;

    public void creatNewGlobalFamily(Family primeFamily) {
        GlobalFamily globalFamily = new GlobalFamily();
        globalFamily.setGuard(new HashSet<>());
        globalFamily.setMembers(new HashSet<>());
        globalFamily.getMembers().add(primeFamily);
        globalFamily.setNumber(1);
        primeFamily.setGlobalFamily(globalFamily);
    }

    @Transactional
    public GlobalFamily mergeGlobalFamilies(GlobalFamily family1, GlobalFamily family2) {
        if (family1 != family2) {
            System.out.println("Слияние глобальных семей");

            System.out.println("Счас бум изменять семьи");
            if (family2.getGuard() != null && !family2.getGuard().isEmpty()) {
                for (Guard guard :
                        family2.getGuard()) {
                    guardService.addGuardToGlobalFamily(guard, family1);
                }
            }
            System.out.println("Добавили Стражу");
            for (Family f :
                    family2.getMembers()) {
                f.setGlobalFamily(family1);
            }
            System.out.println("Добавили членов");
            globalFamilyRepo.delete(family2);
            System.out.println("Удалили старую. Счас бум сохранять изменения");
            family1.setNumber(family1.getMembers().size());
            return globalFamilyRepo.save(family1);
        } else return family1;

    }
}
