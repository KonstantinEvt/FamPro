package ru.memman.conrollers;

import lombok.extern.log4j.Log4j2;
import ru.memman.dtos.FamilyMemberDto;
import ru.memman.dtos.SecurityDto;
import ru.memman.enums.Localisation;
import ru.memman.enums.SecretLevel;
import ru.memman.services.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.memman.services.TokenService;

import java.util.Collection;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/frontAPI/base")
public class BaseController {
    private final BaseService baseService;
    private TokenService tokenService;

    @PostMapping("/{id}/{localisation}")
    public ResponseEntity<FamilyMemberDto> getFamilyMemberById(@PathVariable("id") Long id, @PathVariable("localisation") String localisation) {
        return ResponseEntity.status(200).header("resultFindStatus","Person is found").body(baseService.getFamilyMemberById(id, tokenService.getLocalisation(localisation)));
    }

    @PostMapping("/")
    public FamilyMemberDto getFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        return baseService.getFamilyMember(familyMemberDto);
    }

    @PostMapping("/add")
    public ResponseEntity<FamilyMemberDto> addFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        FamilyMemberDto dto=baseService.addFamilyMember(familyMemberDto);
        log.info(dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("edit")
    public ResponseEntity<FamilyMemberDto> editFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        FamilyMemberDto dto=baseService.editFamilyMember(familyMemberDto);
       log.info(dto);
        return ResponseEntity.ok(dto);
    }
    @PostMapping("/i/{localisation}")
    public FamilyMemberDto getYourself(@PathVariable("localisation")Localisation localisation) {
        return baseService.getYourself(localisation);
    }

    @PostMapping("/get/extended")
    public FamilyMemberDto getFullFamilyMember(@RequestBody SecurityDto securityDto) {
        return baseService.getExtendedInfoFamilyMember(securityDto);
    }
    @PostMapping("/getFirstCreator/{localisation}")
    public Collection<FamilyMemberDto> getMembersByFirstCreator(@PathVariable("localisation")Localisation localisation) {
        return baseService.getMembersByFirstCreator(localisation);
    }
    @PostMapping("/getFamilyTree/{uuid}/{choice}/{localisation}")
    public Collection<FamilyMemberDto> getFamilyTreeOfMember(@PathVariable("uuid") UUID uuid, @PathVariable("choice") SecretLevel choice, @PathVariable("localisation")Localisation localisation) {
        return baseService.getFamilyTreeOfMember(uuid, choice, localisation);
    }
}
