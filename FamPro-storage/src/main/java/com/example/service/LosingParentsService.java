package com.example.service;

import com.example.dtos.FioDto;
import com.example.entity.FamilyMember;
import com.example.entity.LosingParent;
import com.example.entity.OldFio;
import com.example.enums.CheckStatus;
import com.example.enums.Sex;
import com.example.exceptions.FamilyMemberNotFound;
import com.example.exceptions.UncorrectedInformation;
import com.example.exceptions.UncorrectedOrNewInformation;
import com.example.mappers.FioMapper;
import com.example.mappers.LosingParensMapper;
import com.example.repository.FamilyMemberRepo;
import com.example.repository.LosingParentsRepo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@Getter
@Setter
public class LosingParentsService extends FioServiceImp<LosingParent> {
    private final LosingParentsRepo losingParentsRepo;
    private final LosingParensMapper losingParensMapper;
    private final FamilyMemberRepo familyMemberRepo;
    private final OldFioService oldFioService;

    public LosingParentsService(FioMapper fioMapper, LosingParentsRepo losingParentsRepo, LosingParensMapper losingParensMapper, FamilyMemberRepo familyMemberRepo, OldFioService oldFioService) {
        super(fioMapper);
        this.losingParentsRepo = losingParentsRepo;
        this.losingParensMapper = losingParensMapper;
        this.familyMemberRepo = familyMemberRepo;
        this.oldFioService = oldFioService;
    }

    @Transactional
    public void addLosingParent(FioDto fioDto, FamilyMember familyMember, Sex sex) {
        LosingParent losingParent = losingParensMapper.fioDtoToLosingParent(fioDto);
        UUID uuid = generateUUIDFromFio(losingParent);
        if (losingParentsRepo.findFioByUuidAndMember(uuid, familyMember).isEmpty()) {
            if (sex == Sex.MALE) losingParent.setLosingUUID(generateUUIDFromFullName(familyMember.getFatherInfo()));
            else losingParent.setLosingUUID(generateUUIDFromFullName(familyMember.getMotherInfo()));
            losingParent.setMember(familyMember);
            losingParent.setUuid(uuid);
            losingParent.setSex(sex);
            losingParent.setFullName(generateFioStringInfo(losingParent));
            losingParentsRepo.save(losingParent);
        }
    }

    @Transactional
    public void removeParentByLosingUuid(UUID uuid, FamilyMember fm) {
        losingParentsRepo.deleteByLosingUUIDAndMember(uuid, fm);
    }

    @Transactional
    public Set<FamilyMember> checkForAdditionalChilds(UUID uuid, FamilyMember familyMember) {
        Set<FamilyMember> possibleChildrenOfFamilyMember = new HashSet<>();
        List<LosingParent> possibleLosingParent = losingParentsRepo.findAllByUuid(uuid);
        if (possibleLosingParent != null && !possibleLosingParent.isEmpty()) {
            for (LosingParent losingParentForFamilyMember : possibleLosingParent) {
//тут возможны доп проверки на соответствие и право
                FamilyMember childWithLosingParent = losingParentForFamilyMember.getMember();
// тут можно побаловаться с сохраняемым инфо (на данный моммент остается информация старой записи)
                if (losingParentForFamilyMember.getSex() == Sex.MALE && familyMember.getSex() == Sex.MALE) {
                    childWithLosingParent.setFather(familyMember);
                    childWithLosingParent.setFatherInfo(losingParentForFamilyMember.getFullName());
                } else {
                    childWithLosingParent.setMother(familyMember);
                    childWithLosingParent.setMotherInfo(losingParentForFamilyMember.getFullName());
                }
                possibleChildrenOfFamilyMember.add(childWithLosingParent);
            }
            losingParentsRepo.deleteAll(possibleLosingParent);
        }
        return possibleChildrenOfFamilyMember;
    }

