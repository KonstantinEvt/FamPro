package com.example.service;

import com.example.dto.FamilyMemberDto;
import com.example.entity.FamilyMember;
import com.example.mapper.FamilyMemberMapper;
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
    private final FamilyRepo familyRepo;
    static final FamilyMember unknown = FamilyMember.builder()
            .id(-1L)
            .firstname("unknown")
            .lastname("unknown")
            .middlename("unknown")
            .build();

    public FamilyMemberDto getFamilyMember(Long id) {

        Optional<FamilyMember> familyMember = familyRepo.findById(id);
        FamilyMemberDto familyMemberDto = familyMemberMapper.entityToDto(familyMember.orElse(unknown));
        if ((familyMember.orElse(unknown).getFather() != null))
            familyMemberDto.setFather_id(familyMember.orElse(unknown).getFather().getId());

        if ((familyMember.orElse(unknown).getMother() != null))
            familyMemberDto.setMother_id(familyMember.orElse(unknown).getMother().getId());

        return familyMemberDto;
    }

    public FamilyMemberDto saveNewFamilyMember(FamilyMemberDto familyMemberDto) {
        FamilyMember familyMember=familyMemberMapper.dtoToEntity(familyMemberDto);
        Optional<FamilyMember> father = familyRepo.findById(familyMemberDto.getFather_id());
        if (father.isPresent() && father.get().getSex()) familyMember.setFather(father.get());

        if (familyMemberDto.getMother_id()!=null) {
        Optional<FamilyMember> mother = familyRepo.findById(familyMemberDto.getMother_id());
        if (mother.isPresent() && mother.get().getSex()) familyMember.setMother(mother.get());
    }
        return familyMemberMapper.entityToDto(familyRepo.save(familyMember));
    }

    public Collection<FamilyMemberDto> getAllFamilyMembers() {
        log.info("Коллекия выдана");
        List<FamilyMember> familyMemberList = familyRepo.findAll();
        List<FamilyMemberDto> familyMemberDtoList = new ArrayList<>();
        for (FamilyMember familyMember : familyMemberList) {
            FamilyMemberDto familyMemberDto = familyMemberMapper.entityToDto(familyMember);
            if (familyMember.getFather() != null) familyMemberDto.setFather_id(familyMember.getFather().getId());
            if (familyMember.getMother() != null) familyMemberDto.setMother_id(familyMember.getMother().getId());
            familyMemberDtoList.add(familyMemberDto);
        }
        return familyMemberDtoList;
    }

    public FamilyMemberDto updateFamilyMember(FamilyMemberDto familyMemberDto)  {
        Long dtoId = familyMemberDto.getId();
        if (dtoId == null) return familyMemberMapper.entityToDto(unknown);
        Optional<FamilyMember> familyMember = familyRepo.findById(dtoId);
        if (familyMember.isEmpty()) return familyMemberMapper.entityToDto(unknown);
        FamilyMember fm=familyMember.get();
        if (familyMemberDto.getSex()!=null) fm.setSex(familyMemberDto.getSex());
        if (familyMemberDto.getFirstname()!=null) fm.setFirstname(familyMemberDto.getFirstname());
        if (familyMemberDto.getBirthday()!=null) fm.setBirthday(familyMemberDto.getBirthday());
        if (familyMemberDto.getLastname()!=null) fm.setLastname(familyMemberDto.getLastname());
        if (familyMemberDto.getMiddlename()!=null) fm.setMiddlename(familyMemberDto.getMiddlename());
        if (familyMemberDto.getFather_id()!=null) {
            Optional<FamilyMember> father = familyRepo.findById(familyMemberDto.getFather_id());
            if (father.isPresent() && father.get().getSex()) fm.setFather(father.get());
        }
        if (familyMemberDto.getMother_id()!=null) {
            Optional<FamilyMember> mother = familyRepo.findById(familyMemberDto.getMother_id());
            if (mother.isPresent() && mother.get().getSex()) fm.setMother(mother.get());
        }
        familyRepo.save(fm);
        return familyMemberMapper.entityToDto(fm);
    }

    public String removeFamilyMember(Long id) {
        Optional<FamilyMember> remFM = familyRepo.findById(id);
        if (remFM.isPresent() && remFM.get().getSex()) {
            Set<FamilyMember> link1 = familyRepo.findAllByFather_Id(id);
            for (FamilyMember fm : link1
            ) {
                fm.setFather(null);
            }
        }
        if (remFM.isPresent() && !remFM.get().getSex()) {
            Set<FamilyMember> link2 = familyRepo.findAllByMother_Id(id);
            for (FamilyMember fm : link2
            ) {
                fm.setMother(null);
            }
        }
        if (remFM.isPresent()) {
            familyRepo.deleteById(id);
            return String.format("Член семьи: %s %s %s удален",
                    remFM.get().getFirstname(),
                    remFM.get().getMiddlename(),
                    remFM.get().getLastname());
        } else return "Член семьи не найден";
    }


}

