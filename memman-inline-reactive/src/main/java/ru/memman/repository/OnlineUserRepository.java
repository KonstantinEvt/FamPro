package ru.memman.repository;

import ru.memman.entity.BaseUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Log4j2
public class OnlineUserRepository {
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public Optional<BaseUser> getOnlineUserByExternUuid(String externUuid) {
        Optional<BaseUser> usersOnline;
        try {
            usersOnline = Optional.of(entityManager.createQuery("from BaseUser bu where bu.externUuid=:uuid", BaseUser.class)
                    .setParameter("uuid", externUuid)
                    .getSingleResult());
        } catch (NoResultException e) {
            log.warn("user {} is not register in base yet ", externUuid);
            usersOnline = Optional.empty();
        }
        return usersOnline;
    }

    @Transactional
    public void persistNewUser(BaseUser baseUser) {
        try {
            entityManager.persist(baseUser);
        } catch (RuntimeException e) {
            log.warn("recipient: {} not persist", baseUser.getNickName());
        }
    }

    @Transactional
    public void update(BaseUser baseUser) {
        try {
            entityManager.merge(baseUser);
        } catch (RuntimeException e) {
            log.warn("recipient: {} not update", baseUser.getNickName());
        }
    }

    @Transactional
    public void updateAll(List<BaseUser> baseUsers) {
        try {
            int count = 0;
            for (BaseUser baseUser :
                    baseUsers) {
                count++;
                if (count == 100) {
                    entityManager.flush();
                    entityManager.clear();
                    count = 0;
                }
                entityManager.merge(baseUser);
            }
        } catch (RuntimeException e) {
            log.warn("online users not updated");
        }
    }

    @Transactional(readOnly = true)
    public Optional<BaseUser> getRecipientWithReceiveLettersByExternId(String externUuid) {
        Optional<BaseUser> recipient;
        try {
            recipient = Optional.of(entityManager.createQuery("from BaseUser r where r.externUuid=:externUuid", BaseUser.class)
                    .setParameter("externUuid", externUuid)
                    .getSingleResult());
        } catch (NoResultException e) {
            log.warn("recipient with externId: {} not found", externUuid);
            recipient = Optional.empty();
        }
        return recipient;
    }

}
