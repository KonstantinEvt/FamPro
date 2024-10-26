package org.example.conrollers;

import lombok.AllArgsConstructor;
import org.example.dtos.TokenUser;
import org.example.services.KeyCloakService;
import org.example.services.TokenService;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/work")
@AllArgsConstructor
public class FrontContr {
    private TokenService tokenService;
    private KeyCloakService keyCloakService;


    @GetMapping("/token")
    public Authentication whoAmI() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication();
    }

    @GetMapping("/me")
    public String getWelcome(ModelMap model) {
        model.addAllAttributes(tokenService.getTokenUser().getClaims());
        return "welcome";
    }
    @GetMapping("/create")
    public String createUser() {
        return "create-user";
    }

    @PostMapping("/create")
    public String createUser(@RequestBody TokenUser userRequestDTO) {
        keyCloakService.addUser(userRequestDTO);
        return "redirect:/work/me";
    }

}
