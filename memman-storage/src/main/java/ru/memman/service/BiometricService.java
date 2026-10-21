package ru.memman.service;

import ru.memman.entity.Biometric;
import ru.memman.entity.FamilyMemberInfo;
import ru.memman.repository.MainBiometricRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            Optional<Biometric> bio = fmiMergeResult.getBiometricData().stream().filter(x -> x.getAge() == age).findFirst();
            if (bio.isEmpty()) {
                fmiData.getBiometricData().get(i).setUuid(fmiData.getUuid());
                outBase.add(fmiData.getBiometricData().get(i));
            } else {
                bio.get().setUuid(fmiData.getUuid());
                bio.get().setHeight(fmiData.getBiometricData().get(i).getHeight());
                bio.get().setWeight(fmiData.getBiometricData().get(i).getWeight());
                bio.get().setHairColor(fmiData.getBiometricData().get(i).getHairColor());
                bio.get().setShirtSize(fmiData.getBiometricData().get(i).getShirtSize());
                bio.get().setEyesColor(fmiData.getBiometricData().get(i).getEyesColor());
                bio.get().setFootSize(fmiData.getBiometricData().get(i).getFootSize());
                bio.get().setDescription(fmiData.getBiometricData().get(i).getDescription());
            }
        }
        if (!outBase.isEmpty()) {mainBiometricRepository.persistAll(outBase); fmiMergeResult.getBiometricData().addAll(outBase);}
    }

    @Transactional
    public List<Biometric> getBiometricByInfoId(Long id) {
        return mainBiometricRepository.findBiometricOfPerson(id);
    }
}
