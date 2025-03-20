package com.example.repository;

import com.example.entity.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RecipientRepo extends JpaRepository<Recipient,Long>{
    Optional<Recipient> findByExternId(String externId);

}
