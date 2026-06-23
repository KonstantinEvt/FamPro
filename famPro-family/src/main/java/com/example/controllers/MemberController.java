package com.example.controllers;

import com.example.dtos.FamilyMemberDto;
import com.example.enums.SecretLevel;
import com.example.service.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private FacadeService facadeService;
    private TokenService tokenService;
    private GuardService guardService;
    private MemberService memberService;
    private TreeService treeService;

    @PostMapping("/firstCreator")
    public Collection<FamilyMemberDto> getAllMembersByFirstCreator() {
        return memberService.getMembersByFirstCreator((String) tokenService.getTokenUser().getClaims().get("sub"));
    }
    @PostMapping("/familyTree/{uuid}/{choice}")
    public Collection<FamilyMemberDto> getFamilyTreeOfMember(@PathVariable("uuid") UUID uuid, @PathVariable("choice") SecretLevel choice) {
        return treeService.getFamilyTreeOfMember(uuid, choice);
    }
}
