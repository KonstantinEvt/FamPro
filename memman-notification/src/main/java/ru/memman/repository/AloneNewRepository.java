package ru.memman.repository;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.memman.entity.AloneNew;

import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
@Log4j2
public class AloneNewRepository {
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<AloneNew> getLettersWithLetterLocals(List<AloneNew> letters) {
        List<AloneNew> aloneNewList;
        try {
            aloneNewList = entityManager.createQuery("from AloneNew a left join fetch a.letterLocalisations where a in: letters", AloneNew.class)
                    .setParameter("letters", letters)
                    .getResultList();
        } catch (RuntimeException e) {
            log.info("Letters not found");
            aloneNewList = new ArrayList<>();
        }
        return aloneNewList;
    }

    @Transactional
    public AloneNew saveNewLetter(AloneNew aloneNew) {
        try {
            entityManager.persist(aloneNew);
        } catch (RuntimeException e) {
            log.info("Letter not persist");
        }
        return aloneNew;
    }
}
