package com.example.repository;

import com.example.entity.Email;
import com.example.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PhoneRepo extends JpaRepository<Phone,Long> {
    Set<Phone> findAllByPhoneNumberIn(Set<String> set);
}
