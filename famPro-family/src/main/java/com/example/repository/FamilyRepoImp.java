package com.example.repository;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class FamilyRepoImp implements FamilyRepo{
    EntityManager entityManager;

    @Override
    public void addFamily() {

    }
}
