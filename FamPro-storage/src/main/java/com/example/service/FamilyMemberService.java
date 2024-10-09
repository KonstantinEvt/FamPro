package com.example.service;


import com.example.dtos.FamilyMemberDto;
import com.example.dtos.FioDto;
import com.example.entity.FamilyMember;
import com.example.entity.OldFio;
import com.example.enums.CheckStatus;
import com.example.enums.Sex;
import com.example.exceptions.FamilyMemberNotFound;
import com.example.exceptions.ProblemWithId;
import com.example.exceptions.UncorrectedInformation;
import com.example.exceptions.UncorrectedOrNewInformation;
import com.example.mappers.FamilyMemberMapper;
import com.example.mappers.FioMapper;
import com.example.repository.FamilyMemberRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j

public class FamilyMemberService extends FioServiceImp<FamilyMember> {
    private final FamilyMemberMapper familyMemberMapper;
    private final FamilyMemberInfoService familyMemberInfoService;
    private final LosingParentsService losingParentsService;
    private final OldFioService oldFioService;
    private final FamilyMemberRepo familyMemberRepo;

    public FamilyMemberService(FioMapper fioMapper, FamilyMemberMapper familyMemberMapper, FamilyMemberInfoService familyMemberInfoService, LosingParentsService losingParentsService, OldFioService oldFioService, FamilyMemberRepo familyMemberRepo) {
        super(fioMapper);
        this.familyMemberMapper = familyMemberMapper;
        this.familyMemberInfoService = familyMemberInfoService;
        this.losingParentsService = losingParentsService;
        this.oldFioService = oldFioService;
        this.familyMemberRepo = familyMemberRepo;
    }

    @Transactional(readOnly = true)
    public FamilyMemberDto getFamilyMember(Long id) {
        FamilyMember familyMember = familyMemberRepo.findById(id)
                .orElseThrow(() -> new FamilyMemberNotFound("Человек с ID: ".concat(String.valueOf(id)).concat(" не найден")));
        FamilyMemberDto familyMemberDto = familyMemberMapper.entityToDto(familyMember);
        if (familyMember.getFamilyMemberInfo() != null) {
            familyMemberDto.setMemberInfo(familyMemberInfoService.getMemberInfo(familyMember));
        }
        return familyMemberDto;
    }

    @Transactional
    public FamilyMemberDto addFamilyMember(FamilyMemberDto familyMemberDto) {
        log.info("--------ВНОСИМ НОВОГО ЧЕЛОВЕКА-------");
        if (familyMemberDto.getId() != null) throw new ProblemWithId("Удалите ID нового человека");

        FamilyMember familyMember = familyMemberMapper.dtoToEntity(familyMemberDto);
        familyMemberDto.setUuid(generateUUIDFromFio(familyMember));
        familyMember.setUuid(familyMemberDto.getUuid());
        Optional<FamilyMember> fm = familyMemberRepo.findFioByUuid(familyMemberDto.getUuid());
        if (fm.isPresent()) {
            throw new ProblemWithId("Такой человек уже есть в базе. Если Вы хотите его отредактировать - воспользуйтесь Patch-методом. ID человека: ".concat(String.valueOf(fm.get().getId())));
        }
        log.info("Первичная информация установлена");

        extractExtensionOfFamilyMember(familyMemberDto, familyMember);
        familyMemberRepo.save(familyMember);
        addChangingToBase(familyMemberDto, familyMember);
        return familyMemberMapper.entityToDto(familyMemberRepo.save(familyMember));
    }


    @Transactional(readOnly = true)
    public Collection<FamilyMemberDto> getAllFamilyMembers() {
        List<FamilyMember> familyMemberList = familyMemberRepo.findAll();
        List<FamilyMemberDto> familyMemberDtoList = new ArrayList<>();
        for (FamilyMember familyMember : familyMemberList) {
            familyMemberDtoList.add(familyMemberMapper.entityToDto(familyMember));
        }
        log.info("Коллекция всех людей из базы выдана");
        return familyMemberDtoList;
    }

