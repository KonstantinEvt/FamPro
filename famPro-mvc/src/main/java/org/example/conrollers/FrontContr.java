package org.example.conrollers;

import com.example.dtos.TokenUser;
import lombok.AllArgsConstructor;
import org.example.services.TokenService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/work")
@AllArgsConstructor
public class FrontContr {
    private TokenService tokenService;


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

    @PreAuthorize("hasAuthority('BaseUser')")
    @PostMapping("/create")
    public String createUser(@RequestBody TokenUser tokenUser) {
        tokenService.addUser(tokenUser);
        return "redirect:welcome";
    }

    @GetMapping("/pr")
    public String getToken() {
        tokenService.getToken();
        return "welcome";
    }
}
