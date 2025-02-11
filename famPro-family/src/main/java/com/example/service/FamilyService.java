package com.example.service;

import com.example.dtos.FamilyDirective;
import com.example.entity.Family;

import java.util.LinkedList;

public interface FamilyService {
    public Family creatOrFindFamilyByInfo(String fatherInfo, String motherInfo, String uuidChild);
    public Family creatFreeFamily(String fatherInfo, String motherInfo, String externId);
    public void mergeFamilies(Family donor, Family merged);
    public void addChangesToFamilyInfo(Family family,String husbandInfo,String
            wifeInfo);
}
