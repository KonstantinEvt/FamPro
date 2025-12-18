package com.example.repository;

import com.example.entity.Family;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
@Log4j2
public class FamilyRepository {
    private EntityManager entityManager;

    public void saveNewFamily(Family family) {
        try {
            entityManager.persist(family);
        } catch (RuntimeException e) {
            log.warn("family is not saved");
        }
    }
    public void removeFamily(Family family) {
        try {
            entityManager.remove(family);
        } catch (RuntimeException e) {
            log.warn("family is not removed");
        }
    }
    public void detachFamily(Family family) {
        try {
            entityManager.detach(family);
        } catch (RuntimeException e) {
            log.warn("family is detached");
        }
    }
    @Transactional
    public void updateFamily(Family family) {
        try {
            entityManager.merge(family);
        } catch (RuntimeException e) {
            log.warn("family not update:", e);
        }
    }

    @Transactional
    public void flush() {
        try {
            entityManager.flush();
        } catch (RuntimeException e) {
            log.warn("flush is fail", e);
        }
    }
    @Transactional(readOnly = true)
    public Optional<Family> findFamilyWithChildrenByUUID(UUID uuid) {
        Optional<Family> family;
        try {
            family = Optional.of(entityManager.createQuery("from Family a left join fetch a.children where a.uuid=:uuid", Family.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult());
        }catch (NoResultException e){
            log.info("family with children not found");
            family = Optional.empty();
        } catch (RuntimeException e) {
            log.warn("Error in finding family with children");
            family = Optional.empty();
        }
        return family;
    }
    public void refreshFamily(Family family) {
        try {
            entityManager.refresh(family);
        } catch (RuntimeException e) {
            log.warn("family is not refresh {}", e.getMessage());
        }
    }
    @Transactional(readOnly = true)
    public Optional<Family> findFamilyByUUID(UUID uuid) {
        Optional<Family> family;
        try {
            family = Optional.of(entityManager.createQuery("from Family a where a.uuid=:uuid", Family.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult());
        }catch (NoResultException e){
            log.info("family  not found");
            family = Optional.empty();
        } catch (RuntimeException e) {
            log.warn("Error in finding family ");
            family = Optional.empty();
        }
        return family;
    }
}
