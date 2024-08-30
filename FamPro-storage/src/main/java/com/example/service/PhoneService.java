package com.example.service;

import com.example.entity.Phone;
import com.example.mappers.PhoneMapper;
import com.example.repository.PhoneRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class PhoneService {
    private final PhoneMapper phoneMapper;
    private final PhoneRepo phoneRepo;
    Phone getPhoneByPhoneNumber(String phoneNumber) {
        return phoneRepo.findPhoneByPhoneNumber(phoneNumber);
    }
}
