package com.example.repository;
import com.example.entity.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyRepo extends JpaRepository<FamilyMember, Long> {

    List<FamilyMember> findAllByFather_Id(Long father_id);

    List<FamilyMember> findAllByMother_Id(Long mother_id);

    List<FamilyMember> findAllByLastname(String lastName);
}
