package ru.memman.repository;

import ru.memman.entity.Voting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VotingRepo extends JpaRepository<Voting, UUID> {
}
