package com.example.service;

import com.example.entity.Email;
import com.example.mappers.EmailMapper;
import com.example.repository.EmailRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService {
    private final EmailMapper emailMapper;
    private final EmailRepo emailRepo;

    void addEmail(Email email) {
        emailRepo.save(email);
    }

    Optional<Email> getEmailById(Long id) {
        return emailRepo.findById(id);
    }

    Email getEmailbyEmailName(String emailName) {
        return emailRepo.findEmailByEmailName(emailName);
    }

    void removeEmail(Long id) {
        emailRepo.deleteById(id);
    }
}
