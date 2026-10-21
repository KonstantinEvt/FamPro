package ru.memman.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import ru.memman.dtos.OnlineUserDto;
import ru.memman.dtos.TokenUser;
import ru.memman.enums.Localisation;
import ru.memman.feign.KeyCloakManageClient;

import java.util.*;

@Service
@Log4j2
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
        return tokenUser;
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
