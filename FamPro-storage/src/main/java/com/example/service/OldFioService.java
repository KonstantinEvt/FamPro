package com.example.service;

import com.example.dtos.FioDto;
import com.example.entity.FamilyMember;
import com.example.entity.Notification;
import com.example.entity.OldFio;
import com.example.enums.Attention;
import com.example.enums.Subject;
import com.example.mappers.FioMapper;
import com.example.mappers.OldNamesMapper;
import com.example.repository.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Getter
@Setter
public class OldFioService extends FioServiceImp<OldFio> {
    private final OldFioRepo fioRepo;
    private final MainOtherFioRepository mainOtherFioRepository;
    private final OldNamesMapper oldNamesMapper;
    private final FamilyMemberRepo fmRepo;
    private final OldFioRepository oldFioRepository;
    private final MainStorageRepository mainStorageRepository;

    public OldFioService(FioMapper fioMapper, OldFioRepo fioRepo, MainOtherFioRepository mainOtherFioRepository, OldNamesMapper oldNamesMapper, FamilyMemberRepo fmRepo, OldFioRepository oldFioRepository, MainStorageRepository mainStorageRepository) {
        super(fioMapper);
        this.fioRepo = fioRepo;
        this.mainOtherFioRepository = mainOtherFioRepository;
        this.oldNamesMapper = oldNamesMapper;
        this.fmRepo = fmRepo;
        this.oldFioRepository = oldFioRepository;
        this.mainStorageRepository = mainStorageRepository;
    }

