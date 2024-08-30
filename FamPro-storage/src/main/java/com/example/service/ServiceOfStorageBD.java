package com.example.service;

import com.example.dtos.FamilyMemberDto;
import com.example.entity.FamilyMember;
import com.example.mappers.FamilyMemberMapper;
import com.example.repository.FamilyRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ServiceOfStorageBD {
    private final FamilyRepo familyRepo;
    private final FamilyMemberMapper familyMemberMapper;
    private final ServiceFM serviceFM;

    public void saveDataToFile(String filename) {
        try (FileWriter fr = new FileWriter("c:/FamPro/" + filename + ".txt")) {
            Collection<FamilyMemberDto> familyMemberDtoList = serviceFM.getAllFamilyMembers();
            ObjectMapper objectMapper = new ObjectMapper();
            for (FamilyMemberDto fm : familyMemberDtoList) {
                fr.write(objectMapper.writeValueAsString(fm));
                fr.write('\n');
            }
        } catch (IOException e) {
            log.warn("file is not saved:{?}", e);
            throw new RuntimeException(e);
        }
    }

    public void recoverBaseFromFile(String filename) {
        Map<Long, FamilyMemberDto> map = new HashMap<>();
        try (BufferedReader fr = new BufferedReader(new FileReader("c:/FamPro/" + filename + ".txt"))) {
            String newFM = fr.readLine();
            ObjectMapper ss = new ObjectMapper();
            while (newFM != null) {
                FamilyMemberDto familyMemberDto = ss.readValue(newFM, FamilyMemberDto.class);
                map.put(familyMemberDto.getId(), familyMemberDto);
                newFM = fr.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Collection<FamilyMember> listFM = familyMemberMapper.collectionDtoToCollectionEntity(map.values());
        familyRepo.saveAll(listFM);
        for (FamilyMemberDto fm : map.values()) {
            FamilyMember familyMember = familyMemberMapper.dtoToEntity(fm);
            if (fm.getFatherId() != null && map.containsKey(fm.getFatherId())) {
                familyMember.setFather(familyMemberMapper.dtoToEntity(map.get(fm.getFatherId())));
            }
            if (fm.getMotherId() != null && map.containsKey(fm.getMotherId())) {
                familyMember.setMother(familyMemberMapper.dtoToEntity(map.get(fm.getMotherId())));
            }
            familyRepo.save(familyMember);
        }
    }
}

