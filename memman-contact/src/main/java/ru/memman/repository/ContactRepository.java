package ru.memman.repository;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.memman.entity.Contact;
import ru.memman.entity.RealMen;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
@AllArgsConstructor
@Log4j2
public class ContactRepository {
    private EntityManager entityManager;

    @Transactional
    public List<Contact> getContactsWithLocalisations(Set<Contact> contactSet) {
        List<Contact> contacts;
        try {
            contacts = entityManager.createQuery("from Contact c left join fetch c.locals where c in :contacts", Contact.class)
                    .setParameter("contacts", contactSet)
                    .getResultList();
        } catch (RuntimeException e) {
            log.warn("something is wrong", e);
            return new ArrayList<>();
        }
        return contacts;

    }
    @Transactional
    public void detach(Contact contact) {
        try {
            entityManager.detach(contact);
        } catch (RuntimeException e) {
            log.warn("contact: {} not detached", contact.getPersonUuid());
        }
    }
    @Transactional
    public void updateAll(List<Contact> contacts) {
        try {
            int count = 0;
            for (Contact contact :
                    contacts) {
                count++;
                if (count == 100) {
                    entityManager.flush();
                    entityManager.clear();
                    count = 0;
                }
                entityManager.merge(contact);
            }
        } catch (RuntimeException e) {
            log.warn("pack of contacts not updated");
        }
    }

    @Transactional
    public void persistAll(List<Contact> contacts) {
        try {
            int count = 0;
            for (Contact contact :
                    contacts) {
                count++;
                if (count == 100) {
                    entityManager.flush();
                    entityManager.clear();
                    count = 0;
                }
                entityManager.persist(contact);
            }
        } catch (RuntimeException e) {
            log.warn("pack of contacts not persisted");
        }
    }

    @Transactional
    public void persist(Contact contact) {
        try {
            entityManager.persist(contact);
        } catch (RuntimeException e) {
            log.warn("contact not persisted");
        }
    }
}
