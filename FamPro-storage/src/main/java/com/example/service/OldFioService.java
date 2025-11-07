package com.example.service;

import com.example.dtos.FioDto;
import com.example.entity.FamilyMember;
import com.example.entity.OldFio;
import com.example.exceptions.Dublicate;
import com.example.mappers.FioMapper;
import com.example.mappers.OldNamesMapper;
import com.example.repository.FamilyMemberRepo;
import com.example.repository.MainOtherFioRepository;
import com.example.repository.OldFioRepo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@Getter
@Setter
public class OldFioService extends FioServiceImp<OldFio> {
    private final OldFioRepo fioRepo;
    private final MainOtherFioRepository mainOtherFioRepository;
    private final OldNamesMapper oldNamesMapper;
    private final FamilyMemberRepo fmRepo;

    public OldFioService(FioMapper fioMapper, OldFioRepo fioRepo, MainOtherFioRepository mainOtherFioRepository, OldNamesMapper oldNamesMapper, FamilyMemberRepo fmRepo) {
        super(fioMapper);
        this.fioRepo = fioRepo;
        this.mainOtherFioRepository = mainOtherFioRepository;
        this.oldNamesMapper = oldNamesMapper;
        this.fmRepo = fmRepo;
    }

    @Transactional
    public Set<OldFio> addAllNewOldNames(Set<FioDto> oldFios, FamilyMember familyMember) {
        Set<OldFio> enteringFio = new HashSet<>();
        for (FioDto oldName :
                oldFios) {
            OldFio oldFio = oldNamesMapper.fioDtoToOldFio(oldName);
            oldFio.setBirthday(familyMember.getBirthday());
            if (oldFio.getFirstName() != null && oldFio.getMiddleName() != null && oldFio.getLastName() != null) {
                oldFio.setUuid(generateUUIDFromFio(oldFio));
                oldFio.setSex(familyMember.getSex());
                oldFio.setFullName(generateFioStringInfo(oldFio));
                oldFio.setMember(familyMember);
                Optional<OldFio> findInOld = fioRepo.findFioByUuid(oldFio.getUuid());
                if (findInOld.isPresent() && !findInOld.get().getMember().getUuid().equals(familyMember.getUuid()))
                    throw new Dublicate("Введенное альтернативное имя уже есть в базе. Оно принадлежит человеку с ID " + findInOld.get().getMember().getId());
                else {
                    Optional<FamilyMember> findFM = fmRepo.findFioByUuid(oldFio.getUuid());
                    if (findFM.isPresent())
                        throw new Dublicate("Введенное альтернативное имя - основное имя человека с ID " + findFM.get().getId());
                }
                enteringFio.add(oldFio);
            }
        }
        Set<OldFio> checkedOldFio = familyMember.getOtherNames();
        Set<OldFio> resultSet = new HashSet<>();
        if (!enteringFio.isEmpty() && checkedOldFio != null && !checkedOldFio.isEmpty()) {
            for (OldFio o1 :
                    enteringFio) {
                for (OldFio o2 :
                        checkedOldFio) {
                    if (!o1.getUuid().equals(o2.getUuid())) resultSet.add(o1);
                }
            }
        } else {
            if (!enteringFio.isEmpty()) {fioRepo.saveAll(enteringFio);
            familyMember.setOtherNamesExist(true);
            return enteringFio;}
        }
        if (!resultSet.isEmpty()) fioRepo.saveAll(resultSet); else familyMember.setOtherNamesExist(false);

        return resultSet;
    }

    public void changeOldFiosBirthday(FamilyMember fm) {
        for (OldFio o :
                fm.getOtherNames()) {
            o.setBirthday(fm.getBirthday());
            o.setUuid(generateUUIDFromFio(o));
            o.setFullName(generateFioStringInfo(o));
        }
    }
    public Set<OldFio> getOtherNamesByInfoId(Long id){
        return mainOtherFioRepository.findOldNamesOfPerson(id);
    }
}