    @Transactional
    public FamilyMemberDto updateFamilyMember(FamilyMemberDto familyMemberDto) {
        log.info("--------ИЗМЕНЯЕМ ЧЕЛОВЕКА-------");
        Long dtoId = familyMemberDto.getId();
        FamilyMember fm;
        if (dtoId != null) {
            fm = familyMemberRepo.findById(dtoId).orElseThrow(() ->
                    new FamilyMemberNotFound("Попытка изменить человека по Id, которого нет в базе"));
        } else if (familyMemberDto.getUuid() != null) {
            fm = familyMemberRepo.findFioByUuid(familyMemberDto.getUuid()).orElseThrow(() ->
                    new FamilyMemberNotFound("Попытка изменить человека по UUID, которого нет в базе"));
        } else throw new ProblemWithId("Ни Id, ни UUID не указан для поиска/изменения человека");
        Set<FamilyMember> currentChildrenOfFamilyMember = fm.getChilds();
// нужно учесть смену дня рождения в Альтернативных именах
        if (familyMemberDto.getFirstName() != null) fm.setFirstName(familyMemberDto.getFirstName());
        if (familyMemberDto.getBirthday() != null) fm.setBirthday(familyMemberDto.getBirthday());
        if (familyMemberDto.getLastName() != null) fm.setLastName(familyMemberDto.getLastName());
        if (familyMemberDto.getMiddleName() != null) fm.setMiddleName(familyMemberDto.getMiddleName());
        if (familyMemberDto.getSex() != null && (currentChildrenOfFamilyMember == null || currentChildrenOfFamilyMember.isEmpty()))
            fm.setSex(familyMemberDto.getSex());
        else if (familyMemberDto.getSex() != null && familyMemberDto.getSex() != fm.getSex()) {
            throw new UncorrectedInformation("Изменить пол человека, у которого в базе имеются подтвержденные дети, невозможно");
        }
        familyMemberDto.setUuid(generateUUIDFromFio(fm));
        log.info("Первичная информация установлена");

        extractExtensionOfFamilyMember(familyMemberDto, fm);

        fm.setUuid(familyMemberDto.getUuid());
        familyMemberRepo.save(fm);
        addChangingToBase(familyMemberDto, fm);
        if (fm.getChilds() != null && !fm.getChilds().isEmpty()) {
            if (currentChildrenOfFamilyMember != null && !currentChildrenOfFamilyMember.isEmpty())
                fm.getChilds().addAll(currentChildrenOfFamilyMember);
        } else fm.setChilds(currentChildrenOfFamilyMember);
        if (fm.getChilds() != null) addChangesInParensInfo(fm.getChilds(), fm);
        familyMemberRepo.save(fm);
        return familyMemberMapper.entityToDto(fm);
    }


    public void checkOldNamesForAdditionalChilds(Set<FamilyMember> childrenOfFamilyMember, Set<OldFio> oldFios, FamilyMember familyMember) {
        if (childrenOfFamilyMember == null) childrenOfFamilyMember = new HashSet<>();
        for (OldFio oldFio : oldFios) {
            childrenOfFamilyMember.addAll(losingParentsService.checkForAdditionalChilds(oldFio.getUuid(), familyMember));
        }
    }

    public void addChangesInParensInfo(Set<FamilyMember> setOfChilds, FamilyMember fm) {

        if (fm.getSex() == Sex.MALE) {
            for (FamilyMember child : setOfChilds) {
                child.setFatherInfo(fm.getFullName());
                child.setFather(fm);
                familyMemberRepo.save(child);
            }
        } else for (FamilyMember child : setOfChilds) {
            child.setMotherInfo(fm.getFullName());
            child.setMother(fm);
            familyMemberRepo.save(child);
        }
    }

