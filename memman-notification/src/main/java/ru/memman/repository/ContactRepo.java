package ru.memman.repository;

import ru.memman.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ContactRepo  extends JpaRepository<Contact, Long> {
    Set<Contact> findAllByExternId(String externId);
}
