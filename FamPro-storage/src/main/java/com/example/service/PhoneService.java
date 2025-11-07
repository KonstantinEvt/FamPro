package com.example.service;

import com.example.entity.FamilyMemberInfo;
import com.example.entity.Phone;
import com.example.enums.CheckStatus;
import com.example.repository.InternRepo;
import com.example.repository.MainPhoneRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class PhoneService extends InternServiceImp<Phone> {
    private final MainPhoneRepository mainPhoneRepository;
    public PhoneService(@Qualifier("phoneRepo") InternRepo<Phone> internRepo, MainPhoneRepository mainPhoneRepository) {
        super(internRepo);
        this.mainPhoneRepository = mainPhoneRepository;
    }

    @Override
    public void check(Phone phone) {
        if (phone.getInternName() != null) {
            if (phone.getCheckStatus() != CheckStatus.CHECKED) {
                if (phone.getTechString() == null) phone.setTechString("ONE USER");
                super.check(phone);
            }
        } else phone.setTechString("uncorrected");
    }

    @Override
    public void checkMergeAndSetUp(FamilyMemberInfo newFmi, FamilyMemberInfo fmiFromBase) {
        Set<String> namesPhones = new HashSet<>();
        Phone mainPhone = new Phone();
        if (newFmi.getMainPhone() != null) {
            mainPhone.setInternName(newFmi.getMainPhone());
            check(mainPhone);
            if (!mainPhone.getTechString().equals("uncorrected")) {
                newFmi.setMainPhone(mainPhone.getInternName());
                namesPhones.add(mainPhone.getInternName());
            } else newFmi.setMainPhone(null);
        }
        if (newFmi.getPhonesSet() != null && !newFmi.getPhonesSet().isEmpty()) {
            for (Phone phone : newFmi.getPhonesSet()) {
                check(phone);
                if (!phone.getTechString().equals("uncorrected")) {
//                    phone.setUuid(newFmi.getUuid());
                    namesPhones.add(phone.getInternName());
                    if (phone.getId() != null) phone.setId(null);
                }
            }
        }
        if (newFmi.getMainPhone() == null|| newFmi.getMainPhone().isEmpty()) {

            log.info("Основной телефон взят из старой записи, т.к. валидной информации об основном телефоне в новой записи нет");
        }else  fmiFromBase.setMainPhone(newFmi.getMainPhone());

        Set<Phone> phonesFromBase;
        if (!namesPhones.isEmpty()) phonesFromBase = getAllInternEntityByNames(namesPhones);
        else phonesFromBase = new HashSet<>();

        Map<String, Phone> resultList = mergeSetsOfInterns(newFmi.getPhonesSet(), fmiFromBase.getPhonesSet(), phonesFromBase);
        if (mainPhone.getInternName() != null && !resultList.containsKey(mainPhone.getInternName())) {
            if (!phonesFromBase.isEmpty() && !mainPhone.getTechString().equals("uncorrected"))
                this.checkForCommunity(mainPhone, fmiFromBase.getPhonesSet(), phonesFromBase);
            if (!mainPhone.getTechString().equals("uncorrected")) {
                if (!mainPhone.getTechString().equals("COMMUNITY")) {
                    mainPhone.setDescription("Main phone");
                    mainPhone.setUuid(newFmi.getUuid());
                }
                mainPhone.setId(null);
                resultList.put(mainPhone.getInternName(), mainPhone);
            }
        }
        fmiFromBase.setPhonesSet(new HashSet<>());
        for (Phone phone : resultList.values()) {
            if (!phone.getTechString().equals("COMMUNITY")) phone.setUuid(newFmi.getUuid());
            else phone.setUuid(null);
            fmiFromBase.getPhonesSet().add(phone);
        }
        log.info("Телефон(ы) установлен(ы)");
    }
    public Set<Phone> getPhonesByInfoId(Long id){
        return mainPhoneRepository.findPhonesOfPerson(id);
    }
}