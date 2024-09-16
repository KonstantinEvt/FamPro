package com.example.service;

import com.example.entity.Address;
import com.example.entity.FamilyMemberInfo;
import com.example.repository.InternRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class AddressService extends InternServiceImp<Address> {

    public AddressService(@Qualifier("addressRepo") InternRepo<Address> internRepo) {
        super(internRepo);
    }

    @Override
    public void check(Address address) {
        if (address.getInternName() != null) {
            address.setTechString(address.getInternName());
            super.check(address);
        } else address.setTechString("uncorrected");
    }

    @Override
    public void checkMergeAndSetUp(FamilyMemberInfo newFmi, FamilyMemberInfo fmiFromBase) {
        Set<String> namesAddresses = new HashSet<>();
        Address mainAddress = new Address();
        if (newFmi.getMainAddress() != null) {
            mainAddress.setInternName(newFmi.getMainAddress());
            check(mainAddress);
            if (!mainAddress.getTechString().equals("uncorrected")) {
                newFmi.setMainAddress(mainAddress.getInternName());
                namesAddresses.add(mainAddress.getInternName());
            } else newFmi.setMainPhone(null);
        }
        if (newFmi.getAddresses() != null && !newFmi.getAddresses().isEmpty()) {
            for (Address address : newFmi.getAddresses()) {
                address.setInternName(resolveFullAddress(address));
                check(address);
                if (!address.getTechString().equals("uncorrected")) {
                    namesAddresses.add(address.getInternName());
                    if (address.getId() != null) address.setId(null);
                }
            }
        }
        if (newFmi.getMainAddress() == null && fmiFromBase.getMainAddress() != null) {
            newFmi.setMainAddress(fmiFromBase.getMainAddress());
            log.info("Основной адрес взят из старой записи, т.к. валидной информацаии об основном адресе в новой записи нет");
        }

        Set<Address> addressesFromBase;
        if (!namesAddresses.isEmpty()) addressesFromBase = getAllInternEntityByNames(namesAddresses);
        else addressesFromBase = new HashSet<>();

        Map<String, Address> resultList = mergeSetsOfInterns(newFmi.getAddresses(), fmiFromBase.getAddresses(), addressesFromBase);
        if (mainAddress.getInternName() != null && !mainAddress.getTechString().equals("uncorrected") && !resultList.containsKey(mainAddress.getInternName())) {
            mainAddress.setDescription("Основной адрес");
            mainAddress.setId(null);
            mainAddress.setUuid(newFmi.getUuid());
            mainAddress.setTechString("ONE USER");
            resultList.put(mainAddress.getInternName(), mainAddress);
        }
        newFmi.setAddresses(new HashSet<>());
        for (Address address : resultList.values()) newFmi.getAddresses().add(address);


        log.info("Адрес(ы) установлен(ы)");
    }

    public String resolveFullAddress(Address address) {
        return String.join(", ", address.getCountry(), address.getCity(), address.getStreet(), address.getHouse(), address.getBuilding(), address.getFlat());

    }
}