package com.family.fampro.service;

import com.family.fampro.dto.FamilyMemberDto;
import com.family.fampro.entity.FamilyMember;
import com.family.fampro.mapper.FamilyMemberMapper;
import com.family.fampro.repository.FamilyRepo;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ServiceFM {
    private final FamilyMemberMapper familyMemberMapper;
    private final FamilyRepo familyRepo;
    EntityManager entityManager;

    public FamilyMemberDto getFamilyMember(Long id) {
        Optional<FamilyMember> familyMember = familyRepo.findById(id);
        return familyMemberMapper.entityToDto(familyMember.orElseGet(() ->
                FamilyMember.builder()
                        .firstname("unknown")
                        .lastname("unknown")
                        .middlename("unknown")
                        .build()));
    }

    public FamilyMemberDto saveNewFamilyMember(FamilyMemberDto familyMemberDto) {
        return familyMemberMapper.entityToDto(familyRepo.save(familyMemberMapper.dtoToEntity(familyMemberDto)));
    }

    public Collection<FamilyMemberDto> getAllFamilyMembers() {
        log.info("Коллекия выдана");       //    Map<String,Object> property=Map.of(GraphSemantic.LOAD.getJakartaHintName(),entityManager.getEntityGraph("ListOfOne"));
        return familyMemberMapper.collectionEntityToCollectionDto(familyRepo.findAll());
    }

    public FamilyMemberDto updateFamilyMember(FamilyMemberDto familyMemberDto) {

        return familyMemberMapper.entityToDto(familyRepo.save(familyMemberMapper.updateEntity(familyMemberDto, familyRepo.findById(familyMemberDto.getId()).get())));
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

    public void saveDataToFile(String filename) {
        try (FileWriter fr = new FileWriter(new File("c:/FamPro"+filename+".txt"))) {
            List<FamilyMember> list=familyRepo.findAll();
            ObjectMapper objectMapper=new ObjectMapper();
            for (FamilyMember fm:list) {
                fr.write(objectMapper.writeValueAsString(fm));
                fr.write('\n');
            }
        } catch (IOException e) {
            log.warn("file is not saved:%s",e);
            throw new RuntimeException(e);
        }
    }
    public void recoverBaseFromFile(String filename){
        List<FamilyMember> list=new ArrayList<>();
        try (BufferedReader fr=new BufferedReader(new FileReader(new File(filename)))){
            String newFM=fr.readLine();

            System.out.println(newFM);
            ObjectMapper ss = new ObjectMapper();
            while (newFM!=null) {
                list.add(ss.readValue(newFM, FamilyMember.class));
                newFM=fr.readLine();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        familyRepo.saveAll(list);
    }

}

