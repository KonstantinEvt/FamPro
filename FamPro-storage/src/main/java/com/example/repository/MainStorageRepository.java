package com.example.repository;

import com.example.dtos.SecurityDto;
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

import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
@Log4j2
public class MainStorageRepository {
    private EntityManager entityManager;

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
            log.warn("person in base not found");
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
                if (info != null) familyMember.get().setFamilyMemberInfo(info);
            }
        } catch (RuntimeException e) {
            log.warn("person in base not found");
            familyMember = Optional.empty();
        }
        return familyMember;
    }

    @Transactional(readOnly = true)
    public FamilyMember getFullFamilyMember(SecurityDto securityDto) {
        FamilyMember familyMember;
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<FamilyMember> query = builder.createQuery(FamilyMember.class);
            Root<FamilyMember> root = query.from(FamilyMember.class);
            root.fetch("otherNames", JoinType.LEFT);
            if (securityDto.getSecretLevelBirth() != SecretLevel.CLOSE) {
                root.fetch("birth", JoinType.LEFT);
            }
            if (securityDto.getSecretLevelBurial() != SecretLevel.CLOSE) {
                root.fetch("burial", JoinType.LEFT);
            }
            if (securityDto.isInfoExist()) {
                var info=root.fetch("familyMemberInfo", JoinType.LEFT);

                if (securityDto.getSecretLevelBiometric() != SecretLevel.CLOSE) {
                    info.fetch("biometric", JoinType.LEFT);
                }
                if (securityDto.getSecretLevelPhone() != SecretLevel.CLOSE) {
                    info.fetch("phones", JoinType.LEFT);
                }
                if (securityDto.getSecretLevelAddress() != SecretLevel.CLOSE) {
                    info.fetch("addresses", JoinType.LEFT);
                }
                if (securityDto.getSecretLevelEmail() != SecretLevel.CLOSE) {
                    info.fetch("emails", JoinType.LEFT);
                }
            }
            query.where(builder.equal(root.get("id"), securityDto.getPersonId()));
            familyMember = entityManager.createQuery(query).getSingleResult();
        } catch (RuntimeException e) {
            log.warn("Extension error");
            familyMember = null;
        }
        return familyMember;
    }

}