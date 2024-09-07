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
    public Email merge(Email oldEmail, Email newEmail) {
        if (newEmail.getAssignment() != null) oldEmail.setAssignment(newEmail.getAssignment());
        if (newEmail.getStatus() != null) oldEmail.setStatus(newEmail.getStatus());
        if (newEmail.getDescription() != null) oldEmail.setDescription(newEmail.getDescription());
        return oldEmail;
    }

    @Override
    public Set<Email> getAllInternEntityByNames(Set<String> names) {
        return emailRepo.findAllByEmailNameIn(names);
    }

    @Override
    public void check(Email email) {
        if (email.getEmailName() == null) email.setEmailName("uncorrected");
        super.check(email);
    }

    @Override
    public void checkMergeAndSetUp(FamilyMemberInfo newFmi, FamilyMemberInfo fmiFromBase) {
        Set<String> namesEmails = new HashSet<>();
        Email mainEmail = new Email();
        if (newFmi.getMainEmail() != null) {
            mainEmail.setEmailName(newFmi.getMainEmail());
            check(mainEmail);
            if (mainEmail.getEmailName().equals("uncorrected")) {
                newFmi.setMainEmail(null);
                log.warn("Предоставленная информация об основном Email некорректна, основной Email обнулен");
            } else {
                newFmi.setMainEmail(mainEmail.getEmailName());
                namesEmails.add(newFmi.getMainEmail());
            }
        }

        if (newFmi.getEmails() == null) newFmi.setEmails(new HashSet<>());
        else {
            for (Email email : newFmi.getEmails()) {
                check(email);
                if (!email.getEmailName().equals("uncorrected")) {
                    namesEmails.add(email.getEmailName());
                    if (email.getId() != null) {
                        email.setId(null);
                        log.warn("Предоставленная информация об Email имела ID, ID обнулен");
                    }
                }
            }
        }
        if (newFmi.getMainEmail() != null) newFmi.getEmails().add(mainEmail);

        if (newFmi.getMainEmail() == null && fmiFromBase.getMainEmail() != null)
            newFmi.setMainEmail(fmiFromBase.getMainEmail());

        Map<String, Email> resultMap = new HashMap<>();
        Set<Email> emailsFromBase = getAllInternEntityByNames(namesEmails);
        if (fmiFromBase.getEmails() != null && !fmiFromBase.getEmails().isEmpty())
            for (Email email : fmiFromBase.getEmails()) resultMap.put(email.getEmailName(), email);
        if (!emailsFromBase.isEmpty())
            for (Email email : emailsFromBase) resultMap.putIfAbsent(email.getEmailName(), email);
        for (Email email : newFmi.getEmails()) if (!email.getEmailName().equals("uncorrected")) resultMap.merge(email.getEmailName(), email, this::merge);
        newFmi.setEmails(new HashSet<>());
        for (Email email : resultMap.values()
        ) {
            newFmi.getEmails().add(email);
        }
        log.info("Email(s) установлен(ы)");
    }
}
