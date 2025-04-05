package com.example.models;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.TokenUser;
import com.example.enums.CheckStatus;
import com.example.enums.Localisation;
import com.example.enums.UserRoles;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Locale;
import java.util.Map;

@Setter
@Getter
@ToString
public class SimpleUserInfo {
    private String nickName = "Anonymous";
    private Localisation localisation = Localisation.EN;
    private String email;
    private String firstName = "Unknown";
    private String middleName = "Unknown";
    private String lastName = "Unknown";
    private String fullName = "Unknown";
    private String role = "SimpleUser";
    private String birthday;
    private String userName;
    private String id;


    public SimpleUserInfo(TokenUser tokenUser) {
        setUpUserName(tokenUser);
        Map<String, Object> claims = tokenUser.getClaims();
        setUpRole(tokenUser);
        if (claims != null) {
            setUpNickName(claims);
            setUpLocalisation(claims);
            setUpFullName(claims, tokenUser);
            setUpFirstName(claims);
            setUpMiddleName(claims);
            setUpLastName(claims);
            setUpBirthday(claims);
            setUpEmail(claims);
            setUpId(claims);
        }
    }

    private void setUpUserName(TokenUser tokenUser) {
        this.userName = tokenUser.getUsername();
    }

    private void setUpNickName(Map<String, Object> claims) {
        if (claims.get("nickname") != null
                && !claims.get("nickname").toString().isBlank()) {
            this.nickName = (String) claims.get(("nickname"));
        }
    }

    private void setUpId(Map<String, Object> claims) {
        if (claims.get("sub") != null
                && !claims.get("sub").toString().isBlank()) {
            this.id = (String) claims.get(("sub"));
        }
    }

    private void setUpFirstName(Map<String, Object> claims) {
        if (claims.get("given_name") != null
                && !claims.get("given_name").toString().isBlank()) {
            this.firstName = (String) claims.get(("given_name"));
        }
    }

    private void setUpMiddleName(Map<String, Object> claims) {
        if (claims.get("middle_name") != null
                && !claims.get("middle_name").toString().isBlank()) {
            this.middleName = (String) claims.get(("middle_name"));
        }
    }

    private void setUpLastName(Map<String, Object> claims) {
        if (claims.get("family_name") != null
                && !claims.get("family_name").toString().isBlank()) {
            this.lastName = (String) claims.get(("family_name"));
        }
    }

    private void setUpBirthday(Map<String, Object> claims) {
        if (claims.get("birthdate") != null
                && !claims.get("birthdate").toString().isBlank()) {
            this.birthday = (String) claims.get(("birthday"));
        }
    }

    private void setUpEmail(Map<String, Object> claims) {
        if (claims.get("email") != null
                && !claims.get("email").toString().isBlank()) {
            this.email = (String) claims.get(("email"));
        }
    }

    private void setUpLocalisation(Map<String, Object> claims) {
        if (claims.get("localisation") != null
                && !claims.get("localisation").toString().isBlank()) {
            String local = (String) claims.get("localisation");
            for (Localisation loc :
                    Localisation.values()) {
                if (local.toUpperCase().equals(loc.name())) this.localisation = loc;
            }
        }
    }

    private void setUpFullName(Map<String, Object> claims, TokenUser tokenUser) {
        if (claims.get("name") != null
                && !claims.get("name").toString().isBlank()) this.fullName = (String) claims.get("name");
        else this.fullName = tokenUser.getUsername();
    }

    private void setUpRole(TokenUser tokenUser) {
        this.role = getPriorityRole(tokenUser);
    }

    private String getPriorityRole(TokenUser tokenUser) {
        for (UserRoles role :
                UserRoles.values()) {
            if (tokenUser.getRoles().contains(role.getNameSSO())) return role.getNameSSO();
        }
        return "you are haven't role";
    }

    public SimpleUserInfo editUser(TokenUser tokenUser) {
        if (tokenUser.getNickName() != null && !tokenUser.getNickName().isBlank())
            this.nickName = tokenUser.getNickName();
        if (tokenUser.getEmail() != null && !tokenUser.getEmail().isBlank()) this.email = tokenUser.getEmail();
        if (tokenUser.getFirstName() != null && !tokenUser.getFirstName().isBlank())
            this.firstName = tokenUser.getFirstName();
        if (tokenUser.getMiddleName() != null && !tokenUser.getMiddleName().isBlank())
            this.middleName = tokenUser.getMiddleName();
        if (tokenUser.getLastName() != null && !tokenUser.getLastName().isBlank())
            this.lastName = tokenUser.getLastName();
        if (tokenUser.getBirthday() != null && !tokenUser.getBirthday().isBlank())
            this.birthday = tokenUser.getBirthday();
        this.fullName = this.firstName.concat(" ").concat(this.lastName);
        return this;
    }

    public void editUserByLinked(FamilyMemberDto tokenUser) {

//        if (tokenUser.getFirstName() != null && !tokenUser.getFirstName().isBlank())
//            setFirstName(tokenUser.getFirstName());
//        if (tokenUser.getMiddleName() != null && !tokenUser.getMiddleName().isBlank())
//            setMiddleName(tokenUser.getMiddleName());
//        if (tokenUser.getLastName() != null && !tokenUser.getLastName().isBlank()) setLastName(tokenUser.getLastName());
        if (tokenUser.getCheckStatus() == CheckStatus.LINKED)
            this.role = UserRoles.LINKED_USER.getNameSSO();
    }
}
