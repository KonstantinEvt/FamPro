package ru.memman.repository;

import ru.memman.entity.AloneNew;
import ru.memman.enums.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AloneNewRepo extends JpaRepository<AloneNew, UUID> {
    List<AloneNew> findAllByExternId(String uuid);
}
