package com.example.utils;

import com.example.dtos.TokenUser;
import com.example.entity.FamilyMember;
import com.example.enums.CheckStatus;
import com.example.enums.UserRoles;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FamilyMemberUtils {
    public static boolean checkRightsToEdit(FamilyMember familyMember, TokenUser tokenUser) {
        return (familyMember.getCheckStatus() == CheckStatus.UNCHECKED
                || familyMember.getCheckStatus() == CheckStatus.MODERATE
                || tokenUser.getClaims().get("sub").equals(familyMember.getCreator())
                || tokenUser.getRoles().contains(UserRoles.ADMIN.getNameSSO()))
                || (familyMember.getCheckStatus() == CheckStatus.CHECKED
                    && (tokenUser.getRoles().contains(UserRoles.LINKED_USER.getNameSSO())
                        || tokenUser.getRoles().contains(UserRoles.MANAGER.getNameSSO())
                        || tokenUser.getRoles().contains(UserRoles.VIP.getNameSSO())));
    }

    public static boolean checkRightsToModerate(TokenUser tokenUser) {
        return tokenUser.getRoles().contains(UserRoles.MANAGER.getNameSSO())
                || tokenUser.getRoles().contains(UserRoles.ADMIN.getNameSSO());
    }

    public static void selectCheckStatus(FamilyMember fm, Set<String> roles) {

        if (roles.contains(UserRoles.MANAGER.getNameSSO())
                || roles.contains(UserRoles.ADMIN.getNameSSO()))
            fm.setCheckStatus(CheckStatus.CHECKED);
        else fm.setCheckStatus(CheckStatus.UNCHECKED);
    }
}
