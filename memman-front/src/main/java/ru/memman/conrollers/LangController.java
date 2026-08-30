package ru.memman.conrollers;

import lombok.extern.log4j.Log4j2;
import ru.memman.models.ContentDto;
import ru.memman.services.TextSelector;
import ru.memman.services.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/front/languish")
@AllArgsConstructor
@Log4j2
public class LangController {

    private TokenService tokenService;
    private TextSelector textSelector;

    @GetMapping("/get")
    public ResponseEntity<String> getRegisterLang() {
        return ResponseEntity.status(200)
                .body((String) tokenService.getTokenUser().getClaims().get("locale"));
    }

    @GetMapping("/set")
    public ResponseEntity<String> setLocalisation(@RequestParam(value = "loc") String loc) {
        return ResponseEntity.status(200)
                .body(tokenService.setGlobalLocalisation(loc));
    }
    @PostMapping(value = "/beginLoad/{currentLang}")
    public ResponseEntity<ContentDto> beginLoader(@PathVariable("currentLang") String currentLang) {
        return ResponseEntity.status(200)
                .body(textSelector.getContentDto(currentLang));
}
}


