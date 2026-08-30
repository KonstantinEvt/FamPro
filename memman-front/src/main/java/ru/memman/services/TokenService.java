package ru.memman.services;

import lombok.extern.log4j.Log4j2;
import ru.memman.dtos.FamilyMemberDto;
import ru.memman.dtos.TokenUser;
import ru.memman.enums.CheckStatus;
import ru.memman.enums.Localisation;
import ru.memman.enums.UserRole;
import ru.memman.feign.KeyCloakManageClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
@SuppressWarnings("unchecked")
public class TokenService {
    private final KeyCloakManageClient keyCloakManageClient;


    public TokenService(KeyCloakManageClient keyCloakManageClient) {
        this.keyCloakManageClient = keyCloakManageClient;
    }

    public TokenUser getTokenUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        TokenUser tokenUser = new TokenUser();
        tokenUser.setClaims(jwt.getClaims());
        tokenUser.setUsername((String) jwt.getClaims().get("preferred_username"));
        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().getOrDefault("realm_access", Map.of());
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

    public Localisation chooseLocalisation(String localisation) {
        if (localisation == null) return Localisation.RU;
        for (Localisation loc :
                Localisation.values()) {
            if (localisation.toUpperCase().equals(loc.name())) return loc;
        }
        return Localisation.RU;
    }

    public String setGlobalLocalisation(String localisationString) {
        Localisation loc = chooseLocalisation(localisationString);
        keyCloakManageClient.chooseLocalisation(loc.name().toLowerCase());
        if (Objects.equals(loc.name().toLowerCase(), (String) getTokenUser().getClaims().get("locale")))
            return "Change not required but set. Languish: ".concat(loc.name().toLowerCase());
        return "Change languish to: ".concat(loc.name().toLowerCase());

    }
}
