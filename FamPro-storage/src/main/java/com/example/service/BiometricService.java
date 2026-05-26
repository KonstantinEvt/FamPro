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
        if (fmiMergeResult.getBiometricData() == null || fmiMergeResult.getBiometricData().isEmpty())
            fmiMergeResult.setBiometricData(new ArrayList<>());
        for (int i = 0; i < ages; i++) {
            if (fmiMergeResult.getBiometricData().size() < i + 1)
                fmiMergeResult.getBiometricData().add(new Biometric());

            fmiMergeResult.getBiometricData().get(i).setUuid(fmiData.getUuid());
            if (fmiData.getBiometricData().get(i).getAge() != 0)
                fmiMergeResult.getBiometricData().get(i).setAge(fmiData.getBiometricData().get(i).getAge());
            if (fmiData.getBiometricData().get(i).getHeight() != 0)
                fmiMergeResult.getBiometricData().get(i).setHeight(fmiData.getBiometricData().get(i).getHeight());

            if (fmiData.getBiometricData().get(i).getWeight() != 0)
                fmiMergeResult.getBiometricData().get(i).setWeight(fmiData.getBiometricData().get(i).getWeight());

            if (fmiData.getBiometricData().get(i).getHairColor() != null)
                fmiMergeResult.getBiometricData().get(i).setHairColor(fmiData.getBiometricData().get(i).getHairColor());

            if (fmiData.getBiometricData().get(i).getShirtSize() != 0)
                fmiMergeResult.getBiometricData().get(i).setShirtSize(fmiData.getBiometricData().get(i).getShirtSize());

            if (fmiData.getBiometricData().get(i).getEyesColor() != null)
                fmiMergeResult.getBiometricData().get(i).setEyesColor(fmiData.getBiometricData().get(i).getEyesColor());

            if (fmiData.getBiometricData().get(i).getFootSize() != 0)
                fmiMergeResult.getBiometricData().get(i).setFootSize(fmiData.getBiometricData().get(i).getFootSize());
            if (fmiData.getBiometricData().get(i).getDescription() != null && !fmiData.getBiometricData().get(i).getDescription().isBlank())
                fmiMergeResult.getBiometricData().get(i).setDescription(fmiData.getBiometricData().get(i).getDescription());
        }
    }

    @Transactional
    public List<Biometric> getBiometricByInfoId(Long id) {
        return mainBiometricRepository.findBiometricOfPerson(id);
    }
}
