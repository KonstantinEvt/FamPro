package com.example.repository;

import com.example.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface EmailRepo extends JpaRepository<Email,Long> {

    Set<Email> findAllByEmailNameIn(Set<String> set);
}
