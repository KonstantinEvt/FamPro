package ru.memman.service;

import reactor.core.CoreSubscriber;
import ru.memman.dtos.OnlineUserDto;
import ru.memman.dtos.TokenUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TokenService {
    OnlineUserService onlineUserService;

    public Mono<OnlineUserDto> getOnlineUser() {
        TokenUser tokenUser = new TokenUser();
        return Mono.just(new OnlineUserDto())
//                ReactiveSecurityContextHolder.getContext()
//                .map(SecurityContext::getAuthentication)
//                .map(auth -> {
//                    tokenUser.setClaims(((Jwt) auth.getPrincipal()).getClaims());
//                    System.out.println(tokenUser.getClaims());
//                    return onlineUserService.getOnlineUser(tokenUser);
//                })
                ;
    }

    public String getTokenUserUuid(TokenUser tokenUser) {
        return (String) tokenUser.getClaims().get("sub");
    }
}
