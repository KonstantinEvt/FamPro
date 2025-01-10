package com.example.service;


import com.example.entity.Family;
import com.example.entity.ShortFamilyMember;
import com.example.repository.FamilyRepoImp;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BloodFamilyService implements FamilyService{
    FamilyRepoImp familyRepoImp;
    public Family generateFamilyFromBlood(ShortFamilyMember familyMemberParent, ShortFamilyMember familyMemberChild){
        Family family=new Family();
       return null;
    };

    @Override
    public void addFamily() {

    }
}
