package ru.memman.services;

import ru.memman.dtos.TokenUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import ru.memman.enums.UserRole;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TokenService {
    public TokenUser getTokenUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        TokenUser tokenUser = new TokenUser();
        tokenUser.setClaims(jwt.getClaims());
        return tokenUser;
    }
    @SuppressWarnings("unchecked")
    public UserRole getPriorityRole(TokenUser tokenUser) {
        final var realmAccess=(Map<String, Object>) tokenUser.getClaims().getOrDefault("realm_access", Map.of());
        final var realmRoles = (Collection<String>) realmAccess.getOrDefault("roles", List.of());
        for (UserRole role :
                UserRole.values()) {
            if (realmRoles.contains(role.getNameSSO())) return role;
        }
        return UserRole.SIMPLE_USER;
    }
}
