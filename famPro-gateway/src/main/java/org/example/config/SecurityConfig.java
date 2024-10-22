package org.example.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
//    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
//    String jwtLink;
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(auth -> auth.anyExchange().authenticated())
                .oauth2Login(withDefaults())
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));


        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }
    @Bean
    KeyResolver authUserKeyResolver() {
        return exchange -> ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication()
                        .getPrincipal().toString());
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .filter(new ServletBearerExchangeFilterFunction())
                .build();
    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable);
////        http.requiresChannel(c -> c.requestMatchers("/api/me").requiresInsecure());
//        http.authorizeHttpRequests(c -> c.requestMatchers("/api/me").permitAll())
////                       .requestMatchers("/eureka/**").permitAll());
//                .authorizeHttpRequests((authorize) -> authorize
//                        .anyRequest().authenticated())
//                .oauth2ResourceServer((oauth2) -> oauth2
//                        .jwt(jwtConfigurer -> jwtConfigurer.jwkSetUri(jwtLink)));
//        return http.build();
//    }
//    @Bean
//    public JwtDecoder jwtDecoder() {
//        return JwtDecoders.fromIssuerLocation(jwtLink);
//    }
}

