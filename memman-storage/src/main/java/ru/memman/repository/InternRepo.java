package ru.memman.repository;

import ru.memman.entity.InternEntity;

import java.util.Set;

public interface InternRepo<T extends InternEntity> {
    Set<T> findAllByInternNameIn(Set<String> set);
}
