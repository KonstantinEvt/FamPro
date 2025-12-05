package com.example.repository;

import com.example.entity.Recipient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
@Log4j2
public class RecipientRepository {
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public Optional<Recipient> getRecipientWithReceiveLettersById(Long id) {
        Optional<Recipient> recipient;
        try {
            recipient = Optional.of(entityManager.createQuery("from Recipient r left join fetch r.receivedLetters where r.id=:id", Recipient.class)
                    .setParameter("id", id)
                    .getSingleResult());
        } catch (NoResultException e) {
            log.warn("recipient with id: {} not found", id);
            recipient = Optional.empty();
        }
        return recipient;
    }
    @Transactional
    public void persistNewRecipient(Recipient recipient) {
        try {
            entityManager.persist(recipient);
        } catch (RuntimeException e) {
            log.warn("recipient: {} not persist", recipient.getNickName());
        }
    }
    @Transactional
    public void update(Recipient recipient) {
        try {
            entityManager.merge(recipient);
        } catch (RuntimeException e) {
            log.warn("recipient: {} not update", recipient.getNickName());
        }
    }
    @Transactional(readOnly = true)
    public Optional<Recipient> getRecipientWithReceiveLettersByExternId(String externUuid) {
        Optional<Recipient> recipient;
        try {
            recipient = Optional.of(entityManager.createQuery("from Recipient r left join fetch r.receivedLetters where r.externUuid=:externUuid", Recipient.class)
                    .setParameter("externUuid", externUuid)
                    .getSingleResult());
        } catch (NoResultException e) {
            log.warn("recipient with externId: {} not found", externUuid);
            recipient = Optional.empty();
        }
        return recipient;
    }

}
