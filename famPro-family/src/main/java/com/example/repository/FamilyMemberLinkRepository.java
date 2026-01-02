package com.example.repository;

import com.example.entity.Family;
import com.example.entity.FamilyMemberLink;
import com.example.entity.ShortFamilyMember;
import com.example.enums.RoleInFamily;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
@AllArgsConstructor
@Log4j2
public class FamilyMemberLinkRepository {
    private EntityManager entityManager;

    @Transactional
    public void addFamilyMemberLink(FamilyMemberLink familyMemberLink) {
        try {
            entityManager.persist(familyMemberLink);
        } catch (RuntimeException e) {
            log.warn("family member not added:", e);
        }
    }

    @Transactional
    public void updateFamilyMemberLink(FamilyMemberLink familyMemberLink) {
        try {
            entityManager.merge(familyMemberLink);
        } catch (RuntimeException e) {
            log.warn("family member not added:", e);
        }
    }

    @Transactional
    public void refresh(FamilyMemberLink familyMemberLink) {
        try {
            entityManager.refresh(familyMemberLink);
        } catch (RuntimeException e) {
            log.warn("family member not added:", e);
        }
    }

    @Transactional
    public void removeFamilyMember(FamilyMemberLink familyMemberLink) {
        try {
            entityManager.remove(familyMemberLink);
        } catch (RuntimeException e) {
            log.warn("family member not removed:", e);
        }
    }

    @Transactional
    public void addAllFamilyMember(Set<FamilyMemberLink> familyMemberLinks) {
        try {
            entityManager.persist(familyMemberLinks);
        } catch (RuntimeException e) {
            log.warn("family member not added:", e);
        }
    }

    @Transactional(readOnly = true)
    public Set<FamilyMemberLink> getAllFamilyMembersLink(Family family) {
        Set<FamilyMemberLink> members;
        try {
            members = new HashSet<>(entityManager.createQuery("from FamilyMemberLink a left join fetch a.member left join fetch a.family b where b =: family", FamilyMemberLink.class)
                    .setParameter("family", family)
                    .getResultList());
        } catch (RuntimeException e) {
            log.info("Members of family not found");
            return new HashSet<>();
        }
        return members;
    }

    @Transactional(readOnly = true)
    public Optional<FamilyMemberLink> getFamilyMemberLink(Family family, ShortFamilyMember member, RoleInFamily roleInFamily, UUID causePerson) {
        Optional<FamilyMemberLink> familyMemberLink;
        try {
            familyMemberLink = Optional.of(entityManager.createQuery("from FamilyMemberLink a left join fetch a.member left join fetch a.family where a.member=: member and a.family=:family and a.roleInFamily=:roleInFamily and a.causePerson=:causePerson", FamilyMemberLink.class)
                    .setParameter("family", family)
                    .setParameter("member", member)
                    .setParameter("roleInFamily", roleInFamily)
                    .setParameter("causePerson", causePerson)
                    .getSingleResult());
        } catch (RuntimeException e) {
            log.info("Member of family not found");
            familyMemberLink = Optional.empty();
        }
        return familyMemberLink;
    }

    @Transactional(readOnly = true)
    public Set<FamilyMemberLink> getFamilyMemberLinksByFamilyAndCausePerson(Family family, UUID causePerson) {
        Set<FamilyMemberLink> familyMemberLink;
        try {
            familyMemberLink = new HashSet<>(entityManager.createQuery("from FamilyMemberLink a where a.family=:family and a.causePerson=:causePerson", FamilyMemberLink.class)
                    .setParameter("family", family)
                    .setParameter("causePerson", causePerson)
                    .getResultList());
        } catch (RuntimeException e) {
            log.info("Member of family not found");
            familyMemberLink = new HashSet<>();
        }
        return familyMemberLink;
    }

    @Transactional(readOnly = true)
    public Set<FamilyMemberLink> getAllFamilyMemberLinksByFamily(Family family) {
        Set<FamilyMemberLink> familyMemberLinks;
        try {
            familyMemberLinks = new HashSet<>(entityManager.createQuery("from FamilyMemberLink a  where a.family=: family", FamilyMemberLink.class)

                    .setParameter("family", family)
                    .getResultList());
        } catch (RuntimeException e) {
            log.info("Family links of this members not found");
            familyMemberLinks = new HashSet<>();
        }
        return familyMemberLinks;
    }

    @Transactional(readOnly = true)
    public Set<FamilyMemberLink> getAllFamilyMemberLinksByFamily(ShortFamilyMember member) {
        Set<FamilyMemberLink> familyMemberLinks;
        try {
            familyMemberLinks = new HashSet<>(entityManager.createQuery("from FamilyMemberLink a left join fetch a.member left join fetch a.family where a.member=: member", FamilyMemberLink.class)

                    .setParameter("member", member)
                    .getResultList());
        } catch (RuntimeException e) {
            log.info("Family links of this members not found");
            familyMemberLinks = new HashSet<>();
        }
        return familyMemberLinks;
    }

    @Transactional(readOnly = true)
    public Set<FamilyMemberLink> getAllFamilyMemberLinksByCausePerson(UUID memberUuid) {
        Set<FamilyMemberLink> familyMemberLinks;
        try {
            familyMemberLinks = new HashSet<>(entityManager.createQuery("from FamilyMemberLink a where a.causePerson=: memberUuid", FamilyMemberLink.class)
                    .setParameter("memberUuid", memberUuid)
                    .getResultList());
        } catch (RuntimeException e) {
            log.info("Family links of this members not found");
            familyMemberLinks = new HashSet<>();
        }
        return familyMemberLinks;
    }
}
