package com.example.repository;

import com.example.entity.Family;
import com.example.entity.ShortFamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface FamilyRepo extends JpaRepository<Family, Long> {
    Optional<Family> findFirstByExternID(String externId);

    Set<Family> findAllByHusband(ShortFamilyMember husband);
    Set<Family> findAllByWife(ShortFamilyMember wife);
}
