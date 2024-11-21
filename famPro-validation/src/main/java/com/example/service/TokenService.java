package com.example.service;

import com.example.dtos.TokenUser;
import com.example.enums.UserRoles;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

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
    public String getLocalisation(TokenUser tokenUser){
        if (tokenUser.getClaims()!=null&&tokenUser.getClaims().get("localisation").equals("loc=ru")) return "ru";
        else return "en";
    }
}
