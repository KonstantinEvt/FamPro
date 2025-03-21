package com.example.controllers;

import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import com.example.holders.FrontBackgroundHolder;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/")
@AllArgsConstructor

public class GateController {

private FrontBackgroundHolder frontBackgroundHolder;
//    @GetMapping(value = "/token")
//    public Mono<String> getHome(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
//        return Mono.just(authorizedClient.getAccessToken().getTokenValue());
//    }
//    @GetMapping(value = "/token1")
//    public Mono<Authentication> getHome1() {
//        SecurityContext context = SecurityContextHolder.getContext();
//        return Mono.just(context.getAuthentication());
//    }
    @GetMapping(value = "/image",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public Mono<byte[]> getStart() {
        return Mono.just(frontBackgroundHolder.getPicture());
    }
    @GetMapping(value = "/favicon",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public Mono<byte[]> getFavicon() {
        return Mono.just(frontBackgroundHolder.getFavicon());
    }
}


