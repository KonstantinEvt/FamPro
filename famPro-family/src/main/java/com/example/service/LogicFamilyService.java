package com.example.service;

import com.example.dtos.FamilyDirective;
import com.example.entity.Family;
import com.example.entity.ShortFamilyMember;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LogicFamilyService implements FamilyService{
    public void generateFamilyFromMarriage(ShortFamilyMember familyMemberParent1, ShortFamilyMember familyMemberParent2){};
    @Override
    public Family creatOrFindFamilyByInfo(String fatherInfo, String motherInfo, String uuidChild) {
return null;
    }

    @Override
    public Family creatFreeFamily(String fatherInfo, String motherInfo, String externId) {
        return null;
    }

    @Override
    public void mergeFamilies(Family donor, Family merged) {

    }

    @Override
    public void addChangesToFamilyInfo(Family family, String husbandInfo, String wifeInfo) {

    }


}
