package com.example.service;

import com.example.dtos.FamilyMemberDto;
import com.example.entity.*;
import com.example.mappers.FamilyMemberInfoMapper;
import com.example.mappers.FamilyMemberMapper;
import com.example.repository.FamilyMemberRepo;
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
    private final FamilyMemberRepo familyMemberRepo;
    private final FamilyMemberMapper familyMemberMapper;
    private final FamilyMemberInfoMapper familyMemberInfoMapper;
    private final EntityManager entityManager;
    private final FamilyMemberService familyMemberService;

    @Transactional(readOnly = true)
    public void saveDataToFile(String filename) {


        List<FamilyMember> resultList = entityManager.createQuery("from FamilyMember a left join fetch a.familyMemberInfo b left join fetch a.father y  left join fetch a.mother x left join fetch a.otherNames n left join fetch b.phones left join fetch b.addresses left join fetch b.emails"
                , FamilyMember.class).getResultList();

        try (FileWriter fr = new FileWriter("c:/Family/" + filename + ".txt")) {
            ObjectMapper objectMapper = new ObjectMapper();

            for (FamilyMember fm : resultList) {
                FamilyMemberDto fmD = familyMemberMapper.entityToDto(fm);
                if (fm.getFamilyMemberInfo() != null)
                    fmD.setMemberInfo(familyMemberInfoMapper.entityToDto(fm.getFamilyMemberInfo()));
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
        Set<FamilyMemberDto> set = new HashSet<>();
        try (BufferedReader fr = new BufferedReader(new FileReader("c:/Family/" + filename + ".txt"))) {
            String newFM = fr.readLine();
            ObjectMapper ss = new ObjectMapper();
            while (newFM != null) {
                FamilyMemberDto familyMemberDto = ss.readValue(newFM, FamilyMemberDto.class);
                familyMemberDto.setId(null);
                if (familyMemberDto.getMemberInfo()!=null) familyMemberDto.getMemberInfo().setId(null);
               set.add(familyMemberDto);
                newFM = fr.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Collection<FamilyMember> listFM = familyMemberMapper.collectionDtoToCollectionEntity(set);
        familyMemberRepo.saveAll(listFM);

        for (FamilyMemberDto fm : set) {
            familyMemberService.updateFamilyMember(fm);
        }
    }
}

