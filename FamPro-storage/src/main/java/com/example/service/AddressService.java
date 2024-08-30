package com.example.service;

import com.example.mappers.AddressMapper;
import com.example.repository.AddressRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AddressService {
    private final AddressMapper addressMapper;
    private final AddressRepo addressRepo;
}
