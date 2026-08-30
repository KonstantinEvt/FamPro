package ru.memman.controllers;

import ru.memman.dtos.OnlineUserDto;
import ru.memman.service.ActualUserService;
import ru.memman.service.OnlineUserService;
import ru.memman.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/inline")
@AllArgsConstructor
public class OnlineUserController {
    private TokenService tokenService;
    private OnlineUserService onlineUserService;
@GetMapping("/get")
    public Mono<OnlineUserDto> getInlineUser(){
    return tokenService.getOnlineUser();
}
    @GetMapping("/getSimple")
    public Mono<OnlineUserDto> getCount(){
        return Mono.just(new OnlineUserDto());
    }

}
