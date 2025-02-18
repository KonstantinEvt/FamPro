package com.example.repository;

import com.example.entity.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipientRepo extends JpaRepository<Recipient,Long>{
    Recipient findByExternId(String externId);
}
