package org.example.services;

import com.example.dtos.TokenUser;
import lombok.AllArgsConstructor;
import org.example.feign.KeyCloakManageClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
        return tokenUser;
    }

    public TokenUser getRolesUser(TokenUser tokenUser) {

        return tokenUser;
    }

    public void addUser(TokenUser tokenUser) {
        System.out.println(tokenUser);

            keyCloakManageClient.addUser(tokenUser);


//        webClient
//                .post()
//                .uri("http://localhost:6666/manage/add")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(tokenUser)
//                .retrieve()
//                .toBodilessEntity()
//                .block();
                System.out.println(tokenUser);
    }

    public void getToken() {
//        keyCloakManageClient.addUser(tokenUser);
//        SecurityContext context = SecurityContextHolder.getContext();
//        Authentication authentication = context.getAuthentication();

        webClient
                .get()
                .uri("http://famPro-key/manage/token")
                .retrieve()
                .toBodilessEntity()
                .block();

    }
}
