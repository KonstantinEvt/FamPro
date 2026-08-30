package ru.memman.repository;

import ru.memman.entity.Biometric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiometricRepo extends  JpaRepository<Biometric,Long> {




}
