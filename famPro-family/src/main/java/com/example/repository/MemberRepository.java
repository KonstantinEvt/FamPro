package com.example.repository;

import com.example.entity.Family;
import com.example.entity.ShortFamilyMember;
import com.example.entity.ShortFamilyMemberInfo;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@AllArgsConstructor
@Log4j2
public class MemberRepository {
    private EntityManager entityManager;
    @Transactional
    public void persistMember(ShortFamilyMember member) {
        try {
            entityManager.persist(member);
        } catch (RuntimeException e) {
            log.warn("familyMember not persist:", e);
        }
    }
    @Transactional
    public void updateMember(ShortFamilyMember member) {
        try {
            entityManager.merge(member);
        } catch (RuntimeException e) {
            log.warn("familyMember not update:", e);
        }
    }
    @Transactional(readOnly = true)
    public Optional  <Family> getPrimeFamily(ShortFamilyMember member) {
        Optional<Family> family;
        try {
            family = Optional.of(entityManager.createQuery("from ShortFamilyMember a left join fetch a.familyWhereChild where a=:member", ShortFamilyMember.class)
                    .setParameter("member", member)
                    .getSingleResult().getFamilyWhereChild());

        } catch (RuntimeException e) {
            log.warn("Error in finding prime family : {}", e.getMessage());
            family=Optional.empty();
        }
        return family;
    }
    @Transactional
    public void updateInfo(ShortFamilyMemberInfo memberInfo) {
        try {
            entityManager.merge(memberInfo);
        } catch (RuntimeException e) {
            log.warn("Info not update:", e);
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
    public Optional<ShortFamilyMember> findMemberWithPrimeFamily(UUID uuid) {
        Optional<ShortFamilyMember> member;
        try {
            member = Optional.of(entityManager.createQuery("from ShortFamilyMember m join fetch m.familyWhereChild  where m.uuid=:uuid", ShortFamilyMember.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult());
        } catch (RuntimeException e) {
            log.warn("Error in finding familyMember with prime family by uuid: {}", uuid.toString());
            member = Optional.empty();
        }
        return member;
    }
    @Transactional
    public Optional<ShortFamilyMember> getMemberByUuid(UUID uuid) {
        Optional<ShortFamilyMember> member;
        try {
            member = Optional.of(entityManager.createQuery("from ShortFamilyMember m where m.uuid=:uuid", ShortFamilyMember.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult());
        } catch (RuntimeException e) {
            log.warn("Error in finding familyMember by uuid: {}", uuid.toString());
            member = Optional.empty();
        }
        return member;
    }

    @Transactional
    public Optional<ShortFamilyMemberInfo> getInfoByUuid(UUID uuid) {
        Optional<ShortFamilyMemberInfo> info;
        try {
            info = Optional.of(entityManager.createQuery("from ShortFamilyMemberInfo m where m.uuid=:uuid", ShortFamilyMemberInfo.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult());
        } catch (RuntimeException e) {
            log.warn("Error in finding familyMemberInfo by uuid: {}", uuid.toString());
            info = Optional.empty();
        }
        return info;
    }
//    @Transactional(readOnly = true)
//    public Optional<ShortFamilyMember> getPersonForLinking(UUID uuid) {
//        Optional<ShortFamilyMember> member;
//        try {
//            member = Optional.of(entityManager.createQuery("from ShortFamilyMember a join fetch a.familyWhereChild b join fetch b.globalFamily join fetch a.families c left join fetch c.guard left join fetch a.linkedGuard where a.uuid=:uuid", ShortFamilyMember.class)
//                    .setParameter("uuid", uuid)
//                    .getSingleResult());
//        } catch (RuntimeException e) {
//            log.warn("Error in finding familyMember: {} to linking", uuid.toString());
//            member = Optional.empty();
//        }
//        return member;
//    }
    @Transactional(readOnly = true)
    public Set<ShortFamilyMember> getAllMembersByUuids(Set<UUID> uuids) {
        Set<ShortFamilyMember> members;
        try {
            members = new HashSet<>(entityManager.createQuery("from ShortFamilyMember a where a.uuid in: uuids", ShortFamilyMember.class)
                    .setParameter("uuids", uuids)
                    .getResultList());
        } catch (RuntimeException e) {
            log.info("Letters not found");
            members = new HashSet<>();
        }
        return members;
    }
    @Transactional(readOnly = true)
    public Set<ShortFamilyMember> findFamilyChildren(Family family) {
        Set<ShortFamilyMember> members;
        try {
            members = new HashSet<>(entityManager.createQuery("from ShortFamilyMember a where a.familyWhereChild=:family", ShortFamilyMember.class)
                    .setParameter("family", family)
                    .getResultList());

        } catch (RuntimeException e) {
            log.warn("Error in finding children of family: {}", e.getMessage());
            members = new HashSet<>();
        }
        return members;
    }
}
