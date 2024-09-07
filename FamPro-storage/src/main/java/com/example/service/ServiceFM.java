package com.example.service;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.FioDto;
import com.example.entity.*;
import com.example.enums.Sex;
import com.example.exceptions.ProblemWithId;
import com.example.exceptions.FamilyMemberNotFound;
import com.example.exceptions.UncorrectedInformation;
import com.example.mappers.*;
import com.example.repository.FamilyRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ServiceFM {
    private final FamilyMemberMapper familyMemberMapper;
    private final FamilyRepo familyRepo;
    private final FamilyMemberInfoService familyMemberInfoService;

    public FamilyMemberDto getFamilyMember(Long id) {

        FamilyMember familyMember = familyRepo.findById(id)
                .orElseThrow(() -> new FamilyMemberNotFound("Человек с ID: ".concat(String.valueOf(id)).concat(" не найден")));
        FamilyMemberDto familyMemberDto = familyMemberMapper.entityToDto(familyMember);
        if (familyMember.getFamilyMemberInfo() != null) {
            familyMemberDto.setMemberInfo(familyMemberInfoService.getMemberInfo(familyMember));
        }
        return familyMemberDto;
    }

    public FamilyMemberDto addFamilyMember(FamilyMemberDto familyMemberDto) {
        log.info("--------ВНОСИМ НОВОГО ЧЕЛОВЕКА-------");
        if (familyMemberDto.getId() != null) throw new ProblemWithId("Удалите ID нового человека");

        FamilyMember familyMember = familyMemberMapper.dtoToEntity(familyMemberDto);
        familyMemberDto.setUuid(generateUUID(familyMember));
        familyMember.setUuid(familyMemberDto.getUuid());
        Optional<FamilyMember> fm = familyRepo.findFamilyMemberByUuid(familyMemberDto.getUuid());
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
            familyMemberDtoList.add(familyMemberMapper.entityToDto(familyMember));
        }
        log.info("Коллекия всех людей из базы выдана");
        return familyMemberDtoList;
    }

    public FamilyMemberDto updateFamilyMember(FamilyMemberDto familyMemberDto) {
        log.info("--------ИЗМЕНЯЕМ ЧЕЛОВЕКА-------");
        Long dtoId = familyMemberDto.getId();
        if (dtoId == null) throw new ProblemWithId("Id не указан");
        checkFio(familyMemberDto);
        FamilyMember fm = familyRepo.findById(dtoId).orElseThrow(() -> new FamilyMemberNotFound("Попытка изменить человека, которого нет в базе"));

        Set<FamilyMember> childrenOfFamilyMember;
        if (fm.getSex() == Sex.MALE)
            childrenOfFamilyMember = familyRepo.findAllByFatherInfo(generateFamilyMemberStringInfo(fm));
        else childrenOfFamilyMember = familyRepo.findAllByMotherInfo(generateFamilyMemberStringInfo(fm));

        if (familyMemberDto.getFirstName() != null) fm.setFirstName(familyMemberDto.getFirstName());
        if (familyMemberDto.getBirthday() != null) fm.setBirthday(familyMemberDto.getBirthday());
        if (familyMemberDto.getLastName() != null) fm.setLastName(familyMemberDto.getLastName());
        if (familyMemberDto.getMiddleName() != null) fm.setMiddleName(familyMemberDto.getMiddleName());
        if (familyMemberDto.getSex() != null && childrenOfFamilyMember.isEmpty())
            fm.setSex(familyMemberDto.getSex());
        else if (familyMemberDto.getSex() != null && familyMemberDto.getSex() != fm.getSex()) {
            throw new UncorrectedInformation("Изменить пол человека, у которого в базе имеются дети, невозможно");
        }
        familyMemberDto.setUuid(generateUUID(fm));


        if (!childrenOfFamilyMember.isEmpty()) {
            String infoParents = generateFamilyMemberStringInfo(fm);
            if (fm.getSex() == Sex.MALE) {
                for (FamilyMember child : childrenOfFamilyMember) child.setFatherInfo(infoParents);
            } else for (FamilyMember child : childrenOfFamilyMember) child.setMotherInfo(infoParents);
        }
        log.info("Первичная информация установлена");
        extractExtensionOfFamilyMember(familyMemberDto, fm);
        fm.setUuid(familyMemberDto.getUuid());
        familyRepo.save(fm);
        return familyMemberMapper.entityToDto(fm);
    }

    private UUID generateUUID(FamilyMember familyMember) {
        String str = familyMember.getFirstName().strip()
                .concat(familyMember.getMiddleName().strip())
                .concat(familyMember.getLastName().strip())
                .concat(String.valueOf(familyMember.getBirthday().toLocalDate())).toLowerCase()
                .concat("Rainbow");
        log.info("новый UUID человека сгенерирован");
        return UUID.nameUUIDFromBytes(str.getBytes());
    }

    public void checkFio(FioDto familyMemberDto) {

    }

    public String generateFamilyMemberStringInfo(FamilyMember familyMember) {
        return String.join(" ", familyMember.getFirstName(), familyMember.getMiddleName(), familyMember.getLastName(), ". Дата рождения: ", String.valueOf(familyMember.getBirthday().toLocalDate()));
    }

    private void extractExtensionOfFamilyMember(FamilyMemberDto familyMemberDto, FamilyMember fm) {
        if (familyMemberDto.getFatherFio() != null) {
            checkFio(familyMemberDto.getFatherFio());
            FamilyMember father = getParentOfFamilyMember(familyMemberDto.getFatherFio());
            fm.setFather(father);
            fm.setFatherInfo(generateFamilyMemberStringInfo(father));
            log.info("Информация об отце установлена");
        }
        if (familyMemberDto.getMotherFio() != null) {
            checkFio(familyMemberDto.getMotherFio());
            FamilyMember mother = getParentOfFamilyMember(familyMemberDto.getMotherFio());
            fm.setMother(mother);
            fm.setMotherInfo(generateFamilyMemberStringInfo(mother));
            log.info("Информация о матери установлена");
        }
        if (familyMemberDto.getMemberInfo() != null) {
            familyMemberDto.getMemberInfo().setId(null);
            fm.setFamilyMemberInfo(familyMemberInfoService.merge(familyMemberDto, fm.getUuid()));
        }
        log.info("Расширенная информация проверена и установлена");
    }

    private FamilyMember getParentOfFamilyMember(FioDto parentDto) {
        if (parentDto.getId() != null)
            return familyRepo.findById(parentDto.getId()).orElseThrow(() -> new FamilyMemberNotFound("Родитель по Id не найден"));
        if (parentDto.getUuid() == null
                && parentDto.getFirstName() != null
                && parentDto.getMiddleName() != null
                && parentDto.getLastName() != null
                && parentDto.getBirthday() != null) {
            parentDto.setUuid(generateUUID(FamilyMember.builder()
                    .firstName(parentDto.getFirstName())
                    .middleName(parentDto.getMiddleName())
                    .lastName(parentDto.getLastName())
                    .birthday(parentDto.getBirthday())
                    .build()));
        }
        if (parentDto.getUuid() != null) {
            Optional<FamilyMember> parent = familyRepo.findFamilyMemberByUuid(parentDto.getUuid());
            if (parent.isPresent()) return parent.get();
            else throw new UncorrectedInformation("Информация о родителе не соответствует базе.");
        } else throw new UncorrectedInformation("Информация о родителе не полны. Данные игнорированы");
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

