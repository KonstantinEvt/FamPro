package ru.memman.utils;

import ru.memman.dtos.TokenUser;
import ru.memman.entity.FamilyMember;
import ru.memman.enums.CheckStatus;
import ru.memman.enums.UserRole;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FamilyMemberUtils {
    public static boolean checkRightsToEdit(FamilyMember familyMember, TokenUser tokenUser) {
        return (familyMember.getCheckStatus() == CheckStatus.UNCHECKED
                || familyMember.getCheckStatus() == CheckStatus.MODERATE
                || tokenUser.getClaims().get("sub").equals(familyMember.getCreator())
                || tokenUser.getRoles().contains(UserRole.ADMIN.getNameSSO()))
                || (familyMember.getCheckStatus() == CheckStatus.CHECKED
                    && (tokenUser.getRoles().contains(UserRole.LINKED_USER.getNameSSO())
                        || tokenUser.getRoles().contains(UserRole.MANAGER.getNameSSO())
                        || tokenUser.getRoles().contains(UserRole.VIP.getNameSSO())));
    }

    public static boolean checkRightsToModerate(TokenUser tokenUser) {
        return tokenUser.getRoles().contains(UserRole.MANAGER.getNameSSO())
                || tokenUser.getRoles().contains(UserRole.ADMIN.getNameSSO());
    }

    public static void selectCheckStatus(FamilyMember fm, Set<String> roles) {

        if (roles.contains(UserRole.MANAGER.getNameSSO())
                || roles.contains(UserRole.ADMIN.getNameSSO()))
            fm.setCheckStatus(CheckStatus.CHECKED);
        else fm.setCheckStatus(CheckStatus.UNCHECKED);
    }
}
