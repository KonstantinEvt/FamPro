package com.example.repository;

import com.example.entity.DeferredDirective;
import com.example.entity.DirectiveMember;
import com.example.entity.ShortFamilyMember;
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
public class DirectiveRepository {
    private EntityManager entityManager;

    public void saveAllDirectiveMembers(List<DirectiveMember> directiveMembers) {
        try {
            for (DirectiveMember dm :
                    directiveMembers) {
                entityManager.persist(dm);
            }
        } catch (RuntimeException e) {
            log.warn("deferred directive is not saved");
        }
    }

    public List<DirectiveMember> checkForExistDirectiveMember(ShortFamilyMember member) {
        List<DirectiveMember> result;
        try {
            result = entityManager.createQuery("from DirectiveMember a left join fetch a.directiveMember where a.directiveMember=:member", DirectiveMember.class)
                    .setParameter("member", member)
                    .getResultList();
        } catch (RuntimeException e) {
            result = new ArrayList<>();
            log.warn("not found");
        }
        log.info("result of Moderation {}",result);
        return result;
    }
    public Set<DirectiveMember> getListMembersOfDirective(DeferredDirective directive) {
        Set<DirectiveMember> result;
        try {
            result = new HashSet<>(entityManager.createQuery("from DirectiveMember a left join fetch a.directiveMember left join fetch a.directive where a.directive=:directive", DirectiveMember.class)
                    .setParameter("directive", directive)
                    .getResultList());
        } catch (RuntimeException e) {
            result=new HashSet<>();
            log.warn("not found");
        }
        return result;
    }
    @Transactional(readOnly = true)
    public Optional<DeferredDirective> findDirectiveWithPrimeMember(UUID uuid) {
        Optional<DeferredDirective> deferredDirective;
        try {
            deferredDirective = Optional.of(entityManager.createQuery("from DeferredDirective a left join fetch a.directiveMember where a.uuid=:uuid", DeferredDirective.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult());
        } catch (NoResultException e) {
            log.info("deferred directive not found");
            deferredDirective = Optional.empty();
        } catch (RuntimeException e) {
            log.warn("Error in finding family with children");
            deferredDirective = Optional.empty();
//            "Error in finding family with children: {}",
        }
        return deferredDirective;
    }
    @Transactional(readOnly = true)
    public Optional<DeferredDirective> getLinkingDirective(UUID uuid) {
        Optional<DeferredDirective> directive;
        try {
            directive = Optional.of(entityManager.createQuery("from DeferredDirective a join fetch a.directiveMember where a.id=:uuid", DeferredDirective.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult());
        } catch (RuntimeException e) {
            log.warn("person not fond");
            directive = Optional.empty();
        }
        return directive;
    }
}
