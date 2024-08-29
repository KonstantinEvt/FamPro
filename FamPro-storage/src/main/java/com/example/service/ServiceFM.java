package com.example.service;

import com.example.dtos.FamilyMemberDto;
import com.example.entity.FamilyMember;
import com.example.entity.FamilyMemberInfo;
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

    public FamilyMemberDto getFamilyMember(Long id) {

        Optional<FamilyMember> familyMember = familyRepo.findById(id);
        FamilyMemberDto familyMemberDto = familyMemberMapper.entityToDto(familyMember.orElseThrow(() -> new FamilyMemberNotFound("Человек с ID: ".concat(String.valueOf(id)).concat(" не найден"))));
        if ((familyMember.get().getFather() != null))
            familyMemberDto.setFather_id(familyMember.get().getFather().getId());

        if ((familyMember.get().getMother() != null))
            familyMemberDto.setMother_id(familyMember.get().getMother().getId());
        return familyMemberDto;
    }

    public FamilyMemberDto addFamilyMember(FamilyMemberDto familyMemberDto) {
        FamilyMember familyMember = familyMemberMapper.dtoToEntity(familyMemberDto);
        String familiya = familyMember.getLastname();
        List<FamilyMember> familyMemberList = familyRepo.findAllByLastname(familiya);
        for (FamilyMember fm : familyMemberList) {
            if (familyMember.getFirstname().equals(fm.getFirstname()) && familyMember.getMiddlename().equals(fm.getMiddlename()) && familyMember.getBirthday().toLocalDate().equals(fm.getBirthday().toLocalDate())) {
                throw new ProblemWithId("Такой человек уже есть в базе. Если Вы хотите его отредактировать - воспользуйтесь Patch-методом. ID человека: ".concat(String.valueOf(fm.getId())));
            }
        }
        extractExtensionOfFamilyMember(familyMemberDto, familyMember);
        return familyMemberMapper.entityToDto(familyRepo.save(familyMember));
    }

    public Collection<FamilyMemberDto> getAllFamilyMembers() {
        List<FamilyMember> familyMemberList = familyRepo.findAll();
        List<FamilyMemberDto> familyMemberDtoList = new ArrayList<>();
        for (FamilyMember familyMember : familyMemberList) {
            FamilyMemberDto familyMemberDto = familyMemberMapper.entityToDto(familyMember);
            if (familyMember.getFather() != null) familyMemberDto.setFather_id(familyMember.getFather().getId());
            if (familyMember.getMother() != null) familyMemberDto.setMother_id(familyMember.getMother().getId());
            familyMemberDtoList.add(familyMemberDto);
        }
        log.info("Коллекия выдана");
        return familyMemberDtoList;
    }

    public FamilyMemberDto updateFamilyMember(FamilyMemberDto familyMemberDto) {
        Long dtoId = familyMemberDto.getId();
        if (dtoId == null) throw new ProblemWithId("Id не указан");
        Optional<FamilyMember> familyMember = familyRepo.findById(dtoId);
        FamilyMember fm = familyMember.orElseThrow(() -> new FamilyMemberNotFound("Попытка изменить человека, которого нет в базе"));
        if (familyMemberDto.getSex() != null) fm.setSex(familyMemberDto.getSex());
        if (familyMemberDto.getFirstname() != null) fm.setFirstname(familyMemberDto.getFirstname());
        if (familyMemberDto.getBirthday() != null) fm.setBirthday(familyMemberDto.getBirthday());
        if (familyMemberDto.getLastname() != null) fm.setLastname(familyMemberDto.getLastname());
        if (familyMemberDto.getMiddlename() != null) fm.setMiddlename(familyMemberDto.getMiddlename());
        extractExtensionOfFamilyMember(familyMemberDto, fm);
        familyRepo.save(fm);
        return familyMemberMapper.entityToDto(fm);
    }

    private void extractExtensionOfFamilyMember(FamilyMemberDto familyMemberDto, FamilyMember fm) {
        if (familyMemberDto.getFather_id() != null) {
            Optional<FamilyMember> father = familyRepo.findById(familyMemberDto.getFather_id());
            if (father.isPresent() && father.get().getSex() == Sex.MALE) fm.setFather(father.get());
        }
        if (familyMemberDto.getMother_id() != null) {
            Optional<FamilyMember> mother = familyRepo.findById(familyMemberDto.getMother_id());
            if (mother.isPresent() && mother.get().getSex() == Sex.FEMALE) fm.setMother(mother.get());
        }
        if (familyMemberDto.getFamilyMemberInfo() != null) {
            FamilyMemberInfo fmi = familyMemberInfoMapper.dtoToEntity(familyMemberDto.getFamilyMemberInfo());
            fm.setFamilyMemberInfo(fmi);
        }
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
                remFM.get().getFirstname(),
                remFM.get().getMiddlename(),
                remFM.get().getLastname());

    }


}

