package ru.memman.conrollers;

import ru.memman.dtos.FamilyMemberDto;
import ru.memman.dtos.TokenUser;
import ru.memman.enums.CheckStatus;
import ru.memman.enums.UserRole;
import ru.memman.exceptions.ModeratingContent;
import ru.memman.exceptions.RightsIsAbsent;
import ru.memman.services.BaseService;
import lombok.AllArgsConstructor;
import ru.memman.services.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/frontAPI/sso")
@AllArgsConstructor
public class SSOController {
    private TokenService tokenService;
    private final BaseService baseService;


    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody TokenUser tokenUser) {
        UserRole role=tokenService.getPriorityRole(tokenUser);
        if (role== UserRole.ADMIN||role==UserRole.MANAGER) tokenService.addUser(tokenUser);
        else return ResponseEntity.ok("Your are haven't rights");
        return ResponseEntity.ok("New User added to SSO");
    }

    @PostMapping("/edit")
    public ResponseEntity<String> editUser(@RequestBody TokenUser tokenUser) {
        UserRole role=tokenService.getPriorityRole(tokenUser);
        if (role== UserRole.ADMIN||role==UserRole.MANAGER) tokenService.editUser(tokenUser);
        return ResponseEntity.ok("You are update in SSO");
    }
//

    @GetMapping("/link/{id}/{localisation}")
    public ResponseEntity<String> linkingUser(@PathVariable("id") Long id, @PathVariable("localisation") String localisation) {
        FamilyMemberDto dto = baseService.getFamilyMemberById(id, tokenService.getLocalisation(localisation));
        if (dto.getCheckStatus() == CheckStatus.LINKED)
            throw new RightsIsAbsent("Запись уже связана. Если Вы претендуете на нее - обратитесь к администрации");
        else if (dto.getCheckStatus() == CheckStatus.MODERATE)
            throw new ModeratingContent("Запись находится на модерации. Если Вы претендуете на нее - обратитесь к администрации");

        if (baseService.linkFamilyMember(dto) == CheckStatus.LINKED) {
            tokenService.linkUser(dto);
            return ResponseEntity.ok("Вы успешно связаны с челоеком c Id: " + id);
        }
        return ResponseEntity.ok("Запрос на связь отправлен");
    }
}
