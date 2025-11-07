package com.example.service;

import com.example.dtos.*;
import com.example.entity.*;
import com.example.enums.SecretLevel;
import com.example.mappers.*;
import com.example.repository.MainInfoReposirory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FamilyMemberInfoService {
    private final FamilyMemberInfoMapper familyMemberInfoMapper;
    private final EmailService emailService;
    private final PhoneService phoneService;
    private final AddressService addressService;
    private final BiometricService biometricService;
    private final BiometricMapper biometricMapper;
    private final DescriptionService descriptionService;
    private final DescriptionMapper descriptionMapper;
    private final PhoneMapper phoneMapper;
    private final AddressMapper addressMapper;
    private final EmailMapper emailMapper;
    private final BirthMapper birthMapper;
    private final BurialMapper burialMapper;
    private final MainInfoReposirory mainInfoReposirory;
    private final BirthService birthService;
    private final BurialService burialService;
    private final Map<String, FamilyMemberDto> tempExtendedDto;
    private final Map<String, MainContact> tempMainContact;

    public FamilyMemberInfoService(FamilyMemberInfoMapper familyMemberInfoMapper,
                                   EmailService emailService,
                                   PhoneService phoneService,
                                   AddressService addressService,
                                   BiometricService biometricService,
                                   BiometricMapper biometricMapper,
                                   DescriptionService descriptionService,
                                   DescriptionMapper descriptionMapper,
                                   PhoneMapper phoneMapper,
                                   AddressMapper addressMapper,
                                   EmailMapper emailMapper,
                                   BirthMapper birthMapper,
                                   BurialMapper burialMapper,
                                   MainInfoReposirory mainInfoReposirory,
                                   BirthService birthService,
                                   BurialService burialService, Map<String, FamilyMemberDto> tempExtendedDto, Map<String, MainContact> tempMainContact) {
        this.familyMemberInfoMapper = familyMemberInfoMapper;
        this.emailService = emailService;
        this.phoneService = phoneService;
        this.addressService = addressService;
        this.biometricService = biometricService;
        this.biometricMapper = biometricMapper;
        this.descriptionService = descriptionService;
        this.descriptionMapper = descriptionMapper;
        this.phoneMapper = phoneMapper;
        this.addressMapper = addressMapper;
        this.emailMapper = emailMapper;
        this.birthMapper = birthMapper;
        this.burialMapper = burialMapper;
        this.mainInfoReposirory = mainInfoReposirory;
        this.birthService = birthService;
        this.burialService = burialService;
        this.tempExtendedDto = tempExtendedDto;
        this.tempMainContact = tempMainContact;
    }

    @Transactional
    public FamilyMemberInfo merge(FamilyMemberDto familyMemberDto, FamilyMemberInfo fmiFromBase, String token, boolean changingMain) {
        FamilyMemberInfo fmi = familyMemberInfoMapper.dtoToEntity(familyMemberDto.getMemberInfo());
        fmi.setUuid(familyMemberDto.getUuid());
        if (!familyMemberDto.getMemberInfo().isPhotoBurialExist() && fmiFromBase.isPhotoBurialExist()) {
            familyMemberDto.getMemberInfo().getBurial().setPhotoExist(true);
        }
        if (!familyMemberDto.getMemberInfo().isPhotoBirthExist() && fmiFromBase.isPhotoBirthExist()) {
            familyMemberDto.getMemberInfo().getBirth().setPhotoExist(true);
        }
        boolean tempAcceptChange = false;
        FamilyMemberDto tempStatusEdit = tempExtendedDto.get(token);
        MainContact mainContact = tempMainContact.get(token);
//        if (fmiFromBase.getId() != null)
//            fmiFromBase = mainInfoReposirory.getFullFamilyMemberInfo(familyMemberDto.getMemberInfo(), fmiFromBase.getId()).orElseThrow(() -> new RuntimeException("MemberInfo is corrupt"));
        if ((fmi.getSecretLevelEmail() != SecretLevel.CLOSE && fmi.getSecretLevelEmail() != SecretLevel.UNDEFINED) &&
                (fmi.getMainEmail() != null || familyMemberDto.getMemberInfo().getEmails() != null)) {
            if (tempStatusEdit != null && tempStatusEdit.getMemberInfo() != null && tempStatusEdit.getMemberInfo().getEmails() != null) {
                Set<String> emailsExtended = tempStatusEdit.getMemberInfo().getEmails().stream().map(EmailDto::getInternName).collect(Collectors.toSet());
                Set<String> emailsDto;
                if (familyMemberDto.getMemberInfo().getEmails() != null)
                    emailsDto = familyMemberDto.getMemberInfo().getEmails().stream().map(EmailDto::getInternName).collect(Collectors.toSet());
                else emailsDto = new HashSet<>();
                emailsDto.add(fmi.getMainEmail());
                if (!Objects.equals(emailsExtended, emailsDto)) tempAcceptChange = true;
            }
            if (Objects.equals(token, "add") || (tempStatusEdit == null || tempStatusEdit.getMemberInfo() == null ||
                    !Objects.equals(familyMemberDto.getMemberInfo().getMainEmail(), mainContact.getMainEmail()) || tempAcceptChange)) {
                if (familyMemberDto.getMemberInfo().getEmails() == null) fmi.setEmailsSet(new HashSet<>());
                else
                    fmi.setEmailsSet(new HashSet<>(emailMapper.collectionDtoToCollectionEntity(familyMemberDto.getMemberInfo().getEmails())));

                emailService.checkMergeAndSetUp(fmi, fmiFromBase);
            }
            tempAcceptChange = false;
        }
        if ((fmi.getSecretLevelPhone() != SecretLevel.CLOSE && fmi.getSecretLevelPhone() != SecretLevel.UNDEFINED) &&
                (fmi.getMainPhone() != null || familyMemberDto.getMemberInfo().getPhones() != null)) {
            if (tempStatusEdit != null && tempStatusEdit.getMemberInfo() != null && tempStatusEdit.getMemberInfo().getPhones() != null) {
                Set<String> phonesExtended = tempStatusEdit.getMemberInfo().getPhones().stream().map(PhoneDto::getInternName).collect(Collectors.toSet());
                Set<String> phonesDto;
                if (familyMemberDto.getMemberInfo().getPhones() != null)
                    phonesDto = familyMemberDto.getMemberInfo().getPhones().stream().map(PhoneDto::getInternName).collect(Collectors.toSet());
                else phonesDto = new HashSet<>();
                phonesDto.add(fmi.getMainPhone());
                if (!Objects.equals(phonesExtended, phonesDto)) tempAcceptChange = true;
            }
            if (Objects.equals(token, "add") || (tempStatusEdit == null || tempStatusEdit.getMemberInfo() == null ||
                    !Objects.equals(familyMemberDto.getMemberInfo().getMainPhone(), mainContact.getMainPhone()) || tempAcceptChange)) {

                if (familyMemberDto.getMemberInfo().getPhones() == null) fmi.setPhonesSet(new HashSet<>());
                else
                    fmi.setPhonesSet(new HashSet<>(phoneMapper.collectionDtoToCollectionEntity(familyMemberDto.getMemberInfo().getPhones())));
                phoneService.checkMergeAndSetUp(fmi, fmiFromBase);
            }
            tempAcceptChange = false;
        }
        if ((fmi.getSecretLevelAddress() != SecretLevel.CLOSE && fmi.getSecretLevelAddress() != SecretLevel.UNDEFINED) &&
                (fmi.getSecretLevelAddress() != null || familyMemberDto.getMemberInfo().getAddresses() != null)) {
            if (tempStatusEdit != null && tempStatusEdit.getMemberInfo() != null && tempStatusEdit.getMemberInfo().getAddresses() != null) {
                Set<String> addressExtended = tempStatusEdit.getMemberInfo().getAddresses().stream().map(AddressDto::getInternName).collect(Collectors.toSet());
                Set<String> addressDto = new HashSet<>();
                if (familyMemberDto.getMemberInfo().getAddresses() != null)
                    for (AddressDto address :
                            familyMemberDto.getMemberInfo().getAddresses()) {
                        addressDto.add(addressService.resolveFullAddress(addressMapper.dtoToEntity(address)));
                    }
                addressDto.removeAll(addressExtended);
                if (!addressDto.isEmpty()) tempAcceptChange = true;
            }
            if (Objects.equals(token, "add") || (tempStatusEdit == null || tempStatusEdit.getMemberInfo() == null ||
                    tempAcceptChange)) {

                if (familyMemberDto.getMemberInfo().getAddresses() == null) fmi.setAddressesSet(new HashSet<>());
                else
                    fmi.setAddressesSet(new HashSet<>(addressMapper.collectionDtoToCollectionEntity(familyMemberDto.getMemberInfo().getAddresses())));
                addressService.checkMergeAndSetUp(fmi, fmiFromBase);
            }
            tempAcceptChange = false;
        }
        if (fmi.getSecretLevelBiometric() != SecretLevel.CLOSE &&
                fmi.getSecretLevelBiometric() != SecretLevel.UNDEFINED &&
                familyMemberDto.getMemberInfo().getBiometric() != null) {
            if (tempStatusEdit != null &&
                    tempStatusEdit.getMemberInfo() != null &&
                    tempStatusEdit.getMemberInfo().getBiometric() != null) {
                tempStatusEdit.getMemberInfo().getBiometric().setId(null);
                tempStatusEdit.getMemberInfo().getBiometric().setUuid(null);
                tempAcceptChange = !Objects.equals(familyMemberDto.getMemberInfo().getBiometric(), tempStatusEdit.getMemberInfo().getBiometric());
            }
            if (Objects.equals(token, "add") ||
                    tempStatusEdit == null ||
                    tempStatusEdit.getMemberInfo() == null || tempAcceptChange) {
                fmi.setBiometricData(List.of(biometricMapper.dtoToEntity(familyMemberDto.getMemberInfo().getBiometric())));
                biometricService.mergeBiometric(fmi, fmiFromBase);
            }
            tempAcceptChange = false;
        }
        if ((fmi.getSecretLevelDescription() != SecretLevel.CLOSE && fmi.getSecretLevelDescription() != SecretLevel.UNDEFINED) && familyMemberDto.getMemberInfo().getDescription() != null) {
            if (tempStatusEdit != null &&
                    tempStatusEdit.getMemberInfo() != null &&
                    tempStatusEdit.getMemberInfo().getDescription() != null) {
                tempStatusEdit.getMemberInfo().getDescription().setId(null);
                tempAcceptChange = !Objects.equals(familyMemberDto.getMemberInfo().getDescription(), tempStatusEdit.getMemberInfo().getDescription());
            }
            if (Objects.equals(token, "add") ||
                    tempStatusEdit == null ||
                    tempStatusEdit.getMemberInfo() == null || tempAcceptChange) {
                fmi.setDescriptionData(List.of(descriptionMapper.dtoToEntity(familyMemberDto.getMemberInfo().getDescription())));
                descriptionService.mergeDescription(fmi, fmiFromBase);
            }
            tempAcceptChange = false;
        }
        if ((fmi.getSecretLevelBurial() != SecretLevel.CLOSE && fmi.getSecretLevelBurial() != SecretLevel.UNDEFINED) && familyMemberDto.getMemberInfo().getBurial() != null) {
            if (tempStatusEdit != null &&
                    tempStatusEdit.getMemberInfo() != null &&
                    tempStatusEdit.getMemberInfo().getBurial() != null) {
                tempStatusEdit.getMemberInfo().getBurial().setId(null);
                tempStatusEdit.getMemberInfo().getBurial().setInternName(null);
                tempAcceptChange = !Objects.equals(familyMemberDto.getMemberInfo().getBurial(), tempStatusEdit.getMemberInfo().getBurial());
            }
            if (Objects.equals(token, "add") ||
                    tempStatusEdit == null ||
                    tempStatusEdit.getMemberInfo() == null || tempAcceptChange) {
                fmi.setBurialPlace(List.of(burialMapper.dtoToEntity(familyMemberDto.getMemberInfo().getBurial())));
                burialService.checkMergeAndSetUp(fmi, fmiFromBase);
            }
            tempAcceptChange = false;
        }
        if ((fmi.getSecretLevelBirth() != SecretLevel.CLOSE && fmi.getSecretLevelBirth() != SecretLevel.UNDEFINED) && familyMemberDto.getMemberInfo().getBirth() != null) {
            if (tempStatusEdit != null &&
                    tempStatusEdit.getMemberInfo() != null &&
                    tempStatusEdit.getMemberInfo().getBirth() != null) {
                tempStatusEdit.getMemberInfo().getBirth().setId(null);
                tempStatusEdit.getMemberInfo().getBirth().setInternName(null);
                tempAcceptChange = !Objects.equals(familyMemberDto.getMemberInfo().getBirth(), tempStatusEdit.getMemberInfo().getBirth());
            }

            if (Objects.equals(token, "add") ||
                    tempStatusEdit == null ||
                    tempStatusEdit.getMemberInfo() == null || tempAcceptChange) {
                fmi.setBirthPlace(List.of(birthMapper.dtoToEntity(familyMemberDto.getMemberInfo().getBirth())));
                birthService.checkMergeAndSetUp(fmi, fmiFromBase);
            }
        }
        if (changingMain) {
            fmiFromBase.setUuid(fmi.getUuid());
            if (!Objects.equals(token, "add") && tempStatusEdit != null && tempStatusEdit.getMemberInfo() != null)
                changeUuidAllExistInfoElements(fmiFromBase, tempStatusEdit.getMemberInfo());
        }
        return fmiFromBase;
    }

    @Transactional
    public void changeUuidAllExistInfoElements(FamilyMemberInfo newInfo, FamilyMemberInfoDto oldInfo) {
        if (oldInfo.getBurial() != null) newInfo.getBurialPlace().get(0).setUuid(newInfo.getUuid());
        if (oldInfo.getBirth() != null) newInfo.getBirthPlace().get(0).setUuid(newInfo.getUuid());
        if (oldInfo.getBiometric() != null) newInfo.getBiometricData().get(0).setUuid(newInfo.getUuid());
        if (oldInfo.getDescription() != null) newInfo.getDescriptionData().get(0).setUuid(newInfo.getUuid());
        if (oldInfo.getEmails() != null && !oldInfo.getEmails().isEmpty()) for (Email email :
                newInfo.getEmailsSet()) {
            if (email.getUuid() != null) email.setUuid(newInfo.getUuid());
        }
        if (oldInfo.getPhones() != null && !oldInfo.getPhones().isEmpty()) for (Phone phone :
                newInfo.getPhonesSet()) {
            if (phone.getUuid() != null) phone.setUuid(newInfo.getUuid());
        }
        if (oldInfo.getAddresses() != null && !oldInfo.getAddresses().isEmpty()) for (Address address :
                newInfo.getAddressesSet()) {
            if (address.getUuid() != null) address.setUuid(newInfo.getUuid());
        }
    }

    @Transactional(readOnly = true)
    public FamilyMemberInfoDto getSimpleInfoDto(FamilyMemberInfo familyMemberInfo) {
        return familyMemberInfoMapper.entityToDto(familyMemberInfo);
    }

    @Transactional(readOnly = true)
    public FamilyMemberInfo getSimpleInfo(FamilyMemberInfoDto familyMemberInfodto) {
        return familyMemberInfoMapper.dtoToEntity(familyMemberInfodto);
    }

    @Transactional(readOnly = true)
    public FamilyMemberInfoDto getInfoDto(FamilyMemberInfo familyMemberInfo) {
        FamilyMemberInfoDto dto = familyMemberInfoMapper.entityToDto(familyMemberInfo);
        if (familyMemberInfo.getSecretLevelAddress() != SecretLevel.UNDEFINED && familyMemberInfo.getSecretLevelAddress() != SecretLevel.CLOSE) {
            if (familyMemberInfo.getAddressesSet() != null && !familyMemberInfo.getAddressesSet().isEmpty())
                dto.setAddresses(new HashSet<>(addressMapper.collectionEntityToCollectionDto(familyMemberInfo.getAddressesSet())));
            else
                dto.setAddresses(new HashSet<>(addressMapper.collectionEntityToCollectionDto(addressService.getAddressByInfoId(familyMemberInfo.getId()))));
            dto.setMainAddress(familyMemberInfo.getMainAddress());
        } else dto.setAddresses(null);

        if (familyMemberInfo.getSecretLevelEmail() != SecretLevel.UNDEFINED && familyMemberInfo.getSecretLevelEmail() != SecretLevel.CLOSE) {
            if (familyMemberInfo.getEmailsSet() != null && !familyMemberInfo.getEmailsSet().isEmpty())
                dto.setEmails(new HashSet<>(emailMapper.collectionEntityToCollectionDto(familyMemberInfo.getEmailsSet())));
            else
                dto.setEmails(new HashSet<>(emailMapper.collectionEntityToCollectionDto(emailService.getEmailsByInfoId(familyMemberInfo.getId()))));
            dto.setMainEmail(familyMemberInfo.getMainEmail());
        } else dto.setEmails(null);
        if (familyMemberInfo.getSecretLevelPhone() != SecretLevel.UNDEFINED && familyMemberInfo.getSecretLevelPhone() != SecretLevel.CLOSE) {
            if (familyMemberInfo.getPhonesSet() != null && !familyMemberInfo.getPhonesSet().isEmpty())
                dto.setPhones(new HashSet<>(phoneMapper.collectionEntityToCollectionDto(familyMemberInfo.getPhonesSet())));
            else
                dto.setPhones(new HashSet<>(phoneMapper.collectionEntityToCollectionDto(phoneService.getPhonesByInfoId(familyMemberInfo.getId()))));
            dto.setMainPhone(familyMemberInfo.getMainPhone());
        } else
            dto.setPhones(null);
        if (familyMemberInfo.getSecretLevelBiometric() != SecretLevel.UNDEFINED && familyMemberInfo.getSecretLevelBiometric() != SecretLevel.CLOSE) {
            if (familyMemberInfo.getBiometricData() != null && !familyMemberInfo.getBiometricData().isEmpty())
                dto.setBiometric(biometricMapper.entityToDto(familyMemberInfo.getBiometricData().get(0)));
            else
                dto.setBiometric(biometricMapper.entityToDto(biometricService.getBiometricByInfoId(familyMemberInfo.getId()).get(0)));
        } else dto.setBiometric(null);
        if (familyMemberInfo.getSecretLevelDescription() != SecretLevel.UNDEFINED && familyMemberInfo.getSecretLevelDescription() != SecretLevel.CLOSE) {
            if (familyMemberInfo.getDescriptionData() != null && !familyMemberInfo.getDescriptionData().isEmpty())
                dto.setDescription(descriptionMapper.entityToDto(familyMemberInfo.getDescriptionData().get(0)));
            else
                dto.setDescription(descriptionMapper.entityToDto(descriptionService.getDescriptionByInfoId(familyMemberInfo.getId()).get(0)));
        } else dto.setDescription(null);
        if (familyMemberInfo.getSecretLevelBirth() != SecretLevel.UNDEFINED && familyMemberInfo.getSecretLevelBirth() != SecretLevel.CLOSE) {
            if (familyMemberInfo.getBirthPlace() != null && !familyMemberInfo.getBirthPlace().isEmpty())
                dto.setBirth(birthMapper.entityToDto(familyMemberInfo.getBirthPlace().get(0)));
            else
                dto.setBirth(birthMapper.entityToDto(birthService.getPlaceBirthByInfoId(familyMemberInfo.getId()).get(0)));
        } else dto.setBirth(null);
        if (familyMemberInfo.getSecretLevelBurial() != SecretLevel.UNDEFINED && familyMemberInfo.getSecretLevelBurial() != SecretLevel.CLOSE) {
            if (familyMemberInfo.getBurialPlace() != null && !familyMemberInfo.getBurialPlace().isEmpty())
                dto.setBurial(burialMapper.entityToDto(familyMemberInfo.getBurialPlace().get(0)));
            else
                dto.setBurial(burialMapper.entityToDto(burialService.getPlaceBurialByInfoId(familyMemberInfo.getId()).get(0)));
        } else dto.setBurial(null);
        return dto;
    }

    public FamilyMemberInfoDto covertSecurityDtoToInfo(SecurityDto securityDto) {
        FamilyMemberInfoDto familyMemberInfoDto = new FamilyMemberInfoDto();
        if (securityDto.getPersonId() != null) familyMemberInfoDto.setId(securityDto.getPersonId());
        if (securityDto.getPersonUuid() != null)
            familyMemberInfoDto.setUuid(UUID.fromString(securityDto.getPersonUuid()));
        if (securityDto.getSecretLevelPhone() != null)
            familyMemberInfoDto.setSecretLevelPhone(securityDto.getSecretLevelPhone());
        if (securityDto.getSecretLevelBiometric() != null)
            familyMemberInfoDto.setSecretLevelBiometric(securityDto.getSecretLevelBiometric());
        if (securityDto.getSecretLevelDescription() != null)
            familyMemberInfoDto.setSecretLevelDescription(securityDto.getSecretLevelDescription());
        if (securityDto.getSecretLevelEmail() != null)
            familyMemberInfoDto.setSecretLevelEmail(securityDto.getSecretLevelEmail());
        if (securityDto.getSecretLevelAddress() != null)
            familyMemberInfoDto.setSecretLevelAddress(securityDto.getSecretLevelAddress());
        if (securityDto.getSecretLevelBurial() != null)
            familyMemberInfoDto.setSecretLevelBurial(securityDto.getSecretLevelBurial());
        if (securityDto.getSecretLevelBirth() != null)
            familyMemberInfoDto.setSecretLevelBirth(securityDto.getSecretLevelBirth());
        return familyMemberInfoDto;
    }

    public boolean equalsSecurity(FamilyMemberInfoDto fromSecurityDto, FamilyMemberInfoDto infoDto) {
        log.info("from Security: {}", fromSecurityDto);
        log.info("from map: {}", infoDto);
        return fromSecurityDto.getId().equals(infoDto.getId())
                && fromSecurityDto.getUuid().equals(infoDto.getUuid())
                && fromSecurityDto.getSecretLevelBiometric().equals(infoDto.getSecretLevelBiometric())
                && fromSecurityDto.getSecretLevelDescription().equals(infoDto.getSecretLevelDescription())
                && fromSecurityDto.getSecretLevelBurial().equals(infoDto.getSecretLevelBurial())
                && fromSecurityDto.getSecretLevelBirth().equals(infoDto.getSecretLevelBirth())
                && fromSecurityDto.getSecretLevelAddress().equals(infoDto.getSecretLevelAddress())
                && fromSecurityDto.getSecretLevelEmail().equals(infoDto.getSecretLevelEmail())
                && fromSecurityDto.getSecretLevelPhone().equals(infoDto.getSecretLevelPhone());
    }

    public void secretMerge(FamilyMemberInfoDto newInfo, FamilyMemberInfo oldInfo) {
        if (newInfo.getId() == null && newInfo.getBurial() == null)
            oldInfo.setSecretLevelBurial(SecretLevel.UNDEFINED);
        else if ((newInfo.getBurial() != null && oldInfo.getSecretLevelBurial() == SecretLevel.UNDEFINED)
                || (oldInfo.getSecretLevelBurial() != SecretLevel.UNDEFINED && newInfo.getSecretLevelBurial() != oldInfo.getSecretLevelBurial()))
            oldInfo.setSecretLevelBurial(newInfo.getSecretLevelBurial());

        if (newInfo.getId() == null && newInfo.getBirth() == null)
            oldInfo.setSecretLevelBirth(SecretLevel.UNDEFINED);
        else if ((newInfo.getBirth() != null && oldInfo.getSecretLevelBirth() == SecretLevel.UNDEFINED)
                || (oldInfo.getSecretLevelBirth() != SecretLevel.UNDEFINED && newInfo.getSecretLevelBirth() != oldInfo.getSecretLevelBirth()))
            oldInfo.setSecretLevelBirth(newInfo.getSecretLevelBirth());

        if (newInfo.getId() == null && newInfo.getBiometric() == null)
            oldInfo.setSecretLevelBiometric(SecretLevel.UNDEFINED);
        else if ((newInfo.getBiometric() != null && oldInfo.getSecretLevelBiometric() == SecretLevel.UNDEFINED)
                || (oldInfo.getSecretLevelBiometric() != SecretLevel.UNDEFINED && newInfo.getSecretLevelBiometric() != oldInfo.getSecretLevelBiometric()))
            oldInfo.setSecretLevelBiometric(newInfo.getSecretLevelBiometric());

        if (newInfo.getId() == null && newInfo.getDescription() == null)
            oldInfo.setSecretLevelDescription(SecretLevel.UNDEFINED);
        else if ((newInfo.getDescription() != null && oldInfo.getSecretLevelDescription() == SecretLevel.UNDEFINED)
                || (oldInfo.getSecretLevelDescription() != SecretLevel.UNDEFINED && newInfo.getSecretLevelDescription() != oldInfo.getSecretLevelDescription()))
            oldInfo.setSecretLevelDescription(newInfo.getSecretLevelDescription());

        if (newInfo.getId() == null && newInfo.getEmails() == null && newInfo.getMainEmail() == null)
            oldInfo.setSecretLevelEmail(SecretLevel.UNDEFINED);
        else if (((newInfo.getEmails() != null || newInfo.getMainEmail() != null) && oldInfo.getSecretLevelEmail() == SecretLevel.UNDEFINED)
                || (oldInfo.getSecretLevelEmail() != SecretLevel.UNDEFINED && newInfo.getSecretLevelEmail() != oldInfo.getSecretLevelEmail()))
            oldInfo.setSecretLevelEmail(newInfo.getSecretLevelEmail());

        if (newInfo.getId() == null && newInfo.getPhones() == null && newInfo.getMainPhone() == null)
            oldInfo.setSecretLevelPhone(SecretLevel.UNDEFINED);
        else if (((newInfo.getPhones() != null || newInfo.getMainPhone() != null) && oldInfo.getSecretLevelPhone() == SecretLevel.UNDEFINED)
                || (oldInfo.getSecretLevelPhone() != SecretLevel.UNDEFINED && newInfo.getSecretLevelPhone() != oldInfo.getSecretLevelPhone()))
            oldInfo.setSecretLevelPhone(newInfo.getSecretLevelPhone());

        if (newInfo.getId() == null && newInfo.getAddresses() == null && newInfo.getMainAddress() == null)
            oldInfo.setSecretLevelAddress(SecretLevel.UNDEFINED);
        else if (((newInfo.getAddresses() != null || newInfo.getMainAddress() != null) && oldInfo.getSecretLevelAddress() == SecretLevel.UNDEFINED)
                || (oldInfo.getSecretLevelAddress() != SecretLevel.UNDEFINED && newInfo.getSecretLevelAddress() != oldInfo.getSecretLevelAddress()))
            oldInfo.setSecretLevelAddress(newInfo.getSecretLevelAddress());
    }
}