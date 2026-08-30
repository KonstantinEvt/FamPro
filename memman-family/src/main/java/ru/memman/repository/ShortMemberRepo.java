package ru.memman.repository;

import ru.memman.entity.ShortFamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShortMemberRepo extends JpaRepository<ShortFamilyMember, Long> {

}
