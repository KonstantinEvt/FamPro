package com.example.repository;

import com.example.entity.Email;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@Qualifier("emailRepo")
public interface EmailRepo extends JpaRepository<Email, Long>, InternRepo<Email> {
@Override
    Set<Email> findAllByInternNameIn(Set<String> set);
}
