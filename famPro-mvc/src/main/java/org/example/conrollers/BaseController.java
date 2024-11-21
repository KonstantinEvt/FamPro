package org.example.conrollers;

import com.example.dtos.FamilyMemberDto;
import lombok.AllArgsConstructor;
import org.example.services.BaseService;
import org.example.models.SimpleUserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/base")
public class BaseController {
    private final SimpleUserInfo simpleUserInfo;
    private final BaseService baseService;

    @GetMapping("")
    public String getBaseForm(Model model) {
        model.addAttribute("nickname", simpleUserInfo.getNickName());
        if (simpleUserInfo.getLocalisation().equals("loc=ru"))
            return "Connect-to-base-ru";
        else return "Connect-to-base";
    }

    @GetMapping("/family_member")
    public String getFamilyMember() {
        return "GetFamilyMember";
    }

    @ResponseBody
    @PostMapping("/family_member/{id}")
    public FamilyMemberDto getFamilyMemberById(@PathVariable("id") Long id) {
        return baseService.getFamilyMemberById(id);
    }

    @ResponseBody
    @PostMapping("/family_member/")
    public FamilyMemberDto getFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        familyMemberDto.setLocalisation(simpleUserInfo.getLocalisation());

        return baseService.getFamilyMember(familyMemberDto);
    }

    @GetMapping("/family_member/add")
    public String createFamilyMember() {
        return "CreateNewFamilyMember";
    }

    @ResponseBody
    @PostMapping("/family_member/add")
    public ResponseEntity<String> addFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        familyMemberDto.setLocalisation(simpleUserInfo.getLocalisation());
        baseService.addFamilyMember(familyMemberDto);
        return ResponseEntity.ok("Person is saved");
    }
    @GetMapping("/family_member/edit")
    public String editFamilyMember() {
        return "EditFamilyMember";
    }
    @ResponseBody
    @PostMapping("/family_member/edit")
    public ResponseEntity<String> editFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        familyMemberDto.setLocalisation(simpleUserInfo.getLocalisation());
        baseService.editFamilyMember(familyMemberDto);
        return ResponseEntity.ok("Person is update");
    }
}
