package com.example.service;

import com.example.dtos.FamilyMemberDto;
import com.example.feign.FamilyMemberClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProbeServ {
    private final FamilyMemberClient familyMemberClient;
    public FamilyMemberDto getFamilyMem(Long id){
        return familyMemberClient.getFamilyMember(id);
    }
}
