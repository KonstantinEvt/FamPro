package com.example.service;

import com.example.entity.Family;
import com.example.entity.FamilyMember;
import com.example.repository.FamilyRepoImp;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BloodFamilyService implements FamilyService{
    FamilyRepoImp familyRepoImp;
    public Family generateFamilyFromBlood(FamilyMember familyMemberParent, FamilyMember familyMemberChild){
        Family family=new Family();
       return null;
    };

    @Override
    public void addFamily() {

    }
}
