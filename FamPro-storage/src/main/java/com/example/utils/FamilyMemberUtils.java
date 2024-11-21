package com.example.utils;

import com.example.dtos.TokenUser;
import com.example.entity.FamilyMember;
import com.example.enums.CheckStatus;
import com.example.enums.UserRoles;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.UUID;

@Component
public class FamilyMemberUtils {
    public static boolean checkRightsToEdit(FamilyMember familyMember, TokenUser tokenUser) {
        if (tokenUser.getClaims().get("sub").equals(familyMember.getCreator())) return true;
        else if (tokenUser.getRoles().contains(UserRoles.MANAGER.getNameSSO())) {
            familyMember.setCheckStatus(CheckStatus.MODERATE);
            return true;
            //А это надо (отбор права редактирования) прогнать через Validate module
        } else if (tokenUser.getFirstName().equals(familyMember.getFirstName())
                && tokenUser.getMiddleName().equals(familyMember.getMiddleName())
                && tokenUser.getLastName().equals(familyMember.getLastName())
                && (Date.valueOf(tokenUser.getBirthday())).toLocalDate().equals(familyMember.getBirthday().toLocalDate())) {
            familyMember.setCreator((String) tokenUser.getClaims().get("sub"));
            return true;
        }
    else return false;
}
public static boolean checkRightsToModerate(TokenUser tokenUser) {
    return tokenUser.getRoles().contains(UserRoles.MANAGER.getNameSSO());
    }
}
