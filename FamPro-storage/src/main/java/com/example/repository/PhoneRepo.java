package com.example.repository;

import com.example.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepo extends JpaRepository<Phone,Long> {
    Phone findPhoneByPhoneNumber(String phoneNumber);
}
