package com.example.repository;

import com.example.entity.Guard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuardRepo extends JpaRepository<Guard, Long> {
    Optional<Guard> findByTokenUser(String uuid);
}
