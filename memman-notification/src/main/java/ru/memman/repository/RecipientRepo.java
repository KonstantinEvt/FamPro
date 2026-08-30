package ru.memman.repository;

import ru.memman.entity.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipientRepo extends JpaRepository<Recipient,Long>{
    Optional<Recipient> findByExternUuid(String externId);

}
