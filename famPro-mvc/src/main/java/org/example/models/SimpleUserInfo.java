package org.example.models;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.TokenUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.services.TokenService;

import java.util.Map;

@Setter
@Getter
@ToString
public class SimpleUserInfo {
    private TokenService tokenService;
    private String nickName = "Anonymous";
    private String localisation = "en";
    private String email;
    private String firstName= "Unknown";
    private String middleName= "Unknown";
    private String lastName= "Unknown";
    private String fullName= "Unknown";
    private String role = "SimpleUser";
    private String birthday;
    private String userName;


    public SimpleUserInfo(TokenService tokenService) {
        this.tokenService = tokenService;
        TokenUser tokenUser = tokenService.getTokenUser();
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
                && !claims.get("localisation").toString().isBlank())
            this.localisation = (String) claims.get("localisation");
        else tokenService.chooseLocalisation(this.localisation);
    }

    private void setUpFullName(Map<String, Object> claims, TokenUser tokenUser) {
        if (claims.get("name") != null
                && !claims.get("name").toString().isBlank()) this.fullName = (String) claims.get("name");
        else this.fullName = tokenUser.getUsername();
    }

    private void setUpRole(TokenUser tokenUser) {
        this.role = tokenService.getPriorityUserRole(tokenUser);
    }

    public SimpleUserInfo editUser(TokenUser tokenUser) {
        if (tokenUser.getNickName() != null && !tokenUser.getNickName().isBlank()) setNickName(tokenUser.getNickName());
        if (tokenUser.getEmail() != null && !tokenUser.getEmail().isBlank()) setEmail(tokenUser.getEmail());
        if (tokenUser.getFirstName() != null && !tokenUser.getFirstName().isBlank()) setFirstName(tokenUser.getFirstName());
        if (tokenUser.getMiddleName() != null && !tokenUser.getMiddleName().isBlank()) setMiddleName(tokenUser.getMiddleName());
        if (tokenUser.getLastName() != null && !tokenUser.getLastName().isBlank()) setLastName(tokenUser.getLastName());
        if (tokenUser.getBirthday() != null && !tokenUser.getBirthday().isBlank()) setBirthday(tokenUser.getBirthday());
        return this;
    }
    public SimpleUserInfo editUser(FamilyMemberDto tokenUser) {

        if (tokenUser.getFirstName() != null && !tokenUser.getFirstName().isBlank()) setFirstName(tokenUser.getFirstName());
        if (tokenUser.getMiddleName() != null && !tokenUser.getMiddleName().isBlank()) setMiddleName(tokenUser.getMiddleName());
        if (tokenUser.getLastName() != null && !tokenUser.getLastName().isBlank()) setLastName(tokenUser.getLastName());

        return this;
    }
}
