package com.example.repository;

import com.example.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepo extends JpaRepository<Email,Long> {
    Email findEmailByEmailName(String email);
}
