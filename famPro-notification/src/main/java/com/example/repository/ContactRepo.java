package com.example.repository;

import com.example.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ContactRepo  extends JpaRepository<Contact, Long> {
    Set<Contact> findAllByExternId(String externId);
}
