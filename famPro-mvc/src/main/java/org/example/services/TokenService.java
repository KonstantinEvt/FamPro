package org.example.services;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.TokenUser;
import com.example.enums.CheckStatus;
import com.example.enums.UserRoles;
import lombok.AllArgsConstructor;
import org.example.feign.KeyCloakManageClient;
import org.example.config.TaskResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TokenService {
    private WebClient webClient;
    private KeyCloakManageClient keyCloakManageClient;
    private LinkedList<TokenUser> tokenUserResource;

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
        tokenUserResource.add(tokenUser);
    }

    public void linkUser(FamilyMemberDto dto) {
        //Тут должен был запрос на подтверждение личности
        TokenUser tokenUser = getTokenUser();
        tokenUser.setFirstName(dto.getFirstName());
        tokenUser.setMiddleName(dto.getMiddleName());
        tokenUser.setLastName(dto.getLastName());
        tokenUser.setBirthday(String.valueOf(dto.getBirthday()));
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


    public void chooseLocalisation(String localisation) {
        keyCloakManageClient.chooseLocalisation(localisation);
    }

//    public void getToken() {
//
//
//        webClient
//                .get()
//                .uri("http://famPro-key/manage/token")
//                .retrieve()
//                .toBodilessEntity()
//                .block();
//
//    }
}
