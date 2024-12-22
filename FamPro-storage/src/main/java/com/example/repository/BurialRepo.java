package com.example.repository;

import com.example.entity.Address;
import com.example.entity.PlaceBurial;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Qualifier("burialRepo")
@Repository
public interface BurialRepo extends JpaRepository<PlaceBurial,Long>,InternRepo<PlaceBurial> {}