package com.example.repository;

import com.example.entity.Biometric;
import jakarta.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository

@Log4j2
public class MainBiometricRepository {
    private final EntityManager entityManager;
    @Value("${spring.jpa.properties.hibernate.default_schema}")
    private String base;

    public MainBiometricRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public List<Biometric> findBiometricOfPerson(Long id) {
        String request = StringUtils.join("SELECT * from ", base, ".biometric a left join ", base, ".biometric_of_family_member b on a.id=b.biometric_id where b.member_info_id=", id, ' ');
        try {
            return entityManager.createNativeQuery(request, Biometric.class).getResultList();
        } catch (RuntimeException e) {
            log.warn("Biometric in base not found: ", e);
        }
        return null;
    }
}
