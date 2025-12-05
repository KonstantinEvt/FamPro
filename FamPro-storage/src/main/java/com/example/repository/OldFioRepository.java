package com.example.repository;

import com.example.entity.OldFio;
import jakarta.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@Log4j2
public class OldFioRepository {
    private final EntityManager entityManager;
//    @Value("${spring.jpa.properties.hibernate.default_schema}")
//    private String base;

    public OldFioRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public Set<OldFio> findAllOldFiosWithFamilyMembers(Set<UUID> uuid) {
        Set<OldFio> result;
        try {
            result=new HashSet<>(entityManager.createQuery("from OldFio a left join fetch a.member where a.uuid in :uuid", OldFio.class)
                    .setParameter("uuid",uuid)
                    .getResultList());
        } catch (RuntimeException e) {
            log.info("Other names not found");
            result=new HashSet<>();
        }
        return result;
    }
    @Transactional(readOnly = true)
    public Optional<OldFio> findOldFioWithFamilyMember(UUID uuid) {
        Optional<OldFio> result;
        try {
            result=Optional.of(entityManager.createQuery("from OldFio a left join fetch a.member where a.uuid = :uuid", OldFio.class)
                    .setParameter("uuid",uuid)
                    .getSingleResult());
        } catch (RuntimeException e) {
            log.info("Other name not found");
            result=Optional.empty();
        }
        return result;
    }

}
