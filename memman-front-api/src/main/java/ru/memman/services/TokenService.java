package ru.memman.services;

import ru.memman.dtos.DirectiveGuards;
import ru.memman.dtos.FamilyMemberDto;
import ru.memman.dtos.TokenUser;
import ru.memman.enums.CheckStatus;
import ru.memman.enums.KafkaOperation;
import ru.memman.enums.Localisation;
import ru.memman.enums.UserRole;
import ru.memman.feign.KeyCloakManageClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@SuppressWarnings("unchecked")
public class TokenService {
//    private WebClient webClient;
    private final KeyCloakManageClient keyCloakManageClient;
    private final LinkedList<DirectiveGuards> setLanguishFamily;
    private final LinkedList<DirectiveGuards> setLanguishStorage ;

    public TokenService(KeyCloakManageClient keyCloakManageClient,
                        @Qualifier("languishFamily") LinkedList<DirectiveGuards> setLanguishFamily,
                        @Qualifier("languishStorage") LinkedList<DirectiveGuards> setLanguishStorage) {
        this.keyCloakManageClient = keyCloakManageClient;
        this.setLanguishFamily = setLanguishFamily;
        this.setLanguishStorage = setLanguishStorage;
    }

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
        tokenUser.getRoles().add(UserRole.LINKED_USER.getNameSSO());
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
    public void setGlobalLocalisation(String inlineUuid, String localisationString){
        Localisation localisation=Localisation.RU;
        for (Localisation loc :
                Localisation.values()) {
            if (localisationString.toUpperCase().equals(loc.name()))
                localisation=loc;
        }
        DirectiveGuards directive=DirectiveGuards.builder().tokenUser(inlineUuid).operation(KafkaOperation.GET).localisation(localisation).build();
        setLanguishStorage.add(directive);
        setLanguishFamily.add(directive);}
}
