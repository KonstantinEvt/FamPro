package com.example.service;

import com.example.dtos.FamilyMemberDto;
import com.example.entity.Address;
import com.example.entity.FamilyMemberInfo;
import com.example.mappers.FamilyMemberInfoMapper;
import com.example.repository.FamilyMemberInfoRepo;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class FamilyMemberInfoService {
    private final FamilyMemberInfoMapper familyMemberInfoMapper;
    private final FamilyMemberInfoRepo familyMemberInfoRepo;
    private final EmailService emailService;
    private final PhoneService phoneService;
    private final AddressService addressService;
//    private final InternService<?>[] internServiceImp={emailService,phoneService};

    public FamilyMemberInfo merge(FamilyMemberDto familyMemberDto, UUID oldUUID) {
        FamilyMemberInfo fmi = familyMemberInfoMapper.dtoToEntity(familyMemberDto.getMemberInfo());
        FamilyMemberInfo fmiFromBase;
        if (familyMemberDto.getUuid() != oldUUID)
            fmiFromBase = Optional.of(familyMemberInfoRepo.findFamilyMemberInfoByUuid(oldUUID)).get().orElse(new FamilyMemberInfo());
        else fmiFromBase = new FamilyMemberInfo();
        fmi.setId(fmiFromBase.getId());
// Установка и проверка Emails
//        for (InternService<?> service:internServiceImp
//             ) {
//
//        }
        if (fmi.getEmails() != null || fmi.getMainEmail() != null || fmiFromBase.getMainEmail() != null || fmiFromBase.getEmails() != null) {
            emailService.checkMergeAndSetUp(fmi, fmiFromBase);
        }

// Установка и проверка телефонов
        if (fmi.getPhones() != null || fmi.getMainPhone() != null) {
            phoneService.checkMergeAndSetUp(fmi, fmiFromBase);
        }
// Установка и проверка адресов
        if (fmi.getMainAddress() != null || fmi.getAddresses() != null) {
            if (fmi.getAddresses() == null) fmi.setAddresses(new HashSet<>());
            if (fmi.getMainAddress() == null) fmi.setMainAddress(new Address());
            else fmi.setMainAddress(addressService.checkAddress(fmi.getMainAddress()));
            // Установка основного адреса
            String mainAddress = fmi.getMainAddress().getInternName();
            if (fmi.getMainAddress().getId() != null) {
                fmi.getMainAddress().setId(null);
                log.warn("Предоставленная информация об основном адресе имела ID, ID обнулен");
            }
            if (mainAddress != null) {
                Address addressFromBase = addressService.getAddressByFullName(mainAddress);
                if (addressFromBase != null)
                    fmi.setMainAddress(addressService.mergeAddress(fmi.getMainAddress(), addressFromBase));
            } else fmi.setMainAddress(fmiFromBase.getMainAddress());
            // Установка списка адресов
            Set<Address> resultAddress = new HashSet<>();
            resultAddress.add(fmi.getMainAddress());

            for (Address rawAddress : fmi.getAddresses()) {
                Address address = addressService.checkAddress(rawAddress);
                String fullAddress = address.getInternName();
                if (address.getId() != null) {
                    address.setId(null);
                    log.warn("Предоставленная информация о адресе имела ID, ID обнулен");
                }
                if (fullAddress != null) {
                    Address addressFromBase = addressService.getAddressByFullName(fullAddress);
                    if (addressFromBase != null)
                        resultAddress.add(addressService.mergeAddress(address, addressFromBase));
                    else resultAddress.add(address);
                }
            }
            if (fmiFromBase.getAddresses() != null) resultAddress.addAll(fmiFromBase.getAddresses());
            fmi.setAddresses(resultAddress);
            log.info("Адрес(ы) установлен(ы)");
        }
        fmi.setUuid(familyMemberDto.getUuid());
        return fmi;
    }
}