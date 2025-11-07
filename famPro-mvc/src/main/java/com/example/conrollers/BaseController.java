package com.example.conrollers;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.SecurityDto;
import com.example.models.OnlineUserHolder;
import com.example.models.SimpleUserInfo;
import com.example.services.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/base")
public class BaseController {
    private final BaseService baseService;
    private OnlineUserHolder onlineUserHolder;

    @PostMapping("/family_member/{id}")
    public ResponseEntity<FamilyMemberDto> getFamilyMemberById(@PathVariable("id") Long id) {
        SimpleUserInfo simpleUserInfo=onlineUserHolder.getSimpleUser();
        return ResponseEntity.status(200).header("resultFindStatus","Person is found").body(baseService.getFamilyMemberById(id, simpleUserInfo.getLocalisation()));
    }

    @PostMapping("/family_member/")
    public FamilyMemberDto getFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        SimpleUserInfo simpleUserInfo=onlineUserHolder.getSimpleUser();
        familyMemberDto.setLocalisation(simpleUserInfo.getLocalisation());
        return baseService.getFamilyMember(familyMemberDto);
    }

    @PostMapping("/family_member/add")
    public ResponseEntity<String> addFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        SimpleUserInfo simpleUserInfo=onlineUserHolder.getSimpleUser();
        familyMemberDto.setLocalisation(simpleUserInfo.getLocalisation());
        System.out.println(familyMemberDto);
        baseService.addFamilyMember(familyMemberDto);
        return ResponseEntity.ok("Person is saved");
    }

    @PostMapping("/family_member/edit")
    public ResponseEntity<String> editFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        SimpleUserInfo simpleUserInfo=onlineUserHolder.getSimpleUser();
        familyMemberDto.setLocalisation(simpleUserInfo.getLocalisation());
        baseService.editFamilyMember(familyMemberDto);
        return ResponseEntity.ok("Person is update");
    }
    @PostMapping("/family_member/i")
    public FamilyMemberDto getYourself() {
        return baseService.getYourself(onlineUserHolder.getSimpleUser().getLocalisation());
    }

    @PostMapping("/family_member/get/extended")
    public FamilyMemberDto getFullFamilyMember(@RequestBody SecurityDto securityDto) {
        return baseService.getExtendedInfoFamilyMember(securityDto, onlineUserHolder.getSimpleUser().getLocalisation());
    }
}
