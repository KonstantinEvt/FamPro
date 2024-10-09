package com.example.service;

import com.example.dtos.FioDto;
import com.example.entity.FamilyMember;
import com.example.entity.LosingParent;
import com.example.enums.Sex;
import com.example.mappers.FioMapper;
import com.example.mappers.LosingParensMapper;
import com.example.repository.LosingParentsRepo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@Getter
@Setter
public class LosingParentsService extends FioServiceImp<LosingParent> {
    private final LosingParentsRepo fioRepo;
    private final LosingParensMapper losingParensMapper;

    public LosingParentsService(FioMapper fioMapper, LosingParentsRepo fioRepo, LosingParensMapper losingParensMapper) {
        super(fioMapper);
        this.fioRepo = fioRepo;
        this.losingParensMapper = losingParensMapper;
    }

    public void addLosingParent(FioDto fioDto, FamilyMember familyMember, Sex sex) {
        LosingParent losingParent = losingParensMapper.fioDtoToLosingParent(fioDto);
        UUID uuid = generateUUIDFromFio(losingParent);
        if (fioRepo.findFioByUuidAndMember(uuid, familyMember).isEmpty()) {
            if (sex == Sex.MALE) losingParent.setLosingUUID(generateUUIDFromFullName(familyMember.getFatherInfo()));
            else losingParent.setLosingUUID(generateUUIDFromFullName(familyMember.getMotherInfo()));
            losingParent.setMember(familyMember);
            losingParent.setUuid(uuid);
            losingParent.setSex(sex);
            losingParent.setFullName(generateFioStringInfo(losingParent));
            fioRepo.save(losingParent);
        }
    }

    public void removeParentById(Long id) {
        fioRepo.deleteById(id);
    }

    public void removeParentByLosingUuid(UUID uuid) {
        fioRepo.deleteByLosingUUID(uuid);
    }

    public Set<FamilyMember> checkForAdditionalChilds(UUID uuid, FamilyMember familyMember) {
        Set<FamilyMember> possibleChildrenOfFamilyMember = new HashSet<>();
        List<LosingParent> possibleLosingParent = fioRepo.findAllByUuid(uuid);
        if (possibleLosingParent != null && !possibleLosingParent.isEmpty()) {
            for (LosingParent losingParentForFamilyMember : possibleLosingParent) {
//тут возможны доп проверки на соответствие и право
                FamilyMember childWithLosingParent = losingParentForFamilyMember.getMember();
// тут можно побаловаться с сохраняемым инфо (на данный моммент остается информация старой записи)
                if (losingParentForFamilyMember.getSex() == Sex.MALE && familyMember.getSex() == Sex.MALE) {
                    childWithLosingParent.setFather(familyMember);
                    childWithLosingParent.setFatherInfo(losingParentForFamilyMember.getFullName());
                } else childWithLosingParent.setMother(familyMember);
                childWithLosingParent.setMotherInfo(losingParentForFamilyMember.getFullName());
                possibleChildrenOfFamilyMember.add(childWithLosingParent);
            }
            fioRepo.deleteAll(possibleLosingParent);
        }
        return possibleChildrenOfFamilyMember;
    }
}

