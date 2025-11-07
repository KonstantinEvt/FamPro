package com.example.repository;

import com.example.entity.Email;
import com.example.entity.Phone;
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
public class MainPhoneRepository {
    private final EntityManager entityManager;
    @Value("${spring.jpa.properties.hibernate.default_schema}")
    private String base;

    public MainPhoneRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public Set<Phone> findPhonesOfPerson(Long id) {
        String request = StringUtils.join("SELECT * from ", base, ".phones a left join ", base, ".phones_of_family_member b on a.id=b.phone_id where b.member_info_id=", id, ' ');
        try {
            return new HashSet<Phone>(entityManager.createNativeQuery(request, Phone.class).getResultList());
        } catch (RuntimeException e) {
            log.warn("Phones in base not found: ", e);
        }
        return null;
    }
}
