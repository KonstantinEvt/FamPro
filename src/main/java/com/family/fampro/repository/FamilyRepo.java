package com.family.fampro.repository;

import com.family.fampro.entity.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FamilyRepo extends JpaRepository<FamilyMember, Long> {
    Set<FamilyMember> findAllByFather_Id(Long father_id);

    Set<FamilyMember> findAllByMother_Id(Long mother_id);
}
