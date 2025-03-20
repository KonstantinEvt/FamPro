package com.example.conrollers;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.TokenUser;
import com.example.enums.CheckStatus;
import com.example.exceptions.ModeratingContent;
import com.example.exceptions.RightsIsAbsent;
import com.example.services.BaseService;
import lombok.AllArgsConstructor;
import com.example.models.OnlineUserHolder;
import com.example.models.SimpleUserInfo;
import com.example.services.TokenService;
import org.springframework.http.ResponseEntity;
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
        FamilyMemberDto dto = baseService.getFamilyMemberById(id, onlineUserHolder.getSimpleUser().getLocalisation());
        if (dto.getCheckStatus() == CheckStatus.LINKED)
            throw new RightsIsAbsent("Запись уже связана. Если Вы претендуете на нее - обратитесь к администрации");
        else if (dto.getCheckStatus() == CheckStatus.MODERATE)
            throw new ModeratingContent("Запись находится на модерации. Если Вы претендуете на нее - обратитесь к администрации");
        System.out.println("связываем с Клоаком");
        tokenService.linkUser(dto);
        System.out.println("связываем с базой");
        baseService.linkFamilyMember(dto);
        onlineUserHolder.getSimpleUser().editUser(dto);
    return ResponseEntity.ok("Вы успешно связаны с челоеком c Id: "+id);
    }
}
