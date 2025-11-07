package com.example.service;

import com.example.entity.Email;
import com.example.entity.FamilyMemberInfo;
import com.example.enums.CheckStatus;
import com.example.repository.InternRepo;

import com.example.repository.MainEmailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class EmailService extends InternServiceImp<Email> {
private final MainEmailRepository mainEmailRepository;
    EmailService(@Qualifier("emailRepo") InternRepo<Email> internRepo, MainEmailRepository mainEmailRepository) {
        super(internRepo);
        this.mainEmailRepository = mainEmailRepository;
    }

    @Override
    public void check(Email email) {
        if (email.getInternName() != null) {
            if (email.getCheckStatus() != CheckStatus.CHECKED) {
                if (email.getTechString() == null) email.setTechString("ONE USER");
                super.check(email);
            }
        } else email.setTechString("uncorrected");
    }

    @Override
    public void checkMergeAndSetUp(FamilyMemberInfo newFmi, FamilyMemberInfo fmiFromBase) {
        Set<String> namesEmails = new HashSet<>();
        Email mainEmail = new Email();
        if (newFmi.getMainEmail() != null) {
            mainEmail.setInternName(newFmi.getMainEmail());
            check(mainEmail);
            if (!mainEmail.getTechString().equals("uncorrected")) {
                newFmi.setMainEmail(mainEmail.getInternName());
                namesEmails.add(mainEmail.getInternName());
            } else newFmi.setMainEmail(null);
        }
        if (newFmi.getEmailsSet() != null && !newFmi.getEmailsSet().isEmpty()) {
            for (Email email : newFmi.getEmailsSet()) {
                check(email);
                if (!email.getTechString().equals("uncorrected")) {
//                    email.setUuid(newFmi.getUuid());
                    namesEmails.add(email.getInternName());
                    if (email.getId() != null) email.setId(null);
                }
            }
        }
        if (newFmi.getMainEmail() == null || newFmi.getMainEmail().isEmpty()) {
            log.info("Основной Email взят из старой записи, т.к. валидной информации об основном Email в новой записи нет");
        }else fmiFromBase.setMainEmail(newFmi.getMainEmail());

        Set<Email> emailsFromBase;
        if (!namesEmails.isEmpty()) emailsFromBase = getAllInternEntityByNames(namesEmails);
        else emailsFromBase = new HashSet<>();

        Map<String, Email> resultList = mergeSetsOfInterns(newFmi.getEmailsSet(), fmiFromBase.getEmailsSet(), emailsFromBase);
        if (mainEmail.getInternName() != null && !resultList.containsKey(mainEmail.getInternName())) {
            if (!emailsFromBase.isEmpty() && !mainEmail.getTechString().equals("uncorrected"))
                this.checkForCommunity(mainEmail, fmiFromBase.getEmailsSet(), emailsFromBase);
            if (!mainEmail.getTechString().equals("uncorrected")) {
                if (!mainEmail.getTechString().equals("COMMUNITY")) {
                    mainEmail.setDescription("Main Email");
//                    mainEmail.setUuid(newFmi.getUuid());
                }
                mainEmail.setId(null);
                resultList.put(mainEmail.getInternName(), mainEmail);
            }
        }
        fmiFromBase.setEmailsSet(new HashSet<>());
        for (Email email : resultList.values()) {
            if (!email.getTechString().equals("COMMUNITY")) email.setUuid(newFmi.getUuid()); else email.setUuid(null);
            fmiFromBase.getEmailsSet().add(email);
        }
        log.info("Email(s) установлен(ы)");
    }
    public Set<Email> getEmailsByInfoId(Long id){
        return mainEmailRepository.findEmailsOfPerson(id);
    }
}
