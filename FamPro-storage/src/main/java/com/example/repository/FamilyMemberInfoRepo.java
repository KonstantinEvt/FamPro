package com.example.repository;

import com.example.entity.FamilyMemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FamilyMemberInfoRepo extends JpaRepository<FamilyMemberInfo,Long> {
    Optional<FamilyMemberInfo> findFamilyMemberInfoByUuid(UUID uuid);
}
