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
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/onlineUserAPI")
@AllArgsConstructor
public class CabinetController {
    private TokenService tokenService;
    private OnlineUserHolder onlineUserHolder;
    private final BaseService baseService;

    @GetMapping("/info")
    public SimpleUserInfo getOnlineUser() {
        System.out.println("tyt");
        return onlineUserHolder.getSimpleUser();
    }

     @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody TokenUser tokenUser) {
        if (onlineUserHolder.getSimpleUser().getRole().equals("Admin")) tokenService.addUser(tokenUser);
        else return ResponseEntity.ok("Your are haven't rights");
        return ResponseEntity.ok("New User added to SSO");
    }

    @PostMapping("/edit")
    public ResponseEntity<String> editUser(@RequestBody TokenUser tokenUser) {
        SimpleUserInfo simpleUserInfo = onlineUserHolder.getSimpleUser();
        tokenService.editUser(tokenUser);
        onlineUserHolder.addUser(simpleUserInfo.editUser(tokenUser));
        return ResponseEntity.ok("You are update in SSO");
    }
//

    @GetMapping("/link/{id}")
    public ResponseEntity<String> linkingUser(@PathVariable("id") Long id) {
        System.out.println("lets go");
        FamilyMemberDto dto = baseService.getFamilyMemberById(id, "en");
        if (dto.getCheckStatus() == CheckStatus.LINKED)
            throw new RightsIsAbsent("Запись уже связана. Если Вы претендуете на нее - обратитесь к администрации");
        else if (dto.getCheckStatus() == CheckStatus.MODERATE)
            throw new ModeratingContent("Запись находится на модерации. Если Вы претендуете на нее - обратитесь к администрации");
        System.out.println("связываем с Клоаком");
        tokenService.linkUser(dto);
        System.out.println("связываем с базой");
        baseService.linkFamilyMember(id);
        onlineUserHolder.getSimpleUser().editUser(dto);
    return ResponseEntity.ok("Вы успешно связаны с челоеком c Id: "+id);
    }
}
