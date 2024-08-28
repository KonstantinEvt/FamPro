package com.example.famprometa.service;

import com.example.dtos.FamilyMemberDto;
import com.example.famprometa.feign.FamilyMemberClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProbeServ {
    private final FamilyMemberClient familyMemberClient;
    public  FamilyMemberDto getFamilyMem(Long id){
        return familyMemberClient.getFamilyMember(id);
    }
}
