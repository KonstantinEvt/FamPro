package com.example.service;

import com.example.dtos.FioDto;
import com.example.entity.FamilyMember;
import com.example.entity.OldFio;
import com.example.mappers.FioMapper;
import com.example.mappers.OldNamesMapper;
import com.example.repository.OldFioRepo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@Getter
@Setter
public class OldFioService extends FioServiceImp<OldFio> {
    private final OldFioRepo fioRepo;
    private final OldNamesMapper oldNamesMapper;

    public OldFioService(FioMapper fioMapper, OldFioRepo fioRepo, OldNamesMapper oldNamesMapper) {
        super(fioMapper);
        this.fioRepo = fioRepo;
        this.oldNamesMapper = oldNamesMapper;
    }

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
            if (!enteringFio.isEmpty()) fioRepo.saveAll(enteringFio);
            return enteringFio;
        }
        if (!resultSet.isEmpty()) fioRepo.saveAll(resultSet);
        return resultSet;
    }
}
