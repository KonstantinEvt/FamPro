package org.example.conrollers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class frontController {
    @GetMapping("/token")
    public Authentication whoAmI() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication();

    }
}
