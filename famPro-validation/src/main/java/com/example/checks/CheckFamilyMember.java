package com.example.checks;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.FioDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor
public class CheckFamilyMember {
    CommonWordChecks commonWordChecks;

    public void check(FamilyMemberDto familyMemberDto) {
        checkFio(familyMemberDto);
        if (familyMemberDto.getMotherFio() != null) if (!checkFio(familyMemberDto.getMotherFio())) familyMemberDto.setMotherFio(null);
        if (familyMemberDto.getFatherFio() != null) if (!checkFio(familyMemberDto.getFatherFio())) familyMemberDto.setFatherFio(null);
        if (familyMemberDto.getDeathday() != null) if (familyMemberDto.getBirthday() != null
                && (familyMemberDto.getDeathday().before(familyMemberDto.getBirthday())))
            familyMemberDto.setDeathday(null);
// тут надо расписать Инфо мембера

        if (familyMemberDto.getFioDtos() != null) {
            Set<FioDto> oldNames=new HashSet<>();
            for (FioDto fioDto :
                    familyMemberDto.getFioDtos())
                if (checkFio(fioDto)) oldNames.add(fioDto);
            System.out.println(oldNames);
            if (oldNames.isEmpty()) familyMemberDto.setFioDtos(null); else familyMemberDto.setFioDtos(oldNames);
        }
    }

    public boolean checkFio(FioDto fioDto) {
        boolean enable = false;
        if (fioDto.getFirstName() != null) {
            fioDto.setFirstName(commonWordChecks.checkForBlanks(fioDto.getFirstName()));
            if (fioDto.getFirstName() != null) {
                commonWordChecks.checkForSwears(fioDto.getFirstName());
                fioDto.setFirstName(fioDto.getFirstName().toLowerCase());
                enable = true;
            }
        }
        if (fioDto.getMiddleName() != null) {
            fioDto.setMiddleName(commonWordChecks.checkForBlanks(fioDto.getMiddleName()));
            if (fioDto.getMiddleName() != null) {
                commonWordChecks.checkForSwears(fioDto.getMiddleName());
                fioDto.setMiddleName(fioDto.getMiddleName().toLowerCase());
                enable = true;
            }
        }
        if (fioDto.getLastName() != null) {
            fioDto.setLastName(commonWordChecks.checkForBlanks(fioDto.getLastName()));
            if (fioDto.getLastName() != null) {
                commonWordChecks.checkForSwears(fioDto.getLastName());
                fioDto.setLastName(fioDto.getLastName().toLowerCase());
                enable = true;
            }
        }
        if (fioDto.getId()!=null) enable=true;
        if (fioDto.getUuid()!=null) enable=true;
        System.out.println(fioDto);
        return  enable;
    }
}

