package com.example.service;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.FioDto;
import com.example.entity.*;
import com.example.enums.Sex;
import com.example.exceptions.ProblemWithId;
import com.example.exceptions.FamilyMemberNotFound;
import com.example.mappers.*;
import com.example.repository.FamilyRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ServiceFM {
    private final FamilyMemberMapper familyMemberMapper;
    private final FamilyMemberInfoMapper familyMemberInfoMapper;
    private final FamilyRepo familyRepo;
    private final FamilyMemberInfoService familyMemberInfoService;

    public FamilyMemberDto getFamilyMember(Long id) {

        Optional<FamilyMember> familyMember = familyRepo.findById(id);
        FamilyMemberDto familyMemberDto = familyMemberMapper.entityToDto(familyMember.orElseThrow(() -> new FamilyMemberNotFound("Человек с ID: ".concat(String.valueOf(id)).concat(" не найден"))));
        if ((familyMember.get().getFather() != null)) {
            familyMemberDto.setFatherFio(new FioDto(familyMember.get().getFather().getUuid(), familyMember.get().getFather().getFirstName(), familyMember.get().getFather().getLastName(), familyMember.get().getFather().getMiddleName(), familyMember.get().getFather().getBirthday()));
            log.info("Отец установлен");
        }

        if ((familyMember.get().getMother() != null)) {
            familyMemberDto.setMotherFio(new FioDto(familyMember.get().getMother().getUuid(), familyMember.get().getMother().getFirstName(), familyMember.get().getMother().getLastName(), familyMember.get().getMother().getMiddleName(), familyMember.get().getMother().getBirthday()));
            log.info("Мать установлена");

        }
        if (familyMember.get().getFamilyMemberInfo() != null) {
            familyMemberDto.setMemberInfo(familyMemberInfoMapper.entityToDto(familyMember.get().getFamilyMemberInfo()));
        }
        return familyMemberDto;
    }

    public FamilyMemberDto addFamilyMember(FamilyMemberDto familyMemberDto) {
        if (familyMemberDto.getId() != null) throw new ProblemWithId("Удалите ID нового человека");
        FamilyMember familyMember = familyMemberMapper.dtoToEntity(familyMemberDto);
        familyMember.setUuid(genarateUUID(familyMember));
        familyMemberDto.getMemberInfo().setUuid(familyMember.getUuid());
        Optional<FamilyMember> fm = familyRepo.findFamilyMemberByUuid(familyMember.getUuid());
        if (fm.isPresent()) {
            throw new ProblemWithId("Такой человек уже есть в базе. Если Вы хотите его отредактировать - воспользуйтесь Patch-методом. ID человека: ".concat(String.valueOf(fm.get().getId())));
        }
        log.info("Первичная информация установлена");
        extractExtensionOfFamilyMember(familyMemberDto, familyMember);
        return familyMemberMapper.entityToDto(familyRepo.save(familyMember));
    }

    public Collection<FamilyMemberDto> getAllFamilyMembers() {
        List<FamilyMember> familyMemberList = familyRepo.findAll();
        List<FamilyMemberDto> familyMemberDtoList = new ArrayList<>();
        for (FamilyMember familyMember : familyMemberList) {
            FamilyMemberDto familyMemberDto = familyMemberMapper.entityToDto(familyMember);
            if (familyMember.getFather() != null)
                familyMemberDto.setFatherFio(new FioDto(familyMember.getFather().getUuid(), familyMember.getFather().getFirstName(), familyMember.getFather().getLastName(), familyMember.getFather().getMiddleName(), familyMember.getFather().getBirthday()));
            if (familyMember.getMother() != null)
                familyMemberDto.setMotherFio(new FioDto(familyMember.getMother().getUuid(), familyMember.getMother().getFirstName(), familyMember.getMother().getLastName(), familyMember.getMother().getMiddleName(), familyMember.getMother().getBirthday()));
            familyMemberDtoList.add(familyMemberDto);
        }
        log.info("Коллекия всех людей из базы выдана");
        return familyMemberDtoList;
    }

    public FamilyMemberDto updateFamilyMember(FamilyMemberDto familyMemberDto) {
        Long dtoId = familyMemberDto.getId();
        if (dtoId == null) throw new ProblemWithId("Id не указан");
        Optional<FamilyMember> familyMember = familyRepo.findById(dtoId);
        FamilyMember fm = familyMember.orElseThrow(() -> new FamilyMemberNotFound("Попытка изменить человека, которого нет в базе"));
        if (familyMemberDto.getSex() != null) fm.setSex(familyMemberDto.getSex());
        if (familyMemberDto.getFirstName() != null) fm.setFirstName(familyMemberDto.getFirstName());
        if (familyMemberDto.getBirthday() != null) fm.setBirthday(familyMemberDto.getBirthday());
        if (familyMemberDto.getLastName() != null) fm.setLastName(familyMemberDto.getLastName());
        if (familyMemberDto.getMiddleName() != null) fm.setMiddleName(familyMemberDto.getMiddleName());
        fm.setUuid(genarateUUID(fm));
        log.info("Первичная информация установлена");
        extractExtensionOfFamilyMember(familyMemberDto, fm);

        familyRepo.save(fm);
        return familyMemberMapper.entityToDto(fm);
    }

    private UUID genarateUUID(FamilyMember familyMember) {
        String str = familyMember.getFirstName().strip()
                .concat(familyMember.getMiddleName().strip())
                .concat(familyMember.getLastName().strip())
                .concat(String.valueOf(familyMember.getBirthday().toLocalDate())).toLowerCase();
        log.info("новый UUID человека сгенерирован");
        return UUID.nameUUIDFromBytes(str.getBytes());
    }

    private void extractExtensionOfFamilyMember(FamilyMemberDto familyMemberDto, FamilyMember fm) {
        if (familyMemberDto.getFatherFio() != null) {
            Optional<FamilyMember> father = familyRepo.findFamilyMemberByUuid(familyMemberDto.getFatherFio().getUuid());
            if (father.isPresent() && father.get().getSex() == Sex.MALE) fm.setFather(father.get());
            else log.warn("Предъявляенное fatherId не соответствует базе. Данная позиция игнорирована");
        }
        if (familyMemberDto.getMotherFio() != null) {
            Optional<FamilyMember> mother = familyRepo.findFamilyMemberByUuid(familyMemberDto.getMotherFio().getUuid());
            if (mother.isPresent() && mother.get().getSex() == Sex.FEMALE) fm.setMother(mother.get());
            else log.warn("Предъявляенное motherId не соответствует базе. Данная позиция игнорирована");
        }
        if (familyMemberDto.getMemberInfo() != null) {
            familyMemberDto.getMemberInfo().setId(null) ;
            fm.setFamilyMemberInfo(familyMemberInfoService.merge(familyMemberDto));
        }
        log.info("Расширенная информация проверена и установлена");
    }

    public String removeFamilyMember(Long id) {
        Optional<FamilyMember> remFM = familyRepo.findById(id);
        if (remFM.isEmpty())
            throw new FamilyMemberNotFound("Человек с ID:".concat(String.valueOf(id)).concat(" не найден"));
        if (remFM.get().getSex() == Sex.MALE) {
            List<FamilyMember> link1 = familyRepo.findAllByFather_Id(id);
            for (FamilyMember fm : link1
            ) {
                fm.setFather(null);
            }
        }
        if (remFM.get().getSex() == Sex.FEMALE) {
            List<FamilyMember> link2 = familyRepo.findAllByMother_Id(id);
            for (FamilyMember fm : link2
            ) {
                fm.setMother(null);
            }
        }
        familyRepo.deleteById(id);
        return String.format("Человек: %s %s %s удален, из базы",
                remFM.get().getFirstName(),
                remFM.get().getMiddleName(),
                remFM.get().getLastName());
    }
}

