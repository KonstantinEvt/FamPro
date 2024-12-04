package org.example.controllers;

import lombok.AllArgsConstructor;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

@RestController
@RequestMapping("/")
//@AllArgsConstructor

public class GateController {
//    @Value("${spring.security.oauth2.client.provider.keycloak.token-uri}")
//    private String refreshURL;
//   private WebClient webClient;
//@Value("${spring.token-uri}")
//private String tokenUri;
//    @Value("${spring.revoke-uri}")
//    private String revokeUri;
//    @Value("${spring.logout-uri}")
//    private String logoutUri;

    @GetMapping(value = "/token")
    public Mono<String> getHome(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) throws URISyntaxException {
        return Mono.just(authorizedClient.getAccessToken().getTokenValue());
    }
//    @GetMapping(value = "/refresh")
//    public void refresh(){
//
//        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//        formData.put("grant_type", Collections.singletonList("refresh_token"));
//        formData.put("refresh_token", Collections.singletonList("baeldung-api"));
//
////        webClient.post()
//                .uri(refreshURL)
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .header("Authorization","Basic"+SecurityContextHolder.getContext().getAuthentication().toString())
//                .bodyValue(BodyInserters.fromFormData(formData))
//                .retrieve()
//                .toBodilessEntity()
//                .block();
//@GetMapping(value = "/token1")
//public void shouldObtainAccessToken()
//        throws URISyntaxException
//{
//    System.out.println("hjh");
//    URI authorizationURI = new URIBuilder(logoutUri).build();
//    URI revokeURI = new URIBuilder(revokeUri).build();
//    WebClient webclient = WebClient.builder().build();
//    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//    formData.put("grant_type", Collections.singletonList("password"));
//    formData.put("client_id", Collections.singletonList("backend"));
//    formData.put("client_secret", Collections.singletonList("rxOFO2qeHqWMkcvjIkLPV9MTL0iPnfDl"));
//    formData.put("scope", Collections.singletonList("openid"));
//    formData.put("username", Collections.singletonList("testuser2"));
//    formData.put("password", Collections.singletonList("test"));

//    Mono<String> result1 = webclient.post()
//            .uri(revokeURI)
//            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//            .body(BodyInserters.fromFormData(formData))
//            .retrieve()
//            .bodyToMono(String.class);
//    JacksonJsonParser jsonParser = new JacksonJsonParser();
//    System.out.println(jsonParser.parseMap(String.valueOf(result1))
//            .get("access_token")
//            .toString());
//    Mono<String> result2 = webclient.post()
//            .uri(logoutUri)
//            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//            .body(BodyInserters.fromFormData(formData))
//            .retrieve()
//            .bodyToMono(String.class);
//    System.out.println(jsonParser.parseMap(String.valueOf(result2))
//            .get("access_token")
//            .toString());
//    String accessToken;
//    Mono<String> result = webclient.post()
//            .uri(tokenUri)
//            .header("Content-Type:application/x-www-form-urlencoded")
//            .body(BodyInserters.fromFormData(formData))
//            .retrieve()
//            .bodyToMono(String.class);
//    JacksonJsonParser jsonParser = new JacksonJsonParser();
//    accessToken = jsonParser.parseMap(result)
//            .get("access_token")
//            .toString();;
//            Authentication authentication= new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,accessToken);
//SecurityContextHolder.getContext().setAuthentication(authentication);
}


