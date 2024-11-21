package com.example.checks;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.FioDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.HashSet;

@Component
@AllArgsConstructor
public class CheckFamilyMember {
    CommonWordChecks commonWordChecks;

    public FamilyMemberDto check(FamilyMemberDto familyMemberDto) {
        FioDto che = checkFio(familyMemberDto);
        FamilyMemberDto resultFM = new FamilyMemberDto();
        resultFM.setFirstName(che.getFirstName());
        resultFM.setMiddleName(che.getMiddleName());
        resultFM.setLastName(che.getLastName());
        resultFM.setBirthday(che.getBirthday());
        resultFM.setId(familyMemberDto.getId());
        resultFM.setLocalisation(familyMemberDto.getLocalisation());
        resultFM.setMemberInfo(familyMemberDto.getMemberInfo());
        if (familyMemberDto.getSex() != null) resultFM.setSex(familyMemberDto.getSex());
        if (familyMemberDto.getMotherFio() != null) resultFM.setMotherFio(checkFio(familyMemberDto.getMotherFio()));
        if (familyMemberDto.getFatherFio() != null) resultFM.setFatherFio(checkFio(familyMemberDto.getFatherFio()));
        if (familyMemberDto.getDeathday() != null) if (familyMemberDto.getBirthday() != null
                && (familyMemberDto.getDeathday().after(familyMemberDto.getBirthday())))
            resultFM.setDeathday(familyMemberDto.getDeathday());
// тут надо расписать Инфо мембера
        if (familyMemberDto.getMemberInfo() != null) resultFM.setMemberInfo(familyMemberDto.getMemberInfo());
        if (familyMemberDto.getFioDtos() != null) {
            resultFM.setFioDtos(new HashSet<>());
            for (FioDto fioDto :
                    familyMemberDto.getFioDtos())
                resultFM.getFioDtos().add(checkFio(fioDto));

        }
        return resultFM;
    }

    public FioDto checkFio(FioDto fioDto) {
        FioDto resultFM = new FioDto();
        if (fioDto.getFirstName() != null) {
            resultFM.setFirstName(commonWordChecks.checkForBlanks(fioDto.getFirstName()));
            if (resultFM.getFirstName() != null) {
                commonWordChecks.checkForSwears(resultFM.getFirstName());
                resultFM.setFirstName(resultFM.getFirstName().toLowerCase());
            }
        }
        if (fioDto.getMiddleName() != null) {
            resultFM.setMiddleName(commonWordChecks.checkForBlanks(fioDto.getMiddleName()));
            if (resultFM.getMiddleName() != null) {
                commonWordChecks.checkForSwears(resultFM.getMiddleName());
                resultFM.setMiddleName(resultFM.getMiddleName().toLowerCase());
            }
        }
        if (fioDto.getLastName() != null) {
            resultFM.setLastName(commonWordChecks.checkForBlanks(fioDto.getLastName()));
            if (resultFM.getLastName() != null) {
                commonWordChecks.checkForSwears(resultFM.getLastName());
                resultFM.setLastName(resultFM.getLastName().toLowerCase());
            }
        }
        resultFM.setBirthday(fioDto.getBirthday());
        resultFM.setId(fioDto.getId());
        return resultFM;

    }
}

