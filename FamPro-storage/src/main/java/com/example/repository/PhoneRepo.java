package com.example.repository;

import com.example.entity.Email;
import com.example.entity.Phone;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@Qualifier("phoneRepo")
public interface PhoneRepo extends JpaRepository<Phone,Long>,InternRepo<Phone> {
    @Override
    Set<Phone> findAllByInternNameIn(Set<String> set);
}
