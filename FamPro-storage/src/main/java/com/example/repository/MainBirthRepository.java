package com.example.repository;

import com.example.entity.PlaceBirth;
import jakarta.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository

@Log4j2
public class MainBirthRepository {
    private final EntityManager entityManager;
    @Value("${spring.jpa.properties.hibernate.default_schema}")
    private String base;

    public MainBirthRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public List<PlaceBirth> findPlaceBirthOfPerson(Long id) {
        String request = StringUtils.join("SELECT * from ", base, ".births a left join ", base, ".birth_place_member b on a.id=b.birth_id where b.member_info_id=", id, ' ');
        try {
            return entityManager.createNativeQuery(request, PlaceBirth.class).getResultList();
        } catch (RuntimeException e) {
            log.warn("PlaceBirth in base not found: ", e);
        }
        return null;
    }
}
