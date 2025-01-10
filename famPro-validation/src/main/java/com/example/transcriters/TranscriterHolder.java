package com.example.transcriters;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.TokenUser;
import com.example.exceptions.UncorrectedInformation;
import lombok.Getter;

@Getter
public class TranscriterHolder {
    private AbstractTranscriter transcriter;
    private final TokenUser tokenUser;

    public TranscriterHolder(TokenUser tokenUser) {
        this.tokenUser = tokenUser;
    }

    public void setTranscriter(FamilyMemberDto familyMemberDto) {
        if (familyMemberDto.getLocalisation() == null)
            familyMemberDto.setLocalisation((String) tokenUser.getClaims().get("localisation"));
        if (familyMemberDto.getLocalisation().equals("ru")) transcriter = new RusTranscriter();
        else if (familyMemberDto.getLocalisation().equals("en")) {

            if (checkForLang(familyMemberDto.toString()).equals("en")) transcriter = new EmptyTranscriter();
            else
                throw new UncorrectedInformation("Данные не соответствуют выбранной локализации - выберите язык в соответствии с вводимыми данныыми");

        } else throw  new UncorrectedInformation("Пока выбранная локализация не обслуживается");
    }

    private static String checkForLang(String str) {
        String[] str1=str.split("memberInfo=FamilyMemberInfoDto|mainAddress=");
        str1[0]=str1[0]+str1[2];
        for (int i : str1[0].chars().toArray()) {
            //8470 символ - "№"
            if (i >= 191 && i!=8470) {
                return "other";
            }
        }
        return "en";
    }
}
