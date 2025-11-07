package com.example.repository;

import com.example.entity.Address;
import com.example.entity.Email;
import jakarta.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Repository

@Log4j2
public class MainEmailRepository {
    private final EntityManager entityManager;
    @Value("${spring.jpa.properties.hibernate.default_schema}")
    private String base;

    public MainEmailRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public Set<Email> findEmailsOfPerson(Long id) {
        String request = StringUtils.join("SELECT * from ", base, ".emails a left join ", base, ".emails_of_family_member b on a.id=b.email_id where b.member_info_id=", id, ' ');
        try {
            return new HashSet<Email>(entityManager.createNativeQuery(request, Email.class).getResultList());
        } catch (RuntimeException e) {
            log.warn("Emails in base not found: ", e);
        }
        return null;
    }
}
