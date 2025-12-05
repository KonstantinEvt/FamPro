package com.example.repository;

import com.example.entity.LosingParent;
import jakarta.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
@Log4j2
public class LosingParentsRepository {
    private final EntityManager entityManager;
//    @Value("${spring.jpa.properties.hibernate.default_schema}")
//    private String base;

    public LosingParentsRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public List<LosingParent> findLosingParentsWithFamilyMember(Set<UUID> uuid) {
        List<LosingParent> result;
        try {
            result=entityManager.createQuery("from LosingParent a left join fetch a.member where a.uuid in :uuid", LosingParent.class)
                    .setParameter("uuid",uuid)
                    .getResultList();
        } catch (RuntimeException e) {
            log.info("Other names not found");
            result=new ArrayList<>();
        }
        return result;
    }
    @Transactional
    public void removeAllByUuids(Set<UUID> uuids){
     try{
         entityManager.createQuery("DELETE from LosingParent a where a.uuid in :uuids")
                 .setParameter("uuids",uuids)
                 .executeUpdate();
     } catch (RuntimeException e) {
         log.warn("Delete not happened");
     }
    }
}
