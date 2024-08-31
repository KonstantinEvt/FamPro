package com.example.service;

import com.example.entity.Email;
import com.example.mappers.EmailMapper;
import com.example.repository.EmailRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService {
    private final EmailMapper emailMapper;
    private final EmailRepo emailRepo;

    public Email mergeEmail(Email newEmail, Email oldEmail) {
        if (newEmail.getAssignment() != null) oldEmail.setAssignment(newEmail.getAssignment());
        if (newEmail.getStatus() != null) oldEmail.setStatus(newEmail.getStatus());
        if (newEmail.getDescription() != null) oldEmail.setDescription(newEmail.getDescription());
        return oldEmail;
    }

    public Email getEmailbyEmailName(String emailName) {
        return emailRepo.findEmailByEmailName(emailName);
    }

    public Email checkEmail(Email email) {
        return email;
    }
}
