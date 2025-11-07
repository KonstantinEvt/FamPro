package com.example.service;

import com.example.entity.FamilyMemberInfo;
import com.example.entity.PlaceBurial;
import com.example.enums.CheckStatus;
import com.example.repository.InternRepo;
import com.example.repository.MainBurialRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BurialService extends InternServiceImp<PlaceBurial> {
    public final MainBurialRepository mainBurialRepository;

    public BurialService(@Qualifier("burialRepo") InternRepo<PlaceBurial> internRepo, MainBurialRepository mainBurialRepository) {
        super(internRepo);
        this.mainBurialRepository = mainBurialRepository;
    }

    @Override
    public void check(PlaceBurial placeBurial) {
        if ((placeBurial.getInternName() == null && placeBurial.getCheckStatus() == CheckStatus.UNCHECKED)
                || placeBurial.getCheckStatus() == CheckStatus.CHECKED) {
            if (placeBurial.getTechString() == null) placeBurial.setTechString("ONE USER");
            super.check(placeBurial);
        } else placeBurial.setTechString("uncorrected");
    }

    @Override
    public void checkMergeAndSetUp(FamilyMemberInfo fmiData, FamilyMemberInfo fmiMergeResult) {
        PlaceBurial placeBurial;
        PlaceBurial placeBurialData = fmiData.getBurialPlace().get(0);
        if (fmiMergeResult.getBurialPlace() == null || fmiMergeResult.getBurialPlace().isEmpty()) {
            placeBurial = new PlaceBurial();
            check(placeBurial);
            fmiMergeResult.setBurialPlace(List.of(placeBurial));
        } else placeBurial = fmiMergeResult.getBurialPlace().get(0);

        placeBurial.setUuid(fmiData.getUuid());
        if (placeBurialData.getSquare() != null)
            placeBurial.setSquare(placeBurialData.getSquare());
        if (placeBurialData.getCity() != null)
            placeBurial.setCity(placeBurialData.getCity());
        if (placeBurialData.getCountry() != null)
            placeBurial.setCountry(placeBurialData.getCountry());
        if (placeBurialData.getRegion() != null)
            placeBurial.setRegion(placeBurialData.getRegion());
        if (placeBurialData.getStreet() != null)
            placeBurial.setStreet(placeBurialData.getStreet());
        if (placeBurialData.getCemetery() != null)
            placeBurial.setCemetery(placeBurialData.getCemetery());
        if (placeBurialData.getChapter() != null)
            placeBurial.setChapter(placeBurialData.getChapter());
        if (placeBurialData.getGrave() != null)
            placeBurial.setGrave(placeBurialData.getGrave());

        placeBurial.setInternName(resolveFullAddress(placeBurial));
        if (placeBurialData.getAssignment() != null)
            placeBurial.setAssignment(placeBurialData.getAssignment());
        if (placeBurialData.getCheckStatus() != null)
            placeBurial.setCheckStatus(placeBurialData.getCheckStatus());
        if (placeBurialData.getDescription() != null)
            placeBurial.setDescription(placeBurialData.getDescription());
        if (placeBurialData.getWorkStatus() != null)
            placeBurial.setWorkStatus(placeBurialData.getWorkStatus());
        if (placeBurialData.getTechString() != null)
            placeBurial.setTechString(placeBurialData.getTechString());

        placeBurial.setPhotoExist(placeBurialData.isPhotoExist());
        fmiMergeResult.setPhotoBirthExist(placeBurialData.isPhotoExist());

    }

    public String resolveFullAddress(PlaceBurial placeBurial) {
        return String.join(", ", placeBurial.getCountry(), placeBurial.getCity(), placeBurial.getStreet(), placeBurial.getCemetery(), placeBurial.getChapter(), placeBurial.getSquare(), placeBurial.getGrave());
    }

    public List<PlaceBurial> getPlaceBurialByInfoId(Long id) {
        return mainBurialRepository.findPlaceBurialOfPerson(id);
    }
}