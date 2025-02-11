package com.example.repository;

import com.example.entity.DeferredDirective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectiveRepo extends JpaRepository<DeferredDirective,Long> {
}
