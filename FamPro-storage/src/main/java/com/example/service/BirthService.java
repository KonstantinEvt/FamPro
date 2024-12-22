package com.example.service;

import com.example.entity.PlaceBirth;
import com.example.enums.CheckStatus;
import com.example.repository.InternRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BirthService extends InternServiceImp<PlaceBirth> {

    public BirthService(@Qualifier("birthRepo") InternRepo<PlaceBirth> internRepo) {
        super(internRepo);
    }

    @Override
    public void check(PlaceBirth placeBirth) {
        if (placeBirth.getInternName() != null) {
            if (placeBirth.getCheckStatus() != CheckStatus.CHECKED) {
                if (placeBirth.getTechString() == null) placeBirth.setTechString("ONE USER");
                super.check(placeBirth);
            }
        } else placeBirth.setTechString("uncorrected");
    }

     public String resolveFullAddress(PlaceBirth placeBirth) {
        return String.join(", ", placeBirth.getCountry(), placeBirth.getCity(), placeBirth.getStreet(), placeBirth.getBithHouse(), placeBirth.getRegistration());

    }
}