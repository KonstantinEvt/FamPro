package com.example.service;

import com.example.entity.FamilyMemberInfo;
import com.example.entity.Phone;
import com.example.enums.CheckStatus;
import com.example.repository.InternRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class PhoneService extends InternServiceImp<Phone> {
    public PhoneService(@Qualifier("phoneRepo") InternRepo<Phone> internRepo) {
        super(internRepo);
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
        if (newFmi.getPhones() != null && !newFmi.getPhones().isEmpty()) {
            for (Phone phone : newFmi.getPhones()) {
                check(phone);
                if (!phone.getTechString().equals("uncorrected")) {
                    phone.setUuid(newFmi.getUuid());
                    namesPhones.add(phone.getInternName());
                    if (phone.getId() != null) phone.setId(null);
                }
            }
        }
        if (newFmi.getMainPhone() == null && fmiFromBase.getMainPhone() != null) {
            newFmi.setMainPhone(fmiFromBase.getMainPhone());
            log.info("Основной телефон взят из старой записи, т.к. валидной информации об основном телефоне в новой записи нет");
        }

        Set<Phone> phonesFromBase;
        if (!namesPhones.isEmpty()) phonesFromBase = getAllInternEntityByNames(namesPhones);
        else phonesFromBase = new HashSet<>();

        Map<String, Phone> resultList = mergeSetsOfInterns(newFmi.getPhones(), fmiFromBase.getPhones(), phonesFromBase);
        if (mainPhone.getInternName() != null && !resultList.containsKey(mainPhone.getInternName())) {
            if (!phonesFromBase.isEmpty() && !mainPhone.getTechString().equals("uncorrected"))
                this.checkForCommunity(mainPhone, fmiFromBase.getPhones(), phonesFromBase);
            if (!mainPhone.getTechString().equals("uncorrected")) {
                if (!mainPhone.getTechString().equals("COMMUNITY")) {
                    mainPhone.setDescription("Main phone");
                    mainPhone.setUuid(newFmi.getUuid());
                }
                mainPhone.setId(null);
                resultList.put(mainPhone.getInternName(), mainPhone);
            }
        }
        newFmi.setPhones(new HashSet<>());
        for (Phone phone : resultList.values()) newFmi.getPhones().add(phone);
        log.info("Телефон(ы) установлен(ы)");
    }
}