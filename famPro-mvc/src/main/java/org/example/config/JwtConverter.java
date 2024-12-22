package org.example.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

//@EnableWebSecurity
//@Configuration
//public class JwtConverter {
//    public interface Jwt2AuthoritiesConverter extends Converter<Jwt, Collection<? extends GrantedAuthority>> {
//    }
//
//    @SuppressWarnings("unchecked")
//    @Bean
//    public Jwt2AuthoritiesConverter authoritiesConverter() {
//        return jwt -> {
//            final var realmAccess = (Map<String, Object>) jwt.getClaims().getOrDefault("realm_access", Map.of());
//            final var realmRoles = (Collection<String>) realmAccess.getOrDefault("roles", List.of());
//
//            final var resourceAccess = (Map<String, Object>) jwt.getClaims().getOrDefault("resource_access", Map.of());
//            // We assume here you have "backend"  and "other-client" clients configured with "client roles" mapper in Keycloak
//            final var confidentialClientAccess = (Map<String, Object>) resourceAccess.getOrDefault("backend", Map.of());
//            final var confidentialClientRoles = (Collection<String>) confidentialClientAccess.getOrDefault("roles", List.of());
//            final var publicClientAccess = (Map<String, Object>) resourceAccess.getOrDefault("other-client", Map.of());
//            final var publicClientRoles = (Collection<String>) publicClientAccess.getOrDefault("roles", List.of());
//
//            return Stream.concat(realmRoles.stream(), Stream.concat(confidentialClientRoles.stream(), publicClientRoles.stream()))
//                    .map(SimpleGrantedAuthority::new).toList();
//        };
//    }
//
//    public interface Jwt2AuthenticationConverter extends Converter<Jwt, AbstractAuthenticationToken> {
//    }
//
//    @Bean
//    public Jwt2AuthenticationConverter authenticationConverter(Jwt2AuthoritiesConverter authoritiesConverter) {
//        return jwt -> new JwtAuthenticationToken(jwt, authoritiesConverter.convert(jwt));
//    }
//}
