package com.example.transcriters;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.FioDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TranscritFamilyMember {
    TranscriterHolder transcriterHolder;

    public void to(FamilyMemberDto familyMemberDto) {
        transcritToFio(familyMemberDto);

        if (familyMemberDto.getMotherFio() != null)
            transcritToFio(familyMemberDto.getMotherFio());
        if (familyMemberDto.getFatherFio() != null) transcritToFio(familyMemberDto.getFatherFio());

        if (familyMemberDto.getFioDtos() != null) {
            for (FioDto fioDto :
                    familyMemberDto.getFioDtos())
                transcritToFio(fioDto);
        }
    }

    public void toGet(FamilyMemberDto familyMemberDto) {
        familyMemberDto.setFullName(parseFullName(familyMemberDto.getFullName()));
//        if (familyMemberDto.getMotherInfo() != null)
        familyMemberDto.setMotherInfo(parseFullName(familyMemberDto.getMotherInfo()));
//        if (familyMemberDto.getFatherInfo() != null)
        familyMemberDto.setFatherInfo(parseFullName(familyMemberDto.getFatherInfo()));
    }

    private String parseFullName(String str) {
        String result;
        AbstractTranscriter trans = transcriterHolder.getTransctriter();
        if (str == null || str.isEmpty()) return trans.getOut();
        if (str.charAt(0) == '(') {
            if (str.charAt(1) == 'A') {
                String str1 = str.substring(17);
                String[] strings = str1.split(" ");
                result = String.join(" ", trans.getAbsent(), setUpperFirst(trans.transcritWordToLocalisation(strings[0])),
                        setUpperFirst(trans.transcritWordToLocalisation(strings[1])),
                        setUpperFirst(trans.transcritWordToLocalisation(strings[2])),
                        trans.getBirth(), strings[6]);
            } else if (str.charAt(1) == 'I') {
                String str1 = str.substring(17);
                String[] strings = str1.split(" ");
                result = String.join(" ", trans.getInfoNotFully(), strings[0].equals("null") ? trans.empty() : setUpperFirst(trans.transcritWordToLocalisation(strings[0])),
                        strings[1].equals("null") ? trans.empty() : setUpperFirst(trans.transcritWordToLocalisation(strings[1])),
                        strings[2].equals("null") ? trans.empty() : setUpperFirst(trans.transcritWordToLocalisation(strings[2])),
                        trans.getBirth(),
                        strings[6].equals("null") ? trans.empty() : strings[6]);
            } else result = trans.getIncorrectInfo();
        } else {
            String[] strings = str.split(" ");
            result = String.join(" ", setUpperFirst(trans.transcritWordToLocalisation(strings[0])),
                    setUpperFirst(trans.transcritWordToLocalisation(strings[1])),
                    setUpperFirst(trans.transcritWordToLocalisation(strings[2])),
                    trans.getBirth(), strings[6]);
        }
        return result;
    }

    public void transcritToFio(FioDto fioDto) {
        if (fioDto.getFirstName() != null) {
            fioDto.setFirstName(setUpperFirst(transcriterHolder.getTransctriter().transcritWordToLocalisation(fioDto.getFirstName())));

        }
        if (fioDto.getMiddleName() != null) {
            fioDto.setMiddleName(setUpperFirst(transcriterHolder.getTransctriter().transcritWordToLocalisation(fioDto.getMiddleName())));
        }
        if (fioDto.getLastName() != null) {
            fioDto.setLastName(setUpperFirst(transcriterHolder.getTransctriter().transcritWordToLocalisation(fioDto.getLastName())));
        }
    }

    public void from(FamilyMemberDto familyMemberDto) {
        transcritFromFio(familyMemberDto);

        if (familyMemberDto.getMotherFio() != null)
            transcritFromFio(familyMemberDto.getMotherFio());
        if (familyMemberDto.getFatherFio() != null) transcritFromFio(familyMemberDto.getFatherFio());

        if (familyMemberDto.getFioDtos() != null) {
            for (FioDto fioDto :
                    familyMemberDto.getFioDtos())
                transcritFromFio(fioDto);
        }
    }

    public String setUpperFirst(String string) {
        if (string.length() == 1) return String.valueOf(string.charAt(0)).toUpperCase();
        else return String.valueOf(string.charAt(0)).toUpperCase() + string.substring(1);
    }

    public void transcritFromFio(FioDto fioDto) {
        if (fioDto.getFirstName() != null) {
            fioDto.setFirstName(transcriterHolder.getTransctriter().transcritWordFromLocalisation(fioDto.getFirstName()));
        }
        if (fioDto.getMiddleName() != null) {
            fioDto.setMiddleName(transcriterHolder.getTransctriter().transcritWordFromLocalisation(fioDto.getMiddleName()));
        }
        if (fioDto.getLastName() != null) {
            fioDto.setLastName(transcriterHolder.getTransctriter().transcritWordFromLocalisation(fioDto.getLastName()));
        }
    }
}