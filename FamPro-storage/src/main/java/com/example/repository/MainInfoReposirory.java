package com.example.repository;

import com.example.dtos.FamilyMemberInfoDto;
import com.example.dtos.SecurityDto;
import com.example.entity.*;
import com.example.enums.SecretLevel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@Log4j2
@AllArgsConstructor
public class MainInfoReposirory {
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public Optional<FamilyMemberInfo> getFullFamilyMemberInfo(FamilyMemberInfoDto memberInfoDto, Long id) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<FamilyMemberInfo> query = builder.createQuery(FamilyMemberInfo.class);
            Root<FamilyMemberInfo> root = query.from(FamilyMemberInfo.class);
            if (memberInfoDto.getBiometric() != null
                    && memberInfoDto.getSecretLevelBiometric() != SecretLevel.CLOSE)
                root.fetch("biometricData", JoinType.LEFT);
            if (memberInfoDto.getDescription() != null
                    && memberInfoDto.getSecretLevelDescription() != SecretLevel.CLOSE)
                root.fetch("biometricData", JoinType.LEFT);
            if (memberInfoDto.getPhones() != null
                    && memberInfoDto.getSecretLevelPhone() != SecretLevel.CLOSE)
                root.fetch("phonesSet", JoinType.LEFT);
            if (memberInfoDto.getEmails() != null
                    && memberInfoDto.getSecretLevelEmail() != SecretLevel.CLOSE)
                root.fetch("emailsSet", JoinType.LEFT);
            if (memberInfoDto.getAddresses() != null
                    && memberInfoDto.getSecretLevelAddress() != SecretLevel.CLOSE)
                root.fetch("addressesSet", JoinType.LEFT);
            if (memberInfoDto.getBirth() != null
                    && memberInfoDto.getSecretLevelBirth() != SecretLevel.CLOSE)
                root.fetch("birthPlace", JoinType.LEFT);
            if (memberInfoDto.getBurial() != null
                    && memberInfoDto.getSecretLevelBurial() != SecretLevel.CLOSE)
                root.fetch("burialPlace", JoinType.LEFT);
            query.where(builder.equal(root.get("id"), id));
            return Optional.of(entityManager.createQuery(query).getSingleResult());
        } catch (RuntimeException e) {
            log.warn("select from base is corrupt");
            return Optional.empty();
        }
    }

    @Transactional(readOnly = true)
    public void getFullInfo(SecurityDto securityDto, FamilyMemberInfo info) {
        try {
//            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//            CriteriaQuery<FamilyMemberInfo> query = builder.createQuery(FamilyMemberInfo.class);
//            Root<FamilyMemberInfo> info = query.from(FamilyMemberInfo.class);
            log.info("инфо biometricData");
            if (securityDto.getSecretLevelBiometric() != SecretLevel.CLOSE) {
                info.getBiometricData();
            }
            log.info("инфо phonesSet");
            if (securityDto.getSecretLevelDescription() != SecretLevel.CLOSE) {
                info.getDescriptionData();
            }
            if (securityDto.getSecretLevelPhone() != SecretLevel.CLOSE) {
                info.getPhonesSet();
            }
            log.info("инфо addressesSet");
            if (securityDto.getSecretLevelAddress() != SecretLevel.CLOSE) {
                info.getAddressesSet();
            }
            log.info("инфо emailsSet");
            if (securityDto.getSecretLevelEmail() != SecretLevel.CLOSE) {
                info.getEmailsSet();
            }
            log.info("инфо birthPlace");
            if (securityDto.getSecretLevelBirth() != SecretLevel.CLOSE) {
                info.getBirthPlace();
            }
            log.info("инфо burialPlace");
            if (securityDto.getSecretLevelBurial() != SecretLevel.CLOSE) {
                info.getBurialPlace();
            }
//            query.where(builder.equal(info.get("uuid"), uuid));
//            return Optional.of(entityManager.createQuery(query).getSingleResult());
        } catch (RuntimeException e) {
            log.warn("Extension error:", e);
//            return Optional.empty();
        }
    }

    public void persistInfo(FamilyMemberInfo familyMemberInfo) {
        try {
            entityManager.persist(familyMemberInfo);
        } catch (RuntimeException e) {
            log.warn("Info not save");
        }
    }

    @Transactional(readOnly = true)
    public Set<Address> findAddressesOfPerson(FamilyMemberInfo familyMemberInfo) {
        try {
            log.info("id: {}", familyMemberInfo);
            return new HashSet<>(entityManager.createQuery("from Address a where a.familyMemberInfo=:fm", Address.class)
                    .setParameter("fm", familyMemberInfo)
                    .getResultList());
        } catch (RuntimeException e) {
            log.warn("Address in base not found: ", e);
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Set<Phone> findPhonesOfPerson(Long id) {
        try {
            return new HashSet<>(entityManager.createQuery("from Phone a where a.familyMemberInfo=:id", Phone.class)
                    .setParameter("id", id)
                    .getResultList());
        } catch (RuntimeException e) {
            log.warn("Phones in base not found");
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Set<Email> findEmailsOfPerson(Long id) {
        try {
            return new HashSet<>(entityManager.createQuery("from Email a where a.familyMemberInfo=:id", Email.class)
                    .setParameter("id", id)
                    .getResultList());
        } catch (RuntimeException e) {
            log.warn("Emails in base not found");
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<PlaceBirth> findPlaceBirthOfPerson(Long id) {
        try {
            return entityManager.createQuery("from PlaceBirth a where a.familyMemberInfo=:id", PlaceBirth.class)
                    .setParameter("id", id)
                    .getResultList();
        } catch (RuntimeException e) {
            log.warn("PlaceBirth in base not found");
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<PlaceBurial> findPlaceBurialOfPerson(Long id) {
        try {
            return entityManager.createQuery("from PlaceBurial a where a.familyMemberInfo=:id", PlaceBurial.class)
                    .setParameter("id", id)
                    .getResultList();
        } catch (RuntimeException e) {
            log.warn("PlaceBurial in base not found");
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<Biometric> findBiometricOfPerson(Long id) {
        try {
            return entityManager.createQuery("from Biometric a where a.familyMemberInfo=:id", Biometric.class)
                    .setParameter("id", id)
                    .getResultList();
        } catch (RuntimeException e) {
            log.warn("Biometric in base not found");
        }
        return null;
    }
}
