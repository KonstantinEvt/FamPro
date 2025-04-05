package com.example.repository;

import com.example.entity.Family;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
@Log4j2
public class FamilyRepository {
    private EntityManager entityManager;

    public Family getFamilyWithAllGuard(Family family) {
        Family family1;
        try {
            family1 = entityManager.createQuery("select a from Family a left join fetch a.guard join fetch a.globalFamily b left join fetch b.guard where a= :family", Family.class)
                    .setParameter("family", family)
                    .getSingleResult();
        } catch (RuntimeException e) {
            log.warn("family is absent");
            family1 = null;
        }
        return family1;
    }
public void saveFamily(Family family){
        try{entityManager.persist(family);
        }
        catch (RuntimeException e){
            log.warn("family is not saved");
        }
}
}
