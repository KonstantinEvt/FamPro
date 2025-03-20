package com.example.repository;

import com.example.entity.Voting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VotingRepo extends JpaRepository<Voting, UUID> {
}
