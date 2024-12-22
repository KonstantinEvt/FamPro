package com.example.service;

import com.example.entity.PlaceBurial;
import com.example.enums.CheckStatus;
import com.example.repository.InternRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BurialService extends InternServiceImp<PlaceBurial> {

    public BurialService(@Qualifier("burialRepo") InternRepo<PlaceBurial> internRepo) {
        super(internRepo);
    }

    @Override
    public void check(PlaceBurial placeBurial) {
        if (placeBurial.getInternName() != null) {
            if (placeBurial.getCheckStatus() != CheckStatus.CHECKED) {
                if (placeBurial.getTechString() == null) placeBurial.setTechString("ONE USER");
                super.check(placeBurial);
            }
        } else placeBurial.setTechString("uncorrected");
    }

    public String resolveFullAddress(PlaceBurial placeBurial) {
        return String.join(", ", placeBurial.getCountry(), placeBurial.getCity(), placeBurial.getStreet(), placeBurial.getCemetery(), placeBurial.getChapter(), placeBurial.getSquare(), placeBurial.getGrave());
    }
}