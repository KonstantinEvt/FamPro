package com.example.service;

import com.example.entity.Address;
import com.example.entity.FamilyMemberInfo;
import com.example.enums.CheckStatus;
import com.example.repository.InternRepo;
import com.example.repository.MainAddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class AddressService extends InternServiceImp<Address> {
    private final MainAddressRepository mainAddressRepository;

    public AddressService(@Qualifier("addressRepo") InternRepo<Address> internRepo, MainAddressRepository mainAddressRepository) {
        super(internRepo);
        this.mainAddressRepository = mainAddressRepository;
    }

    @Override
    public void check(Address address) {
        if (address.getInternName() != null) {
            if (address.getCheckStatus() != CheckStatus.CHECKED) {
                if (address.getTechString() == null) address.setTechString("ONE USER");
                super.check(address);
            }
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
        if (newFmi.getAddressesSet() != null && !newFmi.getAddressesSet().isEmpty()) {
            for (Address address : newFmi.getAddressesSet()) {
                address.setInternName(resolveFullAddress(address));
                check(address);
                if (!address.getTechString().equals("uncorrected")) {
//                    address.setUuid(newFmi.getUuid());
                    namesAddresses.add(address.getInternName());
                    if (address.getId() != null) address.setId(null);
                }
            }
        }
        if (newFmi.getMainAddress() == null || newFmi.getMainAddress().isEmpty()) {

            log.info("Основной адрес взят из старой записи, т.к. валидной информации об основном адресе в новой записи нет");
        } else fmiFromBase.setMainAddress(newFmi.getMainAddress());

        Set<Address> addressesFromBase;
        if (!namesAddresses.isEmpty()) addressesFromBase = getAllInternEntityByNames(namesAddresses);
        else addressesFromBase = new HashSet<>();

        Map<String, Address> resultList = mergeSetsOfInterns(newFmi.getAddressesSet(), fmiFromBase.getAddressesSet(), addressesFromBase);
        if (mainAddress.getInternName() != null && !resultList.containsKey(mainAddress.getInternName())) {
            if (!addressesFromBase.isEmpty() && !mainAddress.getTechString().equals("uncorrected"))
                this.checkForCommunity(mainAddress, fmiFromBase.getAddressesSet(), addressesFromBase);
            if (!mainAddress.getTechString().equals("uncorrected")) {
                if (!mainAddress.getTechString().equals("COMMUNITY")) {
                    mainAddress.setDescription("Main address");
                    mainAddress.setUuid(newFmi.getUuid());
                }
                mainAddress.setId(null);
                resultList.put(mainAddress.getInternName(), mainAddress);
            }
        }
        fmiFromBase.setAddressesSet(new HashSet<>());
        for (Address address : resultList.values()) {
            if (!address.getTechString().equals("COMMUNITY")) address.setUuid(newFmi.getUuid());
            else address.setUuid(null);
            fmiFromBase.getAddressesSet().add(address);
        }
        if (fmiFromBase.getMainAddress() == null && !fmiFromBase.getAddressesSet().isEmpty())
            fmiFromBase.setMainAddress(fmiFromBase.getAddressesSet().stream().findFirst().get().getInternName());

        log.info("Адрес(ы) установлен(ы)");
    }

    public String resolveFullAddress(Address address) {
        return String.join(", ",address.getIndex(), address.getCountry(), address.getRegion(), address.getCity(), address.getStreet(), address.getHouse(), address.getBuilding(), address.getFlatNumber());
    }

    public Set<Address> getAddressByInfoId(Long id) {
        return mainAddressRepository.findAddressesOfPerson(id);
    }
}