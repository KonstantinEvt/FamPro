package com.example.repository;

import com.example.entity.FamilyMemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyMemberInfoRepo extends JpaRepository<FamilyMemberInfo,Long> {
}
