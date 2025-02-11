package com.example.repository;

import com.example.entity.ShortFamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShortMemberRepo extends JpaRepository<ShortFamilyMember, Long> {
    Optional<ShortFamilyMember> findByUuid(UUID uuid);
}
