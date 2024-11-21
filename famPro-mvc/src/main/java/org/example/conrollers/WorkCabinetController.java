package org.example.conrollers;

import com.example.dtos.TokenUser;
import lombok.AllArgsConstructor;
import org.example.models.SimpleUserInfo;
import org.example.services.TokenService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/cabinet")
@AllArgsConstructor
public class WorkCabinetController {
    private TokenService tokenService;
    private SimpleUserInfo simpleUserInfo;

    @GetMapping("/me")
    public String getWelcome(ModelMap model) {
        if (!simpleUserInfo.getOverCross()) simpleUserInfo.setUp();
        model.addAttribute("nickname", simpleUserInfo.getNickName());
        model.addAttribute("roles", simpleUserInfo.getRole());
        model.addAttribute("name",simpleUserInfo.getFullName());
        if (simpleUserInfo.getLocalisation().equals("loc=ru"))
            return "WelcomeRu";
        else return "Welcome";
    }

    @GetMapping("/create")
    public String createUser() {

        return "Create-user";
    }

    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping("/create")
    public String createUser(@RequestBody TokenUser tokenUser) {
        tokenService.addUser(tokenUser);
        return "Welcome";
    }

    @GetMapping("/edit")
    public String editUser() {
        return "Edit-user";
    }

    @PostMapping("/edit")
    public String editUser(@RequestBody TokenUser tokenUser) {
        tokenService.editUser(tokenUser);
        return "redirect:http/localhost:9898/logout";
    }

    @GetMapping("/token")
    @ResponseBody
    public Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication();

    }


    @PostMapping("/localisation")
    public String chooseLocalisation(@RequestBody String localisation, ModelMap model) {
        tokenService.chooseLocalisation(localisation);
        if (!simpleUserInfo.getOverCross()) simpleUserInfo.setUp();
        simpleUserInfo.setLocalisation(localisation);
        model.addAttribute("nickname", simpleUserInfo.getNickName());
        model.addAttribute("roles", simpleUserInfo.getRole());
        model.addAttribute("name",simpleUserInfo.getFullName());
        if (simpleUserInfo.getLocalisation().equals("loc=ru"))
            return "WelcomeRu";
        else return "Welcome";
    }
}
