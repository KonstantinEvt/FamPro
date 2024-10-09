package com.example.repository;

import com.example.entity.FamilyMember;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
@Qualifier("familyMemberRepo")
public interface FamilyMemberRepo extends  JpaRepository<FamilyMember,Long> {

    List<FamilyMember> findAllByFather_Id(Long father_id);

    List<FamilyMember> findAllByMother_Id(Long mother_id);

    public Optional<FamilyMember> findFioByUuid(UUID uuid);



}
