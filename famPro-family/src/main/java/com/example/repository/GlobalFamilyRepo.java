package com.example.repository;

import com.example.entity.GlobalFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalFamilyRepo extends JpaRepository<GlobalFamily, Long> {

    }