    @Transactional
    public Set<OldFio> checkOtherNamesUniquer(FamilyMember familyMember, Set<FioDto> oldFios, List<Notification> notifications) {
        if (oldFios==null||oldFios.isEmpty()) return new HashSet<>();
        Set<OldFio> enteringFio = new HashSet<>();
        for (FioDto oldName :
                oldFios) {
            OldFio oldFio = oldNamesMapper.fioDtoToOldFio(oldName);
            oldFio.setBirthday(familyMember.getBirthday());
            if (oldFio.getFirstName() != null && oldFio.getMiddleName() != null && oldFio.getLastName() != null) {
                oldFio.setUuid(generateUUIDFromFio(oldFio));
                oldFio.setSex(familyMember.getSex());
                oldFio.setFullName(generateFioStringInfo(oldFio));
                if (oldFio.getUuid() != familyMember.getUuid()) {
                    oldFio.setMember(familyMember);
                    enteringFio.add(oldFio);
                } else {
                    notifications.add(Notification.builder().attention(Attention.NEGATIVE).subject(Subject.WRONG_INFO_OTHER).build());
                }
            } else {
                notifications.add(Notification.builder().attention(Attention.NEGATIVE).subject(Subject.WRONG_INFO_OTHER).build());
            }
            log.info("oldFio is prepare to setup: {}", oldFio.getFullName());
        }
        if (!enteringFio.isEmpty()) {
            Set<OldFio> checked = new HashSet<>();
            Set<OldFio> existingInBaseOther = oldFioRepository.findAllOldFiosWithFamilyMembers(enteringFio.stream().map(OldFio::getUuid).collect(Collectors.toSet()));
            if (!existingInBaseOther.isEmpty())
                for (OldFio oldFio :
                        enteringFio) {
                    for (OldFio oldFio1 :
                            existingInBaseOther) {
                        if (Objects.equals(oldFio.getUuid(), oldFio1.getUuid())) {
                            checked.add(oldFio);
                            if (!Objects.equals(oldFio1.getMember().getUuid(), familyMember.getUuid()))
                                notifications.add(Notification.builder().attention(Attention.NEGATIVE).subject(Subject.DUPLICATE_OTHER).id(oldFio.getMember().getId()).build());
                        }
                    }
                }
            enteringFio.removeAll(checked);
            if (!enteringFio.isEmpty()) {
                Set<OldFio> checkedHide = new HashSet<>();
                Set<FamilyMember> hideOther = mainStorageRepository.findAllFamilyMemberByUuid(enteringFio.stream().map(OldFio::getUuid).collect(Collectors.toSet()));
                if (!hideOther.isEmpty())
                    for (OldFio oldFio :
                            enteringFio) {
                        for (FamilyMember fm :
                                hideOther) {
                            if (Objects.equals(oldFio.getUuid(), fm.getUuid())) {
                                checkedHide.add(oldFio);
                                notifications.add(Notification.builder().attention(Attention.NEGATIVE).subject(Subject.DUPLICATE_OTHER_HIDE).id(fm.getId()).build());
                            }
                        }
                    }
                enteringFio.removeAll(checkedHide);
            }
        }
        log.info("first oldFio is: {}", enteringFio.stream().map(OldFio::getFullName).findFirst().orElse("empty"));
        return enteringFio;
    }
//    public  Set<OldFio> getNewNames(Set<OldFio> otherNames,FamilyMember fm){
//        Set<OldFio> newOthers=new HashSet<>();
//
//        return newOthers;
//    }

//    @Transactional
//    public Set<OldFio> addAllNewOldNames(Set<FioDto> oldFios, FamilyMember familyMember) {
//        Set<OldFio> enteringFio = new HashSet<>();
//        for (FioDto oldName :
//                oldFios) {
//            OldFio oldFio = oldNamesMapper.fioDtoToOldFio(oldName);
//            oldFio.setBirthday(familyMember.getBirthday());
//            if (oldFio.getFirstName() != null && oldFio.getMiddleName() != null && oldFio.getLastName() != null) {
//                oldFio.setUuid(generateUUIDFromFio(oldFio));
//                oldFio.setSex(familyMember.getSex());
//                oldFio.setFullName(generateFioStringInfo(oldFio));
//                oldFio.setMember(familyMember);
//                Optional<OldFio> findInOld = fioRepo.findFioByUuid(oldFio.getUuid());
//                if (findInOld.isPresent() && !findInOld.get().getMember().getUuid().equals(familyMember.getUuid()))
//                    throw new Dublicate("Введенное альтернативное имя уже есть в базе. Оно принадлежит человеку с ID " + findInOld.get().getMember().getId());
//                else {
//                    Optional<FamilyMember> findFM = fmRepo.findFioByUuid(oldFio.getUuid());
//                    if (findFM.isPresent())
//                        throw new Dublicate("Введенное альтернативное имя - основное имя человека с ID " + findFM.get().getId());
//                }
//                enteringFio.add(oldFio);
//            }
//        }
//        Set<OldFio> checkedOldFio = familyMember.getOtherNames();
//        Set<OldFio> resultSet = new HashSet<>();
//        if (!enteringFio.isEmpty() && checkedOldFio != null && !checkedOldFio.isEmpty()) {
//            for (OldFio o1 :
//                    enteringFio) {
//                for (OldFio o2 :
//                        checkedOldFio) {
//                    if (!o1.getUuid().equals(o2.getUuid())) resultSet.add(o1);
//                }
//            }
//        } else {
//            if (!enteringFio.isEmpty()) {
//                fioRepo.saveAll(enteringFio);
//                familyMember.setOtherNamesExist(true);
//                return enteringFio;
//            }
//        }
//        if (!resultSet.isEmpty()) fioRepo.saveAll(resultSet);
//        else familyMember.setOtherNamesExist(false);
//
//        return resultSet;
//    }

    public void changeOldFiosBirthday(FamilyMember fm) {
        for (OldFio o :
                fm.getOtherNames()) {
            o.setBirthday(fm.getBirthday());
            o.setUuid(generateUUIDFromFio(o));
            o.setFullName(generateFioStringInfo(o));
        }
    }

    public Set<OldFio> getOtherNamesByInfoId(Long id) {
        return mainOtherFioRepository.findOldNamesOfPerson(id);
    }
}
