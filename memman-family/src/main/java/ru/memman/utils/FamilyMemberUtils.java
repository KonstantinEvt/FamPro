package ru.memman.utils;

import ru.memman.dtos.TokenUser;
import ru.memman.enums.CheckStatus;
import ru.memman.enums.UserRole;
import ru.memman.entity.ShortFamilyMember;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FamilyMemberUtils {
    public static boolean checkRightsToEdit(ShortFamilyMember familyMember, TokenUser tokenUser) {
        if (familyMember.getCheckStatus() == CheckStatus.UNCHECKED
                || familyMember.getCheckStatus() == CheckStatus.MODERATE
                || tokenUser.getClaims().get("sub").equals(familyMember.getCreator())
                || tokenUser.getRoles().contains(UserRole.ADMIN.getNameSSO())) return true;
        return familyMember.getCheckStatus() == CheckStatus.CHECKED
                && (tokenUser.getRoles().contains(UserRole.MANAGER.getNameSSO())
                || tokenUser.getRoles().contains(UserRole.LINKED_USER.getNameSSO())
                || tokenUser.getRoles().contains(UserRole.VIP.getNameSSO()));

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
        return tokenUser.getRoles().contains(UserRole.MANAGER.getNameSSO())
                || tokenUser.getRoles().contains(UserRole.ADMIN.getNameSSO());
    }

    public static void selectCheckStatus(ShortFamilyMember fm, Set<String> roles) {

        if (roles.contains(UserRole.LINKED_USER.getNameSSO())
                || roles.contains(UserRole.MANAGER.getNameSSO())
                || roles.contains(UserRole.VIP.getNameSSO())
                || roles.contains(UserRole.ADMIN.getNameSSO()))
            fm.setCheckStatus(CheckStatus.CHECKED);
        else fm.setCheckStatus(CheckStatus.UNCHECKED);
    }
}
