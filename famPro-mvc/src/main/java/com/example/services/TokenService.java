package com.example.services;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.TokenUser;
import com.example.enums.CheckStatus;
import com.example.enums.Localisation;
import com.example.enums.UserRoles;
import com.example.feign.KeyCloakManageClient;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@SuppressWarnings("unchecked")
public class TokenService {
//    private WebClient webClient;
    private KeyCloakManageClient keyCloakManageClient;

    public TokenUser getTokenUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        TokenUser tokenUser = new TokenUser();
        tokenUser.setClaims(jwt.getClaims());
        tokenUser.setUsername((String) jwt.getClaims().get("preferred_username"));
        Map<String,Object> realmAccess=(Map<String, Object>) jwt.getClaims().getOrDefault("realm_access", Map.of());
        Set<String> roles = new HashSet<>(((ArrayList<String>) realmAccess.getOrDefault(("roles"), new ArrayList<String>())));
        tokenUser.setRoles(roles);
        return tokenUser;
    }



    public void editUser(TokenUser tokenUser) {
        keyCloakManageClient.editUser(tokenUser);
    }

    public void addUser(TokenUser tokenUser) {
        keyCloakManageClient.addUser(tokenUser);
    }

    public void linkUser(FamilyMemberDto dto) {
        TokenUser tokenUser = getTokenUser();
//        tokenUser.setFirstName(dto.getFirstName());
//        tokenUser.setMiddleName(dto.getMiddleName());
//        tokenUser.setLastName(dto.getLastName());
//        tokenUser.setBirthday(String.valueOf(dto.getBirthday()));
        tokenUser.getRoles().add(UserRoles.LINKED_USER.getNameSSO());
        keyCloakManageClient.editUser(tokenUser);
        dto.setCheckStatus(CheckStatus.LINKED);
    }
//        webClient
//                .post()
//                .uri("http://localhost:6666/manage/add")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(tokenUser)
//                .retrieve()
//                .toBodilessEntity()
//                .block();


    public Localisation chooseLocalisation(String localisation) {
        keyCloakManageClient.chooseLocalisation(localisation);
        for (Localisation loc :
                Localisation.values()) {
            if (localisation.toUpperCase().equals(loc.name())) return loc;
        }
        return Localisation.EN;
    }
}
