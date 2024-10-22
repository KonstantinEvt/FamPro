package com.example.repository;

import com.example.entity.FamilyMember;
import com.example.entity.LosingParent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
@Qualifier("losingParentsRepo")
public interface LosingParentsRepo extends JpaRepository<LosingParent,Long> {
    Optional<LosingParent> findFioByUuid(UUID uuid);
    Optional<LosingParent> findFioByUuidAndMember(UUID uuid, FamilyMember familyMember);
    List<LosingParent> findAllByUuid(UUID uuid);
    Set<LosingParent> findAllByUuidIn(Set<UUID> uuidSet);
    void deleteByLosingUUIDAndMember(UUID uuid, FamilyMember fm);
}
