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
    private OnlineUserService onlineUserService;

    @GetMapping("/get")
    public OnlineUserDto getInlineUser() {
        return onlineUserService.getOnlineUser();
    }
    @GetMapping("/out")
    public void setOffline() {
        onlineUserService.setOffline();
    }
}
