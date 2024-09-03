package com.example.service;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.FioDto;
import com.example.entity.*;
import com.example.mappers.FamilyMemberInfoMapper;
import com.example.mappers.FamilyMemberMapper;
import com.example.repository.FamilyRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ServiceOfStorageBD {
    private final FamilyRepo familyRepo;
    private final FamilyMemberMapper familyMemberMapper;
    private final FamilyMemberInfoMapper familyMemberInfoMapper;
    private final EntityManager entityManager;
@Transactional(readOnly = true)
    public void saveDataToFile(String filename) {


    List<FamilyMember> resultList= entityManager.createQuery("from FamilyMember a left join fetch a.familyMemberInfo b left join fetch b.phones left join fetch b.addresses left join fetch b.emails left join fetch b.mainPhone left join fetch b.mainAddress left join fetch b.mainEmail"
       , FamilyMember.class).getResultList();

    try (FileWriter fr = new FileWriter("c:/Family/" + filename + ".txt")) {
            ObjectMapper objectMapper = new ObjectMapper();

            for (FamilyMember fm : resultList) {
                FamilyMemberDto fmD=familyMemberMapper.entityToDto(fm);
                if (fm.getFather()!=null) fmD.setFatherFio(new FioDto(fm.getFather().getUuid(), fm.getFather().getFirstName(),fm.getFather().getLastName(),fm.getFather().getMiddleName(),fm.getFather().getBirthday()));
                if (fm.getMother()!=null) fmD.setMotherFio(new FioDto(fm.getMother().getUuid(), fm.getMother().getFirstName(),fm.getMother().getLastName(),fm.getMother().getMiddleName(),fm.getMother().getBirthday()));
                if (fm.getFamilyMemberInfo()!=null) fmD.setMemberInfo(familyMemberInfoMapper.entityToDto(fm.getFamilyMemberInfo()));
                fr.write(objectMapper.writeValueAsString(fmD));
                fr.write('\n');
            }
        } catch (IOException e) {
            log.warn("file is not saved:{1}", e);
            throw new RuntimeException(e);
        }
    }
@Transactional
    public void recoverBaseFromFile(String filename) {
        Map<UUID, FamilyMemberDto> map = new HashMap<>();
        try (BufferedReader fr = new BufferedReader(new FileReader("c:/Family/" + filename + ".txt"))) {
            String newFM = fr.readLine();
            ObjectMapper ss = new ObjectMapper();
            while (newFM != null) {
                FamilyMemberDto familyMemberDto = ss.readValue(newFM, FamilyMemberDto.class);
                map.put(familyMemberDto.getUuid(), familyMemberDto);
                newFM = fr.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Collection<FamilyMember> listFM = familyMemberMapper.collectionDtoToCollectionEntity(map.values());


        familyRepo.saveAll(listFM);
        for (FamilyMemberDto fm : map.values()) {
            FamilyMember familyMember = familyMemberMapper.dtoToEntity(fm);
            if (fm.getFatherFio() != null && map.containsKey(fm.getFatherFio().getUuid())) {
                familyMember.setFather(familyMemberMapper.dtoToEntity(map.get(fm.getFatherFio().getUuid())));
            }
            if (fm.getMotherFio() != null && map.containsKey(fm.getMotherFio().getUuid())) {
                familyMember.setMother(familyMemberMapper.dtoToEntity(map.get(fm.getMotherFio().getUuid())));
            }
            if (fm.getMemberInfo()!=null){
                familyMember.setFamilyMemberInfo(familyMemberInfoMapper.dtoToEntity(fm.getMemberInfo()));
            }
            familyRepo.save(familyMember);
        }
    }
}

