package com.example.service;

import com.example.entity.FamilyMemberInfo;
import com.example.entity.Phone;
import com.example.exceptions.UncorrectedInformation;
import com.example.mappers.PhoneMapper;
import com.example.repository.PhoneRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class PhoneService implements InternService<Phone> {
    private final PhoneMapper phoneMapper;
    private final PhoneRepo phoneRepo;

    @Override
    public Phone merge(Phone newPhone, Phone oldPhone) {
        if (newPhone.getAssignment() != null) oldPhone.setAssignment(newPhone.getAssignment());
        if (newPhone.getStatus() != null) oldPhone.setStatus(newPhone.getStatus());
        if (newPhone.getDescription() != null) oldPhone.setDescription(newPhone.getDescription());
    return oldPhone;
    }

    @Override
    public void check(Phone phone) {
    }

    @Override
    public void checkMergeAndSetUp(FamilyMemberInfo newFmi, FamilyMemberInfo fmiFromBase) {
        Set<String> namesPhones = new HashSet<>();
        if (newFmi.getMainPhone() == null) newFmi.setMainPhone(new Phone());
        else {
            check(newFmi.getMainPhone());
            namesPhones.add(newFmi.getMainPhone().getPhoneNumber());
            if (newFmi.getMainPhone().getId() != null) {
                newFmi.getMainPhone().setId(null);
                log.warn("Предоставленная информация об основном телефоне имела ID, ID обнулен");
            }
            if (newFmi.getMainPhone().getPhoneNumber().equals("uncorrected")) {
                newFmi.setMainPhone(null);
                log.warn("Предоставленная информация об основном телефоне некорректна, основной Email обнулен");
            }
        }

        if (newFmi.getPhones() == null) newFmi.setPhones(new HashSet<>());
        else {
            for (Phone phone : newFmi.getPhones()) {
                check(phone);
                namesPhones.add(phone.getPhoneNumber());
                phone.setPhoneNumber(phone.getPhoneNumber());
                if (phone.getId() != null) {
                    phone.setId(null);
                    log.warn("Предоставленная информация об телефоне имела ID, ID обнулен");
                }
            }
        }
        newFmi.getPhones().add(newFmi.getMainPhone());
        if (namesPhones.contains("uncorrected") && namesPhones.size() == 1) {
            throw new UncorrectedInformation("Все введенные телефоны некорректны");
        } else namesPhones.remove("uncorrected");

        Set<Phone> phonesFromBase = getAllInternEntityByNames(namesPhones);
        Set<Phone> resultPhones = new HashSet<>();
        Set<String> namesFromBase = new HashSet<>();
        for (Phone phone : phonesFromBase) {
            merge(newFmi.getMainPhone(), phone);
            if (phone.getPhoneNumber().equals(newFmi.getMainPhone().getPhoneNumber())) {
                newFmi.setMainPhone(phone);
            }
            resultPhones.add(phone);
            namesFromBase.add(phone.getPhoneNumber());
        }
        if (newFmi.getMainPhone() == null && fmiFromBase.getMainEmail() != null)
            newFmi.setMainPhone(fmiFromBase.getMainPhone());

        if (fmiFromBase.getPhones() != null) resultPhones.addAll(fmiFromBase.getPhones());
        for (String name : namesPhones) {
            if (!namesFromBase.contains(name)) {
                for (Phone em : newFmi.getPhones()) {
                    if (em.getPhoneNumber().equals(name)) resultPhones.add(em);
                }
            }
        }
        newFmi.setPhones(resultPhones);
        log.info("телефон(ы) установлен(ы)");
    }

    @Override
    public Set<Phone> getAllInternEntityByNames(Set<String> names) {
        return phoneRepo.findAllByPhoneNumberIn(names);
    }
}
