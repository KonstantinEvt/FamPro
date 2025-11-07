package com.example.service;

import com.example.entity.Description;
import com.example.entity.FamilyMemberInfo;
import com.example.repository.MainDescriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DescriptionService {
    private MainDescriptionRepository mainDescriptionRepository;

    public void mergeDescription(FamilyMemberInfo fmiData, FamilyMemberInfo fmiMergeResult) {

        if (fmiMergeResult.getDescriptionData() == null || fmiMergeResult.getDescriptionData().isEmpty())
            fmiMergeResult.setDescriptionData(List.of(new Description()));
        fmiMergeResult.getDescriptionData().get(0).setUuid(fmiData.getUuid());
        if (fmiData.getDescriptionData().get(0).getCommon() != null)
            fmiMergeResult.getDescriptionData().get(0).setCommon(fmiData.getDescriptionData().get(0).getCommon());

        if (fmiData.getDescriptionData().get(0).getEducation() != null)
            fmiMergeResult.getDescriptionData().get(0).setEducation(fmiData.getDescriptionData().get(0).getEducation());

        if (fmiData.getDescriptionData().get(0).getProfession() != null)
            fmiMergeResult.getDescriptionData().get(0).setProfession(fmiData.getDescriptionData().get(0).getProfession());
    }
    public List<Description> getDescriptionByInfoId(Long id){
        return mainDescriptionRepository.findDescriptionOfPerson(id);
    }
}
