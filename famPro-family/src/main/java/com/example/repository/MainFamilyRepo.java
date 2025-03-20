package com.example.repository;

import com.example.entity.DeferredDirective;
import com.example.entity.Family;
import com.example.entity.Guard;
import com.example.entity.ShortFamilyMember;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
@AllArgsConstructor
@Log4j2
public class MainFamilyRepo {
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public DeferredDirective findDirectiveWithAllLinks(UUID uuid) {
        DeferredDirective deferredDirective;
        String request = "from DeferredDirective a join fetch a.directiveMember g join fetch a.shortFamilyMemberLink f left join fetch g.linkedGuard left join fetch f.linkedGuard left join fetch a.directiveFamily b  left join fetch b.globalFamily left join fetch a.processFamily c left join fetch c.globalFamily where a.id= :externId ";
        try {
            deferredDirective = entityManager.createQuery(request, DeferredDirective.class)
                    .setParameter("externId", uuid)
                    .getSingleResult();
        } catch (RuntimeException e) {
            log.warn("deferredDirective not found or corrupt");
            deferredDirective = null;
        }
        return deferredDirective;
    }

    @Transactional(readOnly = true)
    public Family findFamilyWithGlobal(ShortFamilyMember member) {
        Family family;
        try {
            family = entityManager.createQuery("from Family a join fetch a.children b left join fetch a.globalFamily where b=:member", Family.class)
                    .setParameter("member", member)
                    .getSingleResult();

        } catch (RuntimeException e) {
            log.warn("family not fond");
            family = null;
        }
        return family;
    }

    @Transactional(readOnly = true)
    public Family findFamilyWithAllGuards(ShortFamilyMember member) {
        Family family;
        try {
            family = entityManager.createQuery("from Family a join fetch a.children b left join fetch a.globalFamily c left join fetch a.guard left join fetch c.guard where b=:member", Family.class)
                    .setParameter("member", member)
                    .getSingleResult();

        } catch (RuntimeException e) {
            log.warn("family not fond");
            family = null;
        }
        return family;
    }

    @Transactional(readOnly = true)
    public Family findFamilyWithAllGuardsByExternId(String externId) {
        Family family;
        try {
            family = entityManager.createQuery("from Family a join fetch a.children b left join fetch a.globalFamily c left join fetch a.guard left join fetch c.guard where a.externID=:externId", Family.class)
                    .setParameter("externId", externId)
                    .getSingleResult();

        } catch (RuntimeException e) {
            log.warn(e);
            family = null;
        }
        return family;
    }

    @Transactional
    public void persistNewPerson(ShortFamilyMember member) {
        try {
            entityManager.persist(member);
        } catch (RuntimeException e) {
            log.warn("familyMember not persist:", e);
        }
    }

    @Transactional
    public void persistNewFamily(Family family) {
        try {
            entityManager.persist(family);
        } catch (RuntimeException e) {
            log.warn("family not persist:", e);
        }
    }

    @Transactional(readOnly = true)
    public ShortFamilyMember findMemberWithFamilyWithAllGuardsByUuid(UUID uuid) {
        ShortFamilyMember member;
        try {
            member = entityManager.createQuery("from ShortFamilyMember m join fetch m.familyWhereChild a left join fetch a.globalFamily c left join fetch a.guard left join fetch c.guard where m.uuid=:uuid", ShortFamilyMember.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult();

        } catch (RuntimeException e) {
            log.warn("Member not found");
            member = null;
        }
        return member;
    }

    @Transactional(readOnly = true)
    public Set<Guard> getFamilyGuardWithLinkedPerson(Family family) {
        Set<Guard> guards;
        try {
            Family family1 = entityManager.createQuery("from Family a left join fetch a.guard b left join fetch b.linkedPerson where a=:family", Family.class)
                    .setParameter("family", family)
                    .getSingleResult();
            guards = family1.getGuard();
        } catch (RuntimeException e) {
            log.warn("family not fond");
            guards = null;
        }
        return guards;
    }

    @Transactional(readOnly = true)
    public ShortFamilyMember getPersonForLinking(UUID uuid) {
        ShortFamilyMember member;
        try {
            member = entityManager.createQuery("from ShortFamilyMember a join fetch a.familyWhereChild b join fetch b.globalFamily join fetch a.families c left join fetch c.guard left join fetch a.linkedGuard where a.uuid=:uuid", ShortFamilyMember.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult();
        } catch (RuntimeException e) {
            log.warn("person not fond");
            member = null;
        }
        return member;
    }
    @Transactional(readOnly = true)
    public Optional<DeferredDirective> getLinkingDirective(UUID uuid) {
        Optional<DeferredDirective> directive;
        try {
            directive = Optional.of(entityManager.createQuery("from DeferredDirective a join fetch a.directiveMember b left join fetch b.linkedGuard where a.id=:uuid", DeferredDirective.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult());
        } catch (RuntimeException e) {
            log.warn("person not fond");
            directive = Optional.empty();
        }
        return directive;
    }
}
