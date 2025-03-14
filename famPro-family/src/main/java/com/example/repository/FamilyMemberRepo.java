package com.example.repository;

import com.example.entity.ShortFamilyMember;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Qualifier("familyMemberRepo")
public interface FamilyMemberRepo extends  JpaRepository<ShortFamilyMember,Long> {

//    List<ShortFamilyMember> findAllByFather_Id(Long father_id);
//
//    List<ShortFamilyMember> findAllByMother_Id(Long mother_id);
//
//    public Optional<ShortFamilyMember> findFioByUuid(UUID uuid);



}
