package ru.memman.repository;

import ru.memman.entity.RealMen;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@AllArgsConstructor
@Log4j2
public class RealManRepository {
    private EntityManager entityManager;

    @Transactional
    public Optional<RealMen> getRealManByExternId(String externId) {
        Optional<RealMen> realMen;
        try {
            realMen = Optional.of(entityManager.createQuery("from RealMen r where r.externUuid=:externId", RealMen.class)
                    .setParameter("externId", externId)
                    .getSingleResult());
        } catch (NoResultException e) {
            log.warn("real man with id: {} not found", externId);
            realMen = Optional.empty();
        }
        return realMen;
    }
    @Transactional
    public Optional<RealMen> getRealManWithContactsByExternId(String externId) {
        Optional<RealMen> realMen;
        try {
            realMen = Optional.of(entityManager.createQuery("from RealMen r left join fetch r.contacts where r.externUuid=:externId", RealMen.class)
                    .setParameter("externId", externId)
                    .getSingleResult());
        } catch (NoResultException e) {
            log.warn("real man with id: {} not found", externId);
            realMen = Optional.empty();
        }
        return realMen;
    }
    @Transactional
    public List<RealMen> getRealManWithContactsByExternId(List<String> externIds) {
        List<RealMen> realMen;
        try {
            realMen = entityManager.createQuery("from RealMen r left join fetch r.contacts where r.externUuid in :externId", RealMen.class)
                    .setParameter("externId", externIds)
                    .getResultList();
        } catch (NoResultException e) {
            log.warn("real man with id: {} not found", externIds);
            realMen = new ArrayList<>();
        }
        return realMen;
    }
    @Transactional
    public void persistNewRealMan(RealMen realMen) {
        try {
            entityManager.persist(realMen);
        } catch (RuntimeException e) {
            log.warn("realMen: {} not persist", realMen.getNickName());
        }
    }
    @Transactional
    public void update(RealMen realMen) {
        try {
            entityManager.merge(realMen);
        } catch (RuntimeException e) {
            log.warn("realMen: {} not update", realMen.getNickName());
        }
    }
    @Transactional
    public List<RealMen> getPersonsByUuids(Set<String> externUuids) {
        List<RealMen> realMens;
        try {
            realMens = entityManager.createQuery("from RealMen r where r.externUuid in :externUuids", RealMen.class)
                    .setParameter("externUuids", externUuids)
                    .getResultList();
        } catch (RuntimeException e) {
            log.warn("something is wrong", e);
            return new ArrayList<>();
        }
        return realMens;
    }
    @Transactional
    public void detach(RealMen realMen) {
        try {
            entityManager.detach(realMen);
        } catch (RuntimeException e) {
            log.warn("realMen: {} not detached", realMen.getNickName());
        }
    }
}
