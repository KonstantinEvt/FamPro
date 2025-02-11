package com.example.repository;


import com.example.entity.ShortFamilyMemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FamilyMemberInfoRepo extends JpaRepository<ShortFamilyMemberInfo,Long> {
//    Optional<ShortFamilyMemberInfo> findFamilyMemberInfoByUuid(UUID uuid);
}
