package com.example.repository;

import com.example.entity.Family;
import com.example.entity.Guard;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
@Log4j2
public class GuardRepository {
private EntityManager entityManager;
    @Transactional(readOnly = true)
    public Optional<Guard> findGuard(String uuid){
        Optional<Guard> guard;
        try {
            guard = Optional.of(entityManager.createQuery("from Guard a where a.tokenUser=:uuid", Guard.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult());

        } catch (RuntimeException e) {
            log.warn("Error in finding guard: {}", e.getMessage());
            guard = Optional.empty();
        }
        return guard;
    }
@Transactional(readOnly = true)
    public Optional<Guard> findGuardWithLinkingPerson(String uuid){
    Optional<Guard> guard;
    try {
        guard = Optional.of(entityManager.createQuery("from Guard a left join fetch a.linkedPerson where a.tokenUser=:uuid", Guard.class)
                .setParameter("uuid", uuid)
                .getSingleResult());

    } catch (RuntimeException e) {
        log.warn("Error in finding guard with linkPerson: {}", e.getMessage());
        guard = Optional.empty();
    }
    return guard;
}
@Transactional
    public Guard saveNewGuard(Guard guard) {
        try {
            entityManager.persist(guard);
        } catch (RuntimeException e) {
            log.warn("guard is not saved");
        }
        return guard;
    }
    @Transactional
    public Guard updateGuard(Guard guard) {
        try {
            entityManager.merge(guard);
        } catch (RuntimeException e) {
            log.warn("guard is not update");
        }
        return guard;
    }
}
