package com.example.service;

import com.example.entity.Address;
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

    public Address getAddressByFullName(String address) {
        return addressRepo.findAddressByInternName(address);
    }

    public Address mergeAddress(Address newAddress, Address oldAddress) {
        return oldAddress;
    }

    public Address checkAddress(Address address) {
        if (address.getInternName() == null) address.setInternName(resolveFullAddress(address));
        return address;
    }

    public String resolveFullAddress(Address address) {
        return String.join(", ", address.getCountry(), address.getCity(), address.getStreet(), address.getHouse(), address.getBuilding(), address.getFlat());

    }
}
