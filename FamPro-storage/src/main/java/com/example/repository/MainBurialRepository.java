package com.example.repository;

import com.example.entity.PlaceBurial;
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
public class MainBurialRepository {
    private final EntityManager entityManager;
    @Value("${spring.jpa.properties.hibernate.default_schema}")
    private String base;
    public MainBurialRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Transactional(readOnly = true)
    public List<PlaceBurial> findPlaceBurialOfPerson(Long id) {
        String request = StringUtils.join("SELECT * from ", base, ".burials a left join ", base, ".burial_place_member b on a.id=b.burial_id where b.member_info_id=", id, ' ');
        try {
            return entityManager.createNativeQuery(request, PlaceBurial.class).getResultList();
        } catch (RuntimeException e) {
            log.warn("PlaceBurial in base not found: ", e);
        }
        return null;
    }
}
