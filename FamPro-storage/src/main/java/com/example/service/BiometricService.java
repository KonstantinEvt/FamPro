package com.example.service;

import com.example.entity.Address;
import com.example.entity.Biometric;
import com.example.entity.FamilyMemberInfo;
import com.example.repository.BiometricRepo;
import com.example.repository.MainBiometricRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class BiometricService {
    private MainBiometricRepository mainBiometricRepository;
@Transactional
    public void mergeBiometric(FamilyMemberInfo fmiData, FamilyMemberInfo fmiMergeResult) {

        if (fmiMergeResult.getBiometricData() == null || fmiMergeResult.getBiometricData().isEmpty())
            fmiMergeResult.setBiometricData(List.of(new Biometric()));
        fmiMergeResult.getBiometricData().get(0).setUuid(fmiData.getUuid());
        if (fmiData.getBiometricData().get(0).getHeight() != 0)
            fmiMergeResult.getBiometricData().get(0).setHeight(fmiData.getBiometricData().get(0).getHeight());

        if (fmiData.getBiometricData().get(0).getWeight() != 0)
            fmiMergeResult.getBiometricData().get(0).setWeight(fmiData.getBiometricData().get(0).getWeight());

        if (fmiData.getBiometricData().get(0).getHairColor() != null)
            fmiMergeResult.getBiometricData().get(0).setHairColor(fmiData.getBiometricData().get(0).getHairColor());

        if (fmiData.getBiometricData().get(0).getShirtSize() != 0)
            fmiMergeResult.getBiometricData().get(0).setShirtSize(fmiData.getBiometricData().get(0).getShirtSize());

        if (fmiData.getBiometricData().get(0).getEyesColor() != null)
            fmiMergeResult.getBiometricData().get(0).setEyesColor(fmiData.getBiometricData().get(0).getEyesColor());

        if (fmiData.getBiometricData().get(0).getFootSize() != 0)
            fmiMergeResult.getBiometricData().get(0).setFootSize(fmiData.getBiometricData().get(0).getFootSize());
    }
    @Transactional
    public List<Biometric> getBiometricByInfoId(Long id){
        return mainBiometricRepository.findBiometricOfPerson(id);
    }
}