    private void extractExtensionOfFamilyMember(FamilyMemberDto familyMemberDto, FamilyMember fm) {
        fm.setFullName(generateFioStringInfo(fm));
        if (familyMemberDto.getFatherFio() != null) {
            if (fm.getFatherInfo() != null && fm.getFatherInfo().charAt(1) == 'A')
                losingParentsService.removeParentByLosingUuid(generateUUIDFromFullName(fm.getFatherInfo()));
            try {
                FamilyMember father = getParentOfFamilyMember(familyMemberDto.getFatherFio());
                fm.setFather(father);
                fm.setFatherInfo(father.getFullName());
            } catch (FamilyMemberNotFound e) {
                log.warn(e.getMessage());
            } catch (UncorrectedOrNewInformation | UncorrectedInformation e) {
                fm.setFatherInfo(e.getMessage().concat(generateFioStringInfo(fioMapper.dtoToEntity(familyMemberDto.getFatherFio()))));
            }
            log.info("Информация об отце установлена");
        }
        if (familyMemberDto.getMotherFio() != null) {
            if (fm.getMotherInfo() != null && fm.getMotherInfo().charAt(1) == 'A')
                losingParentsService.removeParentByLosingUuid(generateUUIDFromFullName(fm.getMotherInfo()));
            try {
                FamilyMember mother = getParentOfFamilyMember(familyMemberDto.getMotherFio());
                fm.setMother(mother);
                fm.setMotherInfo(mother.getFullName());
            } catch (FamilyMemberNotFound e) {
                log.warn(e.getMessage());
            } catch (UncorrectedOrNewInformation | UncorrectedInformation e) {
                fm.setMotherInfo(e.getMessage().concat(generateFioStringInfo(fioMapper.dtoToEntity(familyMemberDto.getMotherFio()))));
            }
            log.info("Информация о матери установлена");
        }
        if (familyMemberDto.getMemberInfo() != null) {
            familyMemberDto.getMemberInfo().setId(null);
            fm.setFamilyMemberInfo(familyMemberInfoService.merge(familyMemberDto, fm.getUuid()));
        }
        log.info("Расширенная информация проверена и установлена");
    }

    private FamilyMember getParentOfFamilyMember(FioDto fio) {

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
            throw new UncorrectedInformation(CheckStatus.NOT_FULLY.getComment());
    }

    //remove must be change
    public String removeFamilyMember(Long id) {
        Optional<FamilyMember> remFM = familyMemberRepo.findById(id);
        if (remFM.isEmpty())
            throw new FamilyMemberNotFound("Человек с ID:".concat(String.valueOf(id)).concat(" не найден"));
        if (remFM.get().getSex() == Sex.MALE) {
            List<FamilyMember> link1 = familyMemberRepo.findAllByFather_Id(id);
            for (FamilyMember fm : link1
            ) {
                fm.setFather(null);
            }
        }
        if (remFM.get().getSex() == Sex.FEMALE) {
            List<FamilyMember> link2 = familyMemberRepo.findAllByMother_Id(id);
            for (FamilyMember fm : link2
            ) {
                fm.setMother(null);
            }
        }
        familyMemberRepo.deleteById(id);
        return String.format("Человек: %s %s %s удален, из базы",
                remFM.get().getFirstName(),
                remFM.get().getMiddleName(),
                remFM.get().getLastName());
    }

    public void addChangingToBase(FamilyMemberDto familyMemberDto, FamilyMember familyMember) {
        Set<FamilyMember> childrenOfFamilyMember = losingParentsService.checkForAdditionalChilds(familyMember.getUuid(), familyMember);
        if (familyMemberDto.getFioDtos() != null) {
            Set<OldFio> oldFios = oldFioService.addAllNewOldNames(familyMemberDto.getFioDtos(), familyMember);
            if (oldFios != null) {
                checkOldNamesForAdditionalChilds(childrenOfFamilyMember, oldFios, familyMember);
                if (familyMember.getOtherNames() != null) familyMember.getOtherNames().addAll(oldFios);
                else familyMember.setOtherNames(oldFios);
            }
        }
        if (childrenOfFamilyMember != null) familyMember.setChilds(childrenOfFamilyMember);

        if (familyMember.getFather() == null &&
                familyMemberDto.getFatherFio() != null &&
                familyMember.getFatherInfo().charAt(1) == 'A') {
            losingParentsService.addLosingParent(familyMemberDto.getFatherFio(), familyMember, Sex.MALE);
        }
        if (familyMember.getMother() == null &&
                familyMemberDto.getMotherFio() != null &&
                familyMember.getMotherInfo().charAt(1) == 'A') {
            losingParentsService.addLosingParent(familyMemberDto.getMotherFio(), familyMember, Sex.FEMALE);
        }
    }
}

