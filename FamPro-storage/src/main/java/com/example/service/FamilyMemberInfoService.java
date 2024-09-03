package com.example.service;

import com.example.dtos.FamilyMemberDto;
import com.example.entity.Address;
import com.example.entity.Email;
import com.example.entity.FamilyMemberInfo;
import com.example.entity.Phone;
import com.example.exceptions.UncorrectedInformation;
import com.example.mappers.FamilyMemberInfoMapper;
import com.example.repository.FamilyMemberInfoRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class FamilyMemberInfoService {
    private final FamilyMemberInfoMapper familyMemberInfoMapper;
    private final FamilyMemberInfoRepo familyMemberInfoRepo;
    private final EmailService emailService;
    private final PhoneService phoneService;
    private final AddressService addressService;

    FamilyMemberInfo merge(FamilyMemberDto familyMemberDto) {

        FamilyMemberInfo fmi = familyMemberInfoMapper.dtoToEntity(familyMemberDto.getMemberInfo());
        FamilyMemberInfo fmiFromBase = familyMemberInfoRepo.findFamilyMemberInfoByUuid(fmi.getUuid());
        if (fmiFromBase == null) fmiFromBase = new FamilyMemberInfo();


// Установка и проверка Emails
        if (fmi.getEmails() != null || fmi.getMainEmail() != null) {
            Set<String> namesEmails = new HashSet<>();
            if (fmi.getMainEmail() == null) fmi.setMainEmail(new Email());
            else {
                fmi.setMainEmail(emailService.checkEmail(fmi.getMainEmail()));
                namesEmails.add(fmi.getMainEmail().getEmailName());
                if (fmi.getMainEmail().getId() != null) {
                    fmi.getMainEmail().setId(null);
                    log.warn("Предоставленная информация об основном Email имела ID, ID обнулен");
                }
                if (fmi.getMainEmail().getEmailName().equals("uncorrected")) {
                    fmi.setMainEmail(null);
                    log.warn("Предоставленная информация об основном Email некорректна, основной Email обнулен");
                }
            }

            if (fmi.getEmails() == null) fmi.setEmails(new HashSet<>());
            else {
                for (Email email : fmi.getEmails()) {
                    Email checked = emailService.checkEmail(email);
                    namesEmails.add(checked.getEmailName());
                    email.setEmailName(checked.getEmailName());
                    if (email.getId() != null) {
                        email.setId(null);
                        log.warn("Предоставленная информация об Email имела ID, ID обнулен");
                    }
                }
            }
            fmi.getEmails().add(fmi.getMainEmail());
            if (namesEmails.contains("uncorrected") && namesEmails.size() == 1) {
                throw new UncorrectedInformation("Все введенные Emails некорректны");
            } else namesEmails.remove("uncorrected");

            Set<Email> emailsFromBase = emailService.getAllEmailsByEmailsNames(namesEmails);

            Set<Email> resultEmails = new HashSet<>();
            Set<String> namesFromBase = new HashSet<>();
            for (Email email : emailsFromBase) {
                Email res = emailService.mergeEmail(fmi.getMainEmail(), email);
                if (email.getEmailName().equals(fmi.getMainEmail().getEmailName())) {
                    fmi.setMainEmail(res);
                }
                resultEmails.add(res);
                namesFromBase.add(res.getEmailName());
            }
            if (fmi.getMainEmail() == null && fmiFromBase.getMainEmail() != null)
                fmi.setMainEmail(fmiFromBase.getMainEmail());

            if (fmiFromBase.getEmails() != null) resultEmails.addAll(fmiFromBase.getEmails());
            for (String name : namesEmails) {
                if (!namesFromBase.contains(name)) {
                    for (Email em : fmi.getEmails()) {
                        if (em.getEmailName().equals(name)) resultEmails.add(em);
                    }
                }
            }
            fmi.setEmails(resultEmails);
            log.info("Email(s) установлен(ы)");
        }

// Установка и проверка телефонов
        if (fmi.getPhones() != null || fmi.getMainPhone() != null) {
            if (fmi.getPhones() == null) fmi.setPhones(new HashSet<>());
            if (fmi.getMainPhone() == null) fmi.setMainPhone(new Phone());
            else fmi.setMainPhone(phoneService.checkPhone(fmi.getMainPhone()));
            // Установка основного телефона
            String mainPhone = fmi.getMainPhone().getPhoneNumber();
            if (fmi.getMainPhone().getId() != null) {
                fmi.getMainPhone().setId(null);
                log.warn("Предоставленная информация об основном телефоне имела ID, ID обнулен");
            }
            if (mainPhone != null) {
                Phone phoneFromBase = phoneService.getPhoneByPhoneNumber(mainPhone);
                if (phoneFromBase != null)
                    fmi.setMainPhone(phoneService.mergePhone(fmi.getMainPhone(), phoneFromBase));
            } else fmi.setMainPhone(fmiFromBase.getMainPhone());

            // Установка списка телефонов
            Set<Phone> resultPhones = new HashSet<>();
            resultPhones.add(fmi.getMainPhone());

            for (Phone rawPhone : fmi.getPhones()) {
                Phone phone = phoneService.checkPhone(rawPhone);
                String phoneNumber = phone.getPhoneNumber();
                if (phone.getId() != null) {
                    phone.setId(null);
                    log.warn("Предоставленная информация о телефоне имела ID, ID обнулен");
                }
                if (phoneNumber != null) {
                    Phone phoneFromBase = phoneService.getPhoneByPhoneNumber(phoneNumber);
                    if (phoneFromBase != null)
                        resultPhones.add(phoneService.mergePhone(phone, phoneFromBase));
                    else resultPhones.add(phone);
                }
            }
            if (fmiFromBase.getPhones() != null) resultPhones.addAll(fmiFromBase.getPhones());
            fmi.setPhones(resultPhones);
            log.info("Телефон(ы) установлен(ы)");
        }
// Установка и проверка адресов
        if (fmi.getMainAddress() != null || fmi.getAddresses() != null) {
            if (fmi.getAddresses() == null) fmi.setAddresses(new HashSet<>());
            if (fmi.getMainAddress() == null) fmi.setMainAddress(new Address());
            else fmi.setMainAddress(addressService.checkAddress(fmi.getMainAddress()));
            // Установка основного адреса
            String mainAddress = fmi.getMainAddress().getFullAddress();
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
                String fullAddress = address.getFullAddress();
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
        return fmi;
    }
}