package com.example.repository;

import com.example.entity.DeferredDirective;
import com.example.entity.DirectiveMembers;
import com.example.entity.Family;
import com.example.entity.ShortFamilyMember;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
@Log4j2
public class DirectiveRepository {
    private EntityManager entityManager;

    public void saveAllDirectiveMembers(List<DirectiveMembers> directiveMembers) {
        try {
            for (DirectiveMembers dm :
                    directiveMembers) {
                entityManager.persist(dm);
            }
        } catch (RuntimeException e) {
            log.warn("deferred directive is not saved");
        }
    }

    public int checkForExistDirectiveMember(ShortFamilyMember member) {
        int result;
        try {
            result = entityManager.createQuery("from DirectiveMembers a left join fetch a.directiveMember where a.directiveMember=:member", DirectiveMembers.class)
                    .setParameter("member", member)
                    .getFirstResult();
//            инта хватит?
        } catch (RuntimeException e) {
            result = 0;
            log.warn("not found");
        }
        return result;
    }
    public Set<DirectiveMembers> getListMembersOfDirective(DeferredDirective directive) {
        Set<DirectiveMembers> result;
        try {
            result = new HashSet<>(entityManager.createQuery("from DirectiveMembers a left join fetch a.directiveMember left join fetch a.directive where a.directive=:directive", DirectiveMembers.class)
                    .setParameter("directive", directive)
                    .getResultList());
//            инта хватит?
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
