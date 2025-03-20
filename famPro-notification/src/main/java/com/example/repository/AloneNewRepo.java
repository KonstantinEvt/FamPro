package com.example.repository;

import com.example.entity.AloneNew;
import com.example.enums.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AloneNewRepo extends JpaRepository<AloneNew, UUID> {
    List<AloneNew> findAllByExternId(String uuid);
}
