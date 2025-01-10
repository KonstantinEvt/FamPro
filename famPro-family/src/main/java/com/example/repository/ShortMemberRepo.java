package com.example.repository;

import com.example.entity.ShortFamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortMemberRepo extends JpaRepository<ShortFamilyMember, Long> {
}