    @Transactional
    public void setUpFather(FioDto fioDto, FamilyMember fm) {
        if (fm.getFatherInfo() != null && fm.getFatherInfo().charAt(1) == 'A')
            removeParentByLosingUuid(generateUUIDFromFullName(fm.getFatherInfo()), fm);
        try {
            if (fioDto.getBirthday() != null && !checkDifBirthday(Sex.MALE, fioDto.getBirthday(), fm.getBirthday()))
                throw new UncorrectedInformation(CheckStatus.UNCORRECTED.getComment());
            FamilyMember father = getParentOfFamilyMember(fioDto);
            if (checkDifBirthday(Sex.MALE, father.getBirthday(), fm.getBirthday())) {
                fm.setFather(father);
                fm.setFatherInfo(father.getFullName());
                if (!father.getChilds().isEmpty()) father.setChilds(new HashSet<>());
                father.getChilds().add(fm);
            } else
                throw new UncorrectedInformation(CheckStatus.UNCORRECTED.getComment());
        } catch (FamilyMemberNotFound e) {
            log.warn(e.getMessage());
        } catch (UncorrectedOrNewInformation e) {
            fm.setFatherInfo(e.getMessage().concat(generateFioStringInfo(fioMapper.dtoToEntity(fioDto))));
        } catch (UncorrectedInformation e) {
            fm.setFatherInfo(e.getMessage());
            fm.setFather(null);
        }
        log.info("Информация об отце установлена");
    }

    @Transactional
    public void setUpMother(FioDto fioDto, FamilyMember fm) {
        if (fm.getMotherInfo() != null && fm.getMotherInfo().charAt(1) == 'A')
            removeParentByLosingUuid(generateUUIDFromFullName(fm.getMotherInfo()), fm);
        try {
            if (fioDto.getBirthday() != null && !checkDifBirthday(Sex.FEMALE, fioDto.getBirthday(), fm.getBirthday()))
                throw new UncorrectedInformation(CheckStatus.UNCORRECTED.getComment());
            FamilyMember mother = getParentOfFamilyMember(fioDto);
            if (checkDifBirthday(Sex.FEMALE, mother.getBirthday(), fm.getBirthday())) {
                fm.setMother(mother);
                fm.setMotherInfo(mother.getFullName());
                if (!mother.getChilds().isEmpty()) mother.setChilds(new HashSet<>());
                mother.getChilds().add(fm);
            } else throw new UncorrectedInformation(CheckStatus.UNCORRECTED.getComment());
        } catch (FamilyMemberNotFound e) {
            log.warn(e.getMessage());
        } catch (UncorrectedOrNewInformation e) {
            fm.setMotherInfo(e.getMessage().concat(generateFioStringInfo(fioMapper.dtoToEntity(fioDto))));
        } catch (UncorrectedInformation e) {
            fm.setMotherInfo(e.getMessage());
            fm.setMother(null);
        }
        log.info("Информация о матери установлена");
    }

    @Transactional
    public FamilyMember getParentOfFamilyMember(FioDto fio) {
        if (fio.getId() != null)
            return familyMemberRepo.findById(fio.getId()).orElseThrow(() -> new FamilyMemberNotFound("Parent not found by Id"));
        if (fio.getUuid() != null)
            return familyMemberRepo.findFioByUuid(fio.getUuid()).orElseThrow(() -> new FamilyMemberNotFound("Parent not found by UUID"));
        if (fio.getFirstName() != null
                && fio.getMiddleName() != null
                && fio.getLastName() != null
                && fio.getBirthday() != null) {
            UUID uuid = generateUUIDFromFio(fioMapper.dtoToEntity(fio));
            Optional<FamilyMember> findFM = familyMemberRepo.findFioByUuid(uuid);
            if (findFM.isPresent()) return findFM.get();
            Optional<OldFio> findFio = oldFioService.getFioRepo().findFioByUuid(uuid);
            if (findFio.isPresent()) {
                return findFio.get().getMember();
            } else
                throw new UncorrectedOrNewInformation(CheckStatus.ABSENT.getComment());
        } else
            throw new UncorrectedOrNewInformation(CheckStatus.NOT_FULLY.getComment());
    }
}

