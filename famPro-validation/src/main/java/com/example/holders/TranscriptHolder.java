package com.example.holders;

import com.example.dtos.FamilyMemberDto;
import com.example.enums.Localisation;
import com.example.exceptions.UncorrectedInformation;
import com.example.transcriters.AbstractTranscripter;
import lombok.Getter;

import java.util.Objects;

@Getter
public class TranscriptHolder {
    private final AbstractTranscripter rusTranscript;
    private final AbstractTranscripter englishTranscript;

    public TranscriptHolder(AbstractTranscripter rusTranscript, AbstractTranscripter englishTranscript) {
        this.rusTranscript = rusTranscript;
        this.englishTranscript = englishTranscript;
    }

    public AbstractTranscripter getTranscript(FamilyMemberDto familyMemberDto, String localisation) {
        if (familyMemberDto.getLocalisation() == null)
            for (Localisation loc :
                    Localisation.values()) {
                if (loc.name().equals(localisation))
                    familyMemberDto.setLocalisation(loc);
            }
        if (familyMemberDto.getLocalisation() == null) familyMemberDto.setLocalisation(Localisation.EN);
        if (familyMemberDto.getLocalisation() == Localisation.RU) return rusTranscript;
        else if (familyMemberDto.getLocalisation() == Localisation.EN) {

            if (checkForLang(familyMemberDto.toString()).equals("en")) return englishTranscript;
            else
                throw new UncorrectedInformation("Данные не соответствуют выбранной локализации - выберите язык в соответствии с вводимыми данныыми");

        } else throw new UncorrectedInformation("Пока выбранная локализация не обслуживается");
    }

    private static String checkForLang(String str) {
        String[] str1 = str.split("memberInfo=FamilyMemberInfoDto|mainAddress=");
        str1[0] = str1[0] + str1[2];
        for (int i : str1[0].chars().toArray()) {
            //8470 символ - "№"
            if (i >= 191 && i != 8470) {
                return "other";
            }
        }
        return "en";
    }

    public AbstractTranscripter getTranscript(String localisation) {
        for (Localisation loc :
                Localisation.values()) {
            if (Objects.equals(loc.name(), localisation)) return getTranscript(loc);
        }
        return englishTranscript;
    }

    public AbstractTranscripter getTranscript(Localisation localisation) {
        switch (localisation) {
            case RU -> {
                return rusTranscript;
            }
            default -> {
                return englishTranscript;
            }
        }


    }}
