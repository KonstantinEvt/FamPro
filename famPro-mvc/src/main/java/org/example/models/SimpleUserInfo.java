package org.example.models;

import com.example.dtos.TokenUser;
import lombok.Getter;
import lombok.Setter;
import org.example.services.TokenService;
import org.springframework.stereotype.Component;

import java.util.Map;


//@Scope(value = SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Setter
@Getter
public class SimpleUserInfo {
    private TokenService tokenService;
    private String nickName = "Anonymous";
    private String localisation = "loc=en";
    private String fullName;
    private String role = "SimpleUser";
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
        return this;
    }

    ;
}
