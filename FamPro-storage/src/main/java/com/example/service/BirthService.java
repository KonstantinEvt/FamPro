package com.example.service;

import com.example.entity.FamilyMemberInfo;
import com.example.entity.PlaceBirth;
import com.example.enums.CheckStatus;
import com.example.repository.InternRepo;
import com.example.repository.MainBirthRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class BirthService extends InternServiceImp<PlaceBirth> {
    public final MainBirthRepository mainBirthRepository;

    public BirthService(@Qualifier("birthRepo") InternRepo<PlaceBirth> internRepo, MainBirthRepository mainBirthRepository) {
        super(internRepo);
        this.mainBirthRepository = mainBirthRepository;
    }

    @Override
    public void check(PlaceBirth placeBirth) {
        if ((placeBirth.getInternName() == null && placeBirth.getCheckStatus() == CheckStatus.UNCHECKED)
                || placeBirth.getCheckStatus() == CheckStatus.CHECKED) {
            if (placeBirth.getTechString() == null) placeBirth.setTechString("ONE USER");
            super.check(placeBirth);
        } else placeBirth.setTechString("uncorrected");
    }

    @Override
    public void checkMergeAndSetUp(FamilyMemberInfo fmiData, FamilyMemberInfo fmiMergeResult) {
        PlaceBirth placeBirth;
        PlaceBirth birthPlaceData = fmiData.getBirthPlace().get(0);
        if (fmiMergeResult.getBirthPlace() == null || fmiMergeResult.getBirthPlace().isEmpty()) {
            placeBirth = new PlaceBirth();
            check(placeBirth);
            fmiMergeResult.setBirthPlace(List.of(placeBirth));
        } else placeBirth = fmiMergeResult.getBirthPlace().get(0);

        placeBirth.setUuid(fmiData.getUuid());
        if (birthPlaceData.getBirthHouse() != null)
            placeBirth.setBirthHouse(birthPlaceData.getBirthHouse());
        if (birthPlaceData.getCity() != null)
            placeBirth.setCity(birthPlaceData.getCity());
        if (birthPlaceData.getCountry() != null)
            placeBirth.setCountry(birthPlaceData.getCountry());
        if (birthPlaceData.getRegion() != null)
            placeBirth.setRegion(birthPlaceData.getRegion());
        if (birthPlaceData.getStreet() != null)
            placeBirth.setStreet(birthPlaceData.getStreet());
        if (birthPlaceData.getRegistration() != null)
            placeBirth.setRegistration(birthPlaceData.getRegistration());

        placeBirth.setInternName(resolveFullAddress(placeBirth));

        if (birthPlaceData.getAssignment() != null)
            placeBirth.setAssignment(birthPlaceData.getAssignment());
        if (birthPlaceData.getCheckStatus() != null)
            placeBirth.setCheckStatus(birthPlaceData.getCheckStatus());
        if (birthPlaceData.getDescription() != null)
            placeBirth.setDescription(birthPlaceData.getDescription());
        if (birthPlaceData.getWorkStatus() != null)
            placeBirth.setWorkStatus(birthPlaceData.getWorkStatus());
        if (birthPlaceData.getTechString() != null)
            placeBirth.setTechString(birthPlaceData.getTechString());

        placeBirth.setPhotoExist(birthPlaceData.isPhotoExist());
        fmiMergeResult.setPhotoBirthExist(birthPlaceData.isPhotoExist());
    }

    public String resolveFullAddress(PlaceBirth placeBirth) {
        return String.join(", ", placeBirth.getCountry(), placeBirth.getRegion(), placeBirth.getCity(), placeBirth.getStreet(), placeBirth.getBirthHouse(), placeBirth.getRegistration());
    }

    public List<PlaceBirth> getPlaceBirthByInfoId(Long id) {
        return mainBirthRepository.findPlaceBirthOfPerson(id);
    }
}