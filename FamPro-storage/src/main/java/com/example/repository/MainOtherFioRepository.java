package com.example.repository;

import com.example.entity.Email;
import com.example.entity.OldFio;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@Log4j2
public class MainOtherFioRepository {
    private final EntityManager entityManager;
    @Value("${spring.jpa.properties.hibernate.default_schema}")
    private String base;

    MainOtherFioRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public Set<OldFio> findOldNamesOfPerson(Long id) {
        String request = StringUtils.join("SELECT * from ", base, ".old_fio a where a.member_id=", id, ' ');
        try {
            return new HashSet<OldFio>(entityManager.createNativeQuery(request, OldFio.class).getResultList());
        } catch (RuntimeException e) {
            log.warn("Other names in base not found: ", e);
        }
        return null;
    }
}
