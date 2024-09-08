package com.example.repository;

import com.example.entity.InternEntity;

import java.util.Set;

public interface InternRepo<T extends InternEntity> {
    Set<T> findAllByInternNameIn(Set<String> set);
}
