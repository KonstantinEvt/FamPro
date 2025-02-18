package com.example.repository;

import com.example.entity.AloneNew;
import com.example.enums.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AloneNewRepo extends JpaRepository<AloneNew,Long> {
    List<AloneNew> findAllByCategory(NewsCategory category);
}
