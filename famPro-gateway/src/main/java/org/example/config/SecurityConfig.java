package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.*;

import java.net.URI;
import java.net.http.HttpHeaders;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Value("${application.gate}")
    private String gateURL;

    @Value("${server.port}")
    private String port;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http, ServerLogoutSuccessHandler handler) {

        http
                .authorizeExchange(auth -> auth
                        .pathMatchers("/").permitAll()
                        .pathMatchers("/image/**").permitAll()
                        .pathMatchers("/resources/**").permitAll()
                        .anyExchange().authenticated())
                .oauth2Login(withDefaults())
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));

        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        http.logout(logout -> logout.logoutHandler(logoutHandler()).logoutSuccessHandler(handler));

        return http.build();
    }

    @Bean
    ServerLogoutSuccessHandler oidcLogoutSuccessHandler(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        OidcClientInitiatedServerLogoutSuccessHandler successHandler = new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
        successHandler.setPostLogoutRedirectUri(URI.create(gateURL+":"+port).toString());
        return successHandler;
    }

    private ServerLogoutHandler logoutHandler() {
        return new DelegatingServerLogoutHandler(new WebSessionServerLogoutHandler(), new SecurityContextServerLogoutHandler());
    }

    @Bean
    KeyResolver authUserKeyResolver() {

        return exchange -> ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication()
                        .getPrincipal().toString());
    }

}

