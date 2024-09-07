package com.example.service;

import com.example.entity.Email;
import com.example.entity.FamilyMemberInfo;
import com.example.exceptions.UncorrectedInformation;
import com.example.mappers.EmailMapper;
import com.example.repository.EmailRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService extends InternServiceImp<Email> {
    private final EmailMapper emailMapper;
    private final EmailRepo emailRepo;

    @Override
    public Set<Email> getAllInternEntityByNames(Set<String> names) {
        return emailRepo.findAllByEmailNameIn(names);
    }

    @Override
    public void check(Email email) {
        if (email.getEmailName() != null) {
            email.setTechString(email.getEmailName());
            super.check(email);
        } else email.setTechString("uncorrected");
    }

    public void checkingSet(Set<String> str, Set<Email> emails) {
    }

    @Override
    public void checkMergeAndSetUp(FamilyMemberInfo newFmi, FamilyMemberInfo fmiFromBase) {
        Set<String> namesEmails = new HashSet<>();
        Email mainEmail = new Email();
        if (newFmi.getMainEmail() != null) {
            mainEmail.setEmailName(newFmi.getMainEmail());
            if (!newFmi.getEmails().add(mainEmail)) {
                newFmi.getEmails().remove(mainEmail);
                newFmi.getEmails().add(mainEmail);
            }
        }
        if (!newFmi.getEmails().isEmpty())
//            checkingSet(namesEmails,newFmi.getEmails());
            for (Email email : newFmi.getEmails()) {
                check(email);
                if (!email.getTechString().equals("uncorrected")) {
                    namesEmails.add(email.getEmailName());
                    if (email.getId() != null) {
                        email.setId(null);
                    }
                }
            }
        if (!mainEmail.getTechString().equals("uncorrected")) newFmi.setMainEmail(mainEmail.getEmailName());
        else log.warn("Предоставленная информация об основном Email некорректна, основной Email обнулен");

        if (newFmi.getMainEmail() == null && fmiFromBase.getMainEmail() != null) {
            newFmi.setMainEmail(fmiFromBase.getMainEmail());
            log.info("Основной Email взят из старой записи, т.к. валидной информцаии об основном Email в новой записи нет");
        }

        Set<Email> emailsFromBase;
        if (!namesEmails.isEmpty()) emailsFromBase = getAllInternEntityByNames(namesEmails);
        else emailsFromBase = new HashSet<>();

        Collection<Email> resultList = mergeSetsOfInterns(newFmi.getEmails(), fmiFromBase.getEmails(), emailsFromBase);

        newFmi.setEmails(new HashSet<>());
        for (Email email : resultList) newFmi.getEmails().add(email);
        log.info("Email(s) установлен(ы)");
    }
}
