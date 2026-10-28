package ru.memman.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import ru.memman.dtos.FamilyMemberDto;
import ru.memman.dtos.TokenUser;
import ru.memman.enums.CheckStatus;
import ru.memman.enums.Localisation;
import ru.memman.enums.UserRole;
import ru.memman.feign.KeyCloakManageClient;

import java.util.*;

@Service
@SuppressWarnings("unchecked")
public class TokenService {
//    private WebClient webClient;
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
        Map<String,Object> realmAccess=(Map<String, Object>) jwt.getClaims().getOrDefault("realm_access", Map.of());
        Set<String> roles = new HashSet<>(((ArrayList<String>) realmAccess.getOrDefault(("roles"), new ArrayList<String>())));
        tokenUser.setRoles(roles);
        return tokenUser;
    }
    public UserRole getPriorityRole(TokenUser tokenUser) {
        final var realmAccess=(Map<String, Object>) tokenUser.getClaims().getOrDefault("realm_access", Map.of());
        final var realmRoles = (Collection<String>) realmAccess.getOrDefault("roles", List.of());
        for (UserRole role :
                UserRole.values()) {
            if (realmRoles.contains(role.getNameSSO())) return role;
        }
        return UserRole.SIMPLE_USER;
    }

    public void editUser(TokenUser tokenUser) {
        keyCloakManageClient.editUser(tokenUser);
    }

    public void addUser(TokenUser tokenUser) {
        keyCloakManageClient.addUser(tokenUser);
    }

    public void linkUser(FamilyMemberDto dto) {
        TokenUser tokenUser = getTokenUser();
        tokenUser.getRoles().add(UserRole.LINKED_USER.getNameSSO());
        keyCloakManageClient.editUser(tokenUser);
        dto.setCheckStatus(CheckStatus.LINKED);
    }

    public Localisation getLocalisation(String localisation) {
        if (localisation == null) return Localisation.RU;
        for (Localisation loc :
                Localisation.values()) {
            if (localisation.toUpperCase().equals(loc.name())) return loc;
        }
        return Localisation.RU;
    }
}
