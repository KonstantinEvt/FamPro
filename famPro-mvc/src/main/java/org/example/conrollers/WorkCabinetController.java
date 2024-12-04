package org.example.conrollers;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.TokenUser;
import com.example.enums.CheckStatus;
import com.example.exceptions.ModeratingContent;
import com.example.exceptions.RightsIsAbsent;
import lombok.AllArgsConstructor;
import org.example.models.OnlineUserHolder;
import org.example.models.SimpleUserInfo;
import org.example.services.BaseService;
import org.example.services.TokenService;
import org.example.texts.DescriptionHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;


@Controller
@RequestMapping("/cabinet")
@AllArgsConstructor
public class WorkCabinetController {
    private TokenService tokenService;
    private OnlineUserHolder onlineUserHolder;
    private final BaseService baseService;
    private WebClient webClient;

    @GetMapping("/me")
    public String getWelcome(ModelMap model) {
        SimpleUserInfo simpleUserInfo = onlineUserHolder.getSimpleUser();
        model.addAttribute("nickname", simpleUserInfo.getNickName());
        model.addAttribute("roles", simpleUserInfo.getRole());
        model.addAttribute("name", simpleUserInfo.getFullName());
        if (simpleUserInfo.getLocalisation().equals("loc=ru"))
            return "WelcomeRu";
        else return "Welcome";
    }

    @GetMapping("/rules")
    public String getRules(ModelMap model) {
        SimpleUserInfo simpleUserInfo = onlineUserHolder.getSimpleUser();
        model.addAttribute("nickname", simpleUserInfo.getNickName());
        model.addAttribute("roles", simpleUserInfo.getRole());
        if (simpleUserInfo.getLocalisation().equals("loc=ru"))
            return "RuRules";
        else return "Rules";
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
        SimpleUserInfo simpleUserInfo = onlineUserHolder.getSimpleUser();
        tokenService.editUser(tokenUser);
        onlineUserHolder.addUser(simpleUserInfo.editUser(tokenUser));
        return "redirect:http/localhost:9898/logout";
    }

    @PreAuthorize("hasAuthority('BaseUser')")
    @ResponseBody
    @GetMapping("/link/{id}")
    public void linkingUser(@PathVariable("id") Long id) {
        System.out.println("lets go");
        FamilyMemberDto dto = baseService.getFamilyMemberById(id, "loc=en");
        if (dto.getCheckStatus() == CheckStatus.LINKED)
            throw new RightsIsAbsent("Запись уже связана. Если Вы претендуете на нее - обратитесь к администрации");
        else if (dto.getCheckStatus() == CheckStatus.MODERATE)
            throw new ModeratingContent("Запись находится на модерации. Если Вы претендуете на нее - обратитесь к администрации");
        System.out.println("связываем с Клоаком");
        tokenService.linkUser(dto);
        System.out.println("связываем с базой");
        baseService.linkFamilyMember(id);
    }

    @GetMapping("/token")
    @ResponseBody
    public Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication();

    }
//    @RequestMapping(method = RequestMethod.POST, value = "/tokens/revoke/{tokenId:.*}")
//    @ResponseBody
//    public String revokeToken(@PathVariable String tokenId) {
//        tokenServices.revokeToken(tokenId);
//        return tokenId;
//    }

    @PostMapping("/localisation")
    public String chooseLocalisation(@RequestBody String localisation, ModelMap model) {
        SimpleUserInfo simpleUserInfo = onlineUserHolder.getSimpleUser();
        tokenService.chooseLocalisation(localisation);
        simpleUserInfo.setLocalisation(localisation);
        model.addAttribute("nickname", simpleUserInfo.getNickName());
        model.addAttribute("roles", simpleUserInfo.getRole());
        model.addAttribute("name", simpleUserInfo.getFullName());
//        webClient
//                .get()
//                .uri("http://localhost:9898/refresh")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(tokenUser)
//                .retrieve()
//                .toBodilessEntity()
//                .block();
        if (simpleUserInfo.getLocalisation().equals("loc=ru"))
            return "WelcomeRu";
        else return "Welcome";
    }
}
