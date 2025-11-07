package com.example.repository;

import com.example.entity.Biometric;
import com.example.entity.Description;
import jakarta.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository

@Log4j2
public class MainDescriptionRepository {
    private final EntityManager entityManager;
    @Value("${spring.jpa.properties.hibernate.default_schema}")
    private String base;

    public MainDescriptionRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public List<Description> findDescriptionOfPerson(Long id) {
        String request = StringUtils.join("SELECT * from ", base, ".description a left join ", base, ".description_of_family_member b on a.id=b.description_id where b.member_info_id=", id, ' ');
        try {
            return entityManager.createNativeQuery(request, Description.class).getResultList();
        } catch (RuntimeException e) {
            log.warn("Description in base not found: ", e);
        }
        return null;
    }
}
