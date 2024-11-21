package org.example.services;

import com.example.dtos.TokenUser;
import com.example.enums.UserRoles;
import lombok.AllArgsConstructor;
import org.example.feign.KeyCloakManageClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TokenService {
    private WebClient webClient;
    private KeyCloakManageClient keyCloakManageClient;


    public TokenUser getTokenUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        TokenUser tokenUser = new TokenUser();
        tokenUser.setClaims(jwt.getClaims());
        tokenUser.setUsername((String) jwt.getClaims().get("preferred_username"));
        Set<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        tokenUser.setRoles(roles);
        return tokenUser;
    }

    public String getPriorityUserRole(TokenUser tokenUser) {
        for (UserRoles role :
                UserRoles.values()) {
            if (tokenUser.getRoles().contains(role.getNameSSO())) return role.getNameSSO();
        }
        return "you are haven't role";
    }

    public void editUser(TokenUser tokenUser) {
        keyCloakManageClient.editUser(tokenUser);
    }

    public void addUser(TokenUser tokenUser) {
        keyCloakManageClient.addUser(tokenUser);


//        webClient
//                .post()
//                .uri("http://localhost:6666/manage/add")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(tokenUser)
//                .retrieve()
//                .toBodilessEntity()
//                .block();
    }
    public void chooseLocalisation(String localisation){
        keyCloakManageClient.chooseLocalisation(localisation);

    }
    public void getToken() {


        webClient
                .get()
                .uri("http://famPro-key/manage/token")
                .retrieve()
                .toBodilessEntity()
                .block();

    }
}
