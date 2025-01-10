package com.example.service;

import com.example.entity.Email;
import com.example.entity.FamilyMemberInfo;
import com.example.enums.CheckStatus;
import com.example.repository.InternRepo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class EmailService extends InternServiceImp<Email> {

    public EmailService(@Qualifier("emailRepo") InternRepo<Email> internRepo) {
        super(internRepo);
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
        if (newFmi.getEmails() != null && !newFmi.getEmails().isEmpty()) {
            for (Email email : newFmi.getEmails()) {
                check(email);
                if (!email.getTechString().equals("uncorrected")) {
//                    email.setUuid(newFmi.getUuid());
                    namesEmails.add(email.getInternName());
                    if (email.getId() != null) email.setId(null);
                }
            }
        }
        if (newFmi.getMainEmail() == null && fmiFromBase.getMainEmail() != null) {
            newFmi.setMainEmail(fmiFromBase.getMainEmail());
            log.info("Основной Email взят из старой записи, т.к. валидной информации об основном Email в новой записи нет");
        }

        Set<Email> emailsFromBase;
        if (!namesEmails.isEmpty()) emailsFromBase = getAllInternEntityByNames(namesEmails);
        else emailsFromBase = new HashSet<>();

        Map<String, Email> resultList = mergeSetsOfInterns(newFmi.getEmails(), fmiFromBase.getEmails(), emailsFromBase);
        if (mainEmail.getInternName() != null && !resultList.containsKey(mainEmail.getInternName())) {
            if (!emailsFromBase.isEmpty() && !mainEmail.getTechString().equals("uncorrected"))
                this.checkForCommunity(mainEmail, fmiFromBase.getEmails(), emailsFromBase);
            if (!mainEmail.getTechString().equals("uncorrected")) {
                if (!mainEmail.getTechString().equals("COMMUNITY")) {
                    mainEmail.setDescription("Main Email");
//                    mainEmail.setUuid(newFmi.getUuid());
                }
                mainEmail.setId(null);
                resultList.put(mainEmail.getInternName(), mainEmail);
            }
        }
        newFmi.setEmails(new HashSet<>());
        for (Email email : resultList.values()) {
            if (!email.getTechString().equals("COMMUNITY")) email.setUuid(newFmi.getUuid()); else email.setUuid(null);
            newFmi.getEmails().add(email);
        }
        log.info("Email(s) установлен(ы)");
    }
}
