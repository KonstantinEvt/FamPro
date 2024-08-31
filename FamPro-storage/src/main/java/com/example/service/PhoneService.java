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

    public Phone getPhoneByPhoneNumber(String phoneNumber) {
        return phoneRepo.findPhoneByPhoneNumber(phoneNumber);
    }

    public Phone mergePhone(Phone newPhone, Phone oldPhone) {
        if (newPhone.getAssignment() != null) oldPhone.setAssignment(newPhone.getAssignment());
        if (newPhone.getStatus() != null) oldPhone.setStatus(newPhone.getStatus());
        if (newPhone.getDescription() != null) oldPhone.setDescription(newPhone.getDescription());
        return oldPhone;
    }
    public Phone checkPhone(Phone phone) {
        return phone;
    }
}
