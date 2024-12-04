package com.example.transcriters;

import com.example.dtos.FamilyMemberDto;
import com.example.exceptions.UncorrectedInformation;
import com.example.service.TokenService;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TranscriterHolder {
    private AbstractTranscriter transctriter;
    private final TokenService tokenService;

    public TranscriterHolder(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public void setTranscriter(FamilyMemberDto familyMemberDto) {
        if (familyMemberDto.getLocalisation()==null) familyMemberDto.setLocalisation((String) tokenService.getTokenUser().getClaims().get("localisation"));
        if (familyMemberDto.getLocalisation().equals("loc=ru")) transctriter = new RusTranscriter();
        if (familyMemberDto.getLocalisation().equals("loc=en")) {
            if (checkForLang(familyMemberDto.toString()).equals("en")) transctriter = new EmptyTranscriter();
            else throw new UncorrectedInformation("Данные не соответствуют выбранной локализации");
        }
    }

    private static String checkForLang(String str) {
        System.out.println(str);
        for (int i : str.chars().toArray()){
            if (i >= 178&&i!=697) {
                System.out.println(i); return "other";}}
        return "en";
    }
}
