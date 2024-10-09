package com.example.repository;

import com.example.entity.FamilyMember;
import com.example.entity.LosingParent;
import com.example.entity.OldFio;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
@Qualifier("oldFioRepo")
public interface OldFioRepo extends JpaRepository<OldFio,Long> {
    Set<OldFio> findAllByUuidIn(Set<UUID> uuidSet);
    public Optional<OldFio> findFioByUuid(UUID uuid);
    public Optional<OldFio> findFioByUuidAndMember(UUID uuid, FamilyMember familyMember);
    List<OldFio> findAllByUuid(UUID uuid);

}
