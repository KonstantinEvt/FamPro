package org.example.models;

import lombok.Getter;
import lombok.Setter;
import org.example.services.TokenService;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class SimpleUserInfo {
    private final TokenService tokenService;
    private String nickName = "Anonymous";
    private String localisation = "loc=en";
    private String fullName = "Unnamed user";
    private Boolean overCross = false;
    private String role = "SimpleUser";

    public SimpleUserInfo(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    public void setUpNickName() {
        if (tokenService.getTokenUser().getClaims() != null
                && tokenService.getTokenUser().getClaims().get("nickname") != null
                && !tokenService.getTokenUser().getClaims().get("nickname").toString().isBlank()) {
            this.nickName = (String) tokenService.getTokenUser().getClaims().get(("nickname"));
        }
    }

    public void setUpLocalisation() {
        if (tokenService.getTokenUser().getClaims() != null) {
            this.localisation = (String) tokenService.getTokenUser().getClaims().get("localisation");
        }
    }

    public void setUpOverCross() {
        this.overCross = true;
    }

    public void setUpFullName() {
        if (tokenService.getTokenUser().getClaims() != null) {
            this.fullName = (String) tokenService.getTokenUser().getClaims().get("name");
        }
    }
    public void setUpRole(){
        this.role=tokenService.getPriorityUserRole(tokenService.getTokenUser());
    }

    public void setUp() {
        setUpOverCross();
        setUpRole();
        setUpNickName();
        setUpLocalisation();
        setUpFullName();
    }
}
