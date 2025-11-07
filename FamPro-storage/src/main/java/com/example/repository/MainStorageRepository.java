package com.example.repository;

import com.example.dtos.SecurityDto;
import com.example.entity.Address;
import com.example.entity.FamilyMember;
import com.example.entity.FamilyMemberInfo;
import com.example.entity.OldFio;
import com.example.enums.SecretLevel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@AllArgsConstructor
@Log4j2
public class  MainStorageRepository {
    private EntityManager entityManager;
    MainInfoReposirory mainInfoReposirory;

    @Transactional(readOnly = true)
    public Optional<FamilyMember> findMemberWithInfoById(Long id) {
        Optional<FamilyMember> familyMember;
        try {
            familyMember = Optional.of(entityManager.createQuery("from FamilyMember a left join fetch a.familyMemberInfo where a.id=:id", FamilyMember.class)
                    .setParameter("id", id)
                    .getSingleResult());
        } catch (RuntimeException e) {
            log.warn("person in base not found");
            familyMember = Optional.empty();
        }
        return familyMember;
    }

    @Transactional(readOnly = true)
    public Optional<FamilyMember> findFullFamilyMemberById(Long id) {
        Optional<FamilyMember> familyMember;
        try {
            familyMember = Optional.of(entityManager.createQuery("from FamilyMember a left join fetch a.familyMemberInfo where a.id=:id", FamilyMember.class)
                    .setParameter("id", id)
                    .getSingleResult());
        } catch (RuntimeException e) {
            log.warn("person in base not found");
            familyMember = Optional.empty();
        }
        return familyMember;
    }

    @Transactional(readOnly = true)
    public Optional<FamilyMember> findMemberWithInfoByUUID(UUID uuid) {
        Optional<FamilyMember> familyMember;
        try {
            familyMember = Optional.of(entityManager.createQuery("from FamilyMember a left join fetch a.familyMemberInfo where a.uuid=:uuid", FamilyMember.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult());
        } catch (RuntimeException e) {
            familyMember = Optional.empty();
        }
        return familyMember;
    }

    @Transactional(readOnly = true)
    public Optional<FamilyMember> findMemberWithInfoByOldNameUUID(UUID uuid) {
        Optional<FamilyMember> familyMember;
        try {
            Optional<OldFio> oldFio = Optional.of((entityManager.createQuery("from OldFio a left join fetch a.member where a.uuid=:uuid", OldFio.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult()));
            familyMember = oldFio.map(OldFio::getMember);
            if (familyMember.isPresent()) {
                FamilyMemberInfo info = entityManager.createQuery("from FamilyMemberInfo a where a.uuid=:uuid", FamilyMemberInfo.class)
                        .setParameter("uuid", familyMember.get().getUuid())
                        .getSingleResult();
                if (info != null) familyMember.get().setFamilyMemberInfo(List.of(info));
            }
        } catch (RuntimeException e) {
            log.warn("person in base not found");
            familyMember = Optional.empty();
        }
        return familyMember;
    }

    @Transactional(readOnly = true)
    public Optional<FamilyMember> getFullFamilyMember(SecurityDto securityDto) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<FamilyMember> query = builder.createQuery(FamilyMember.class);
            Root<FamilyMember> root = query.from(FamilyMember.class);
            if (securityDto.isOtherNamesExist()) root.fetch("otherNames", JoinType.LEFT);
            if (securityDto.isInfoExist()) root.fetch("familyMemberInfo", JoinType.LEFT);
            query.where(builder.equal(root.get("id"), securityDto.getPersonId()));
            Optional<FamilyMember> familyMember = Optional.of(entityManager.createQuery(query).getSingleResult());
            if (securityDto.isInfoExist()) {
                mainInfoReposirory.getFullInfo(securityDto, familyMember.get().getFamilyMemberInfo().get(0));
//                info.ifPresent(familyMemberInfo -> familyMember.get().setFamilyMemberInfo(List.of(familyMemberInfo)));
            }
            return familyMember;
        } catch (RuntimeException e) {
            log.warn("Extension error:", e);
            return Optional.empty();
        }
    }

    @Transactional
    public void persistMember(FamilyMember familyMember) {
        try {
            entityManager.persist(familyMember);
        } catch (RuntimeException e) {
            log.warn("Person not save");
        }
    }

    @Transactional
    public void updateMember(FamilyMember familyMember) {
        try {
            entityManager.merge(familyMember);
        } catch (RuntimeException e) {
            log.warn("Person not update");
        }
    }
    @Transactional
    public void flushMember() {
        try {
            entityManager.flush();
        } catch (RuntimeException e) {
            log.warn("flush is corrupt");
        }
    }

}