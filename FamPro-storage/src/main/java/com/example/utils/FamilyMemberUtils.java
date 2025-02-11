package com.example.utils;

import com.example.dtos.TokenUser;
import com.example.entity.FamilyMember;
import com.example.enums.CheckStatus;
import com.example.enums.UserRoles;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Set;
import java.util.UUID;

@Component
public class FamilyMemberUtils {
    public static boolean checkRightsToEdit(FamilyMember familyMember, TokenUser tokenUser) {
        if (familyMember.getCheckStatus() == CheckStatus.UNCHECKED
                || familyMember.getCheckStatus() == CheckStatus.MODERATE
                || tokenUser.getClaims().get("sub").equals(familyMember.getCreator())
                || tokenUser.getRoles().contains(UserRoles.ADMIN.getNameSSO())) return true;
        return familyMember.getCheckStatus() == CheckStatus.CHECKED
                && (tokenUser.getRoles().contains(UserRoles.MANAGER.getNameSSO())
                || tokenUser.getRoles().contains(UserRoles.VIP.getNameSSO()));

//        else if (tokenUser.getRoles().contains(UserRoles.MANAGER.getNameSSO())) {
//            familyMember.setCheckStatus(CheckStatus.MODERATE);
//            return true;
//            //А это надо (отбор права редактирования) прогнать через Validate module
//        } else if (tokenUser.getFirstName().equals(familyMember.getFirstName())
//                && tokenUser.getMiddleName().equals(familyMember.getMiddleName())
//                && tokenUser.getLastName().equals(familyMember.getLastName())
//                && (Date.valueOf(tokenUser.getBirthday())).toLocalDate().equals(familyMember.getBirthday().toLocalDate())) {
//            familyMember.setCreator((String) tokenUser.getClaims().get("sub"));
//            return true;
//        } else return false;
    }

    public static boolean checkRightsToModerate(TokenUser tokenUser) {
        return tokenUser.getRoles().contains(UserRoles.MANAGER.getNameSSO())
                || tokenUser.getRoles().contains(UserRoles.ADMIN.getNameSSO());
    }

    public static void selectCheckStatus(FamilyMember fm, Set<String> roles) {

        if ( roles.contains(UserRoles.MANAGER.getNameSSO())
                || roles.contains(UserRoles.ADMIN.getNameSSO()))
            fm.setCheckStatus(CheckStatus.CHECKED);
        else fm.setCheckStatus(CheckStatus.UNCHECKED);
    }
}
