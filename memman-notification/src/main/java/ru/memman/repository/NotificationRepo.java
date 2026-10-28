package ru.memman.repository;

import ru.memman.entity.AloneNew;
import ru.memman.entity.Recipient;
import ru.memman.entity.Voting;
import ru.memman.enums.NewsCategory;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@AllArgsConstructor
@Log4j2
public class NotificationRepo {
    EntityManager entityManager;

    @Transactional(readOnly = true)
    public Recipient findRecipientWithReceivedLetter(String externId) {
        Recipient recipient;
        try {
            recipient = entityManager.createQuery("select a from Recipient a left join fetch a.receivedLetters b where a.externUuid=: user", Recipient.class)
                    .setParameter("user", externId)
                    .getSingleResult();
        } catch (RuntimeException e) {
            log.info("Recipient not found");
            recipient = null;
        }
        return recipient;
    }

    @Transactional(readOnly = true)
    public List<AloneNew> getNewLettersWithSenders(List<AloneNew> aloneNewListEntering) {
        List<AloneNew> aloneNewList;
        try {
            aloneNewList = entityManager.createQuery("select a from AloneNew a left join fetch a.sendFrom where a in: letters and a.alreadyRead=false", AloneNew.class)
                    .setParameter("letters", aloneNewListEntering)
                    .getResultList();
        } catch (RuntimeException e) {
            log.info("Letters not found");
            aloneNewList = new ArrayList<>();
        }
        return aloneNewList;
    }

//    @Transactional(readOnly = true)
//    public List<AloneNew> getLettersWithSenders(List<AloneNew> aloneNewListEntering) {
//        List<AloneNew> aloneNewList;
//        try {
//            aloneNewList = entityManager.createQuery("select a from AloneNew a left join fetch a.sendFrom where a in: letters", AloneNew.class)
//                    .setParameter("letters", aloneNewListEntering)
//                    .getResultList();
//        } catch (RuntimeException e) {
//            log.info("Letters not found");
//            aloneNewList = new ArrayList<>();
//        }
//        return aloneNewList;
//    }

    @Transactional(readOnly = true)
    public List<AloneNew> getLettersWithSendersByCategory(List<AloneNew> aloneNewListEntering, NewsCategory category) {
        List<AloneNew> aloneNewList;
        try {
            aloneNewList = entityManager.createQuery("from AloneNew a left join fetch a.sendFrom where a in :letters and a.category= :category", AloneNew.class)
                    .setParameter("letters", aloneNewListEntering)
                    .setParameter("category", category)
                    .getResultList();
        } catch (RuntimeException e) {
            log.info("Letters not found");
            aloneNewList = new ArrayList<>();
        }
        return aloneNewList;
    }

    @Transactional(readOnly = true)
    public List<AloneNew> getLettersWithSenders(List<AloneNew> aloneNewListEntering) {
        List<AloneNew> aloneNewList;
        try {
            aloneNewList = entityManager.createQuery("from AloneNew a left join fetch a.sendFrom where a in :letters", AloneNew.class)
                    .setParameter("letters", aloneNewListEntering)
                    .getResultList();
        } catch (RuntimeException e) {
            log.info("Letters not found");
            aloneNewList = new ArrayList<>();
        }
        return aloneNewList;
    }
    @Transactional(readOnly = true)
    public List<AloneNew> getFullLettersWithSenders(List<AloneNew> aloneNewListEntering) {
        List<AloneNew> aloneNewList;
        try {
            aloneNewList = entityManager.createQuery("from AloneNew a left join fetch a.sendFrom left join fetch a.letterLocalisations where a in :letters", AloneNew.class)
                    .setParameter("letters", aloneNewListEntering)
                    .getResultList();
        } catch (RuntimeException e) {
            log.info("Letters not found");
            aloneNewList = new ArrayList<>();
        }
        return aloneNewList;
    }
    @Transactional(readOnly = true)
    public Optional<Voting> findVotingWithLetter(String externId) {
        Optional<Voting> voting;
        try {
            voting = Optional.of(entityManager.createQuery("from Voting v left join fetch v.letter where v.letterId in :externId", Voting.class)
                    .setParameter("letterId", externId)
                    .getSingleResult());
        } catch (RuntimeException e) {
            log.info("voting not found");
            voting = Optional.empty();
        }
        return voting;
    }
//    @Transactional(readOnly = true)
//    public Recipient findRecipientWithContacts(String externId) {
//        Recipient owner;
//        try {
//            owner = entityManager.createQuery("select a from Recipient a left join fetch a.contacts where a.externUuid= :externId", Recipient.class)
//                    .setParameter("externId", externId)
//                    .getSingleResult();
//        } catch (RuntimeException e) {
//            log.warn("owner not found");
//            owner = null;
//        }
//        return owner;
//    }

//    @Transactional(readOnly = true)
//    public Recipient findRecipientWithContactsByLinkExternId(String externId) {
//        Recipient owner;
//        try {
//            owner = entityManager.createQuery("select a from Recipient a left join fetch a.contacts where a.linkExternId= :externId", Recipient.class)
//                    .setParameter("externId", externId)
//                    .getSingleResult();
//        } catch (RuntimeException e) {
//            log.warn("owner not found");
//            owner = null;
//        }
//        return owner;
//    }


    @Transactional(readOnly = true)
    public Set<Recipient> findGuards(Set<String> guardExternId) {
        Set<Recipient> recipientSet;
        try {
            recipientSet = new HashSet<>(entityManager.createQuery("FROM Recipient a where a.externUuid in :guards", Recipient.class)
                    .setParameter("guards", guardExternId)
                    .getResultList());
        } catch (RuntimeException e) {
            log.info("Guards not found");
            recipientSet = new HashSet<>();
        }
        return recipientSet;
    }

    @Transactional(readOnly = true)
    public Voting findVoting(String externId) {
        Voting voting;
        try {
            voting = entityManager.createQuery("FROM Voting a where a.letter= :externId", Voting.class)
                    .setParameter("externId", externId)
                    .getSingleResult();
        } catch (RuntimeException e) {
            log.info("Voting is not exist");
            return null;
        }
        return voting;
    }

    @Transactional
    public List<AloneNew> getAllAloneNewWithSendTo(String externId) {
        List<AloneNew> aloneNews;
        try {
            aloneNews = entityManager.createQuery("from AloneNew a left join fetch a.sendTo where a.externId= :externId", AloneNew.class)
                    .setParameter("externId", externId).getResultList();
        } catch (RuntimeException e) {
            log.info("Voting-letter link not found");
            return null;
        }
        return aloneNews;
    }


}
