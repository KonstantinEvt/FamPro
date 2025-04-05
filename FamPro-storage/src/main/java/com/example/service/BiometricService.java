package com.example.service;

import com.example.entity.Biometric;
import com.example.entity.FamilyMemberInfo;
import com.example.repository.BiometricRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class BiometricService {
    private BiometricRepo biometricRepo;

    public void mergeBiometric(FamilyMemberInfo fmiNew,FamilyMemberInfo fmiOld) {
        if (fmiOld.getBiometric()!=null) {
        fmiOld.getBiometric().setUuid(fmiNew.getUuid());
        fmiOld.getBiometric().setHeight(fmiNew.getBiometric().getHeight());
        fmiOld.getBiometric().setWeight(fmiNew.getBiometric().getWeight());
        fmiOld.getBiometric().setHairColor(fmiNew.getBiometric().getHairColor());
        fmiOld.getBiometric().setShirtSize(fmiNew.getBiometric().getShirtSize());
        fmiOld.getBiometric().setEyesColor(fmiNew.getBiometric().getEyesColor());
        fmiOld.getBiometric().setFootSize(fmiNew.getBiometric().getFootSize());
        fmiNew.setBiometric(fmiOld.getBiometric());
    }else fmiNew.getBiometric().setUuid(fmiNew.getUuid());}
}
