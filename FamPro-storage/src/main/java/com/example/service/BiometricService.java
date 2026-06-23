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
        int ages = fmiData.getBiometricData().size();
        List<Biometric> outBase = new ArrayList<>();
        if (fmiMergeResult.getBiometricData() == null) fmiMergeResult.setBiometricData(new ArrayList<>());
        for (int i = 0; i < ages; i++) {
            int age = fmiData.getBiometricData().get(i).getAge();
            Biometric bio = fmiMergeResult.getBiometricData().stream().filter(x -> x.getAge() == age).findFirst().orElse(null);
            if (bio == null) {fmiData.getBiometricData().get(i).setUuid(fmiMergeResult.getUuid());outBase.add(fmiData.getBiometricData().get(i));}
            else {
                bio.setHeight(fmiData.getBiometricData().get(i).getHeight());
                bio.setWeight(fmiData.getBiometricData().get(i).getWeight());
                bio.setHairColor(fmiData.getBiometricData().get(i).getHairColor());
                bio.setShirtSize(fmiData.getBiometricData().get(i).getShirtSize());
                bio.setEyesColor(fmiData.getBiometricData().get(i).getEyesColor());
                bio.setFootSize(fmiData.getBiometricData().get(i).getFootSize());
                bio.setDescription(fmiData.getBiometricData().get(i).getDescription());
            }
        }
        if (!outBase.isEmpty()) fmiMergeResult.getBiometricData().addAll(outBase);
    }

    @Transactional
    public List<Biometric> getBiometricByInfoId(Long id) {
        return mainBiometricRepository.findBiometricOfPerson(id);
    }
}
