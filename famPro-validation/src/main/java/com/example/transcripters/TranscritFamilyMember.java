package com.example.transcripters;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.FioDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@AllArgsConstructor
public class TranscritFamilyMember {
    TranscriterHolder transcriterHolder;

    public FamilyMemberDto to(FamilyMemberDto familyMemberDto) {
        FioDto che = transcritToFio(familyMemberDto);
        FamilyMemberDto resultFM = new FamilyMemberDto();
        resultFM.setFirstName(che.getFirstName());
        resultFM.setMiddleName(che.getMiddleName());
        resultFM.setLastName(che.getLastName());
        resultFM.setBirthday(che.getBirthday());
        resultFM.setId(familyMemberDto.getId());
        resultFM.setMemberInfo(familyMemberDto.getMemberInfo());
        resultFM.setLocalisation(familyMemberDto.getLocalisation());
        resultFM.setDeathday(familyMemberDto.getDeathday());

        if (familyMemberDto.getSex() != null) resultFM.setSex(familyMemberDto.getSex());
        if (familyMemberDto.getMotherFio() != null)
            resultFM.setMotherFio(transcritToFio(familyMemberDto.getMotherFio()));
        if (familyMemberDto.getFatherFio() != null)
            resultFM.setFatherFio(transcritToFio(familyMemberDto.getFatherFio()));

        if (familyMemberDto.getFioDtos() != null) {
            resultFM.setFioDtos(new HashSet<>());
            for (FioDto fioDto :
                    familyMemberDto.getFioDtos())
                resultFM.getFioDtos().add(transcritToFio(fioDto));
        }
        return resultFM;
    }

    public FioDto transcritToFio(FioDto fioDto) {
        FioDto resultFM = new FioDto();
        if (fioDto.getFirstName() != null) {
            resultFM.setFirstName(transcriterHolder.getTransctriter().transcritWordToLocalisation(fioDto.getFirstName()));
        }
        if (fioDto.getMiddleName() != null) {
            resultFM.setMiddleName(transcriterHolder.getTransctriter().transcritWordToLocalisation(fioDto.getMiddleName()));
        }
        if (fioDto.getLastName() != null) {
            resultFM.setLastName(transcriterHolder.getTransctriter().transcritWordToLocalisation(fioDto.getLastName()));
        }
        resultFM.setBirthday(fioDto.getBirthday());
        resultFM.setId(fioDto.getId());
        return resultFM;
    }

    public FamilyMemberDto from(FamilyMemberDto familyMemberDto) {
        FioDto che = transcritFromFio(familyMemberDto);
        FamilyMemberDto resultFM = new FamilyMemberDto();
        resultFM.setFirstName(che.getFirstName());
        resultFM.setMiddleName(che.getMiddleName());
        resultFM.setLastName(che.getLastName());
        resultFM.setBirthday(che.getBirthday());
        resultFM.setId(familyMemberDto.getId());
        resultFM.setMemberInfo(familyMemberDto.getMemberInfo());
        resultFM.setLocalisation(familyMemberDto.getLocalisation());
        resultFM.setDeathday(familyMemberDto.getDeathday());

        if (familyMemberDto.getSex() != null) resultFM.setSex(familyMemberDto.getSex());
        if (familyMemberDto.getMotherFio() != null)
            resultFM.setMotherFio(transcritFromFio(familyMemberDto.getMotherFio()));
        if (familyMemberDto.getFatherFio() != null)
            resultFM.setFatherFio(transcritFromFio(familyMemberDto.getFatherFio()));

        if (familyMemberDto.getFioDtos() != null) {
            resultFM.setFioDtos(new HashSet<>());
            for (FioDto fioDto :
                    familyMemberDto.getFioDtos())
                resultFM.getFioDtos().add(transcritFromFio(fioDto));
        }
        return resultFM;
    }

    public FioDto transcritFromFio(FioDto fioDto) {
        FioDto resultFM = new FioDto();
        if (fioDto.getFirstName() != null) {
            resultFM.setFirstName(transcriterHolder.getTransctriter().transcritWordFromLocalisation(fioDto.getFirstName()));
        }
        if (fioDto.getMiddleName() != null) {
            resultFM.setMiddleName(transcriterHolder.getTransctriter().transcritWordFromLocalisation(fioDto.getMiddleName()));
        }
        if (fioDto.getLastName() != null) {
            resultFM.setLastName(transcriterHolder.getTransctriter().transcritWordFromLocalisation(fioDto.getLastName()));
        }
        resultFM.setBirthday(fioDto.getBirthday());
        resultFM.setId(fioDto.getId());
        return resultFM;
    }
}