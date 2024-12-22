package com.example.repository;

import com.example.entity.Address;
import com.example.entity.PlaceBirth;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Qualifier("birthRepo")
@Repository
public interface BirthRepo extends JpaRepository<PlaceBirth,Long>,InternRepo<PlaceBirth> {}