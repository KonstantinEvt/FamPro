package ru.memman.controllers;

import ru.memman.dtos.FamilyMemberDto;
import ru.memman.dtos.SecurityDto;
import ru.memman.service.FamilyMemberService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/storage/database")
public class FamilyMemberController {
    private final FamilyMemberService familyMemberService;

    @GetMapping("/{id}")
    public FamilyMemberDto getFamilyMember(@PathVariable Long id) {
        return familyMemberService.getFamilyMemberById(id);
    }

    //    @GetMapping("/database/link/{id}")
//    public FamilyMemberDto linkFamilyMember(@PathVariable Long id) {
//        return familyMemberService.linkFamilyMember(id);
//    }
    @GetMapping("/i")
    public FamilyMemberDto getYourself() {
        return familyMemberService.getYourself();
    }

    @PostMapping("/add")
    public ResponseEntity<FamilyMemberDto> addFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        FamilyMemberDto dto = familyMemberService.addFamilyMember(familyMemberDto);
        System.out.println(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/all")
    public Collection<FamilyMemberDto> getAllFamilyMember() {
        return familyMemberService.getAllFamilyMembers();
    }

    @PostMapping("/get")
    public FamilyMemberDto getFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        return familyMemberService.getFamilyMember(familyMemberDto);
    }

    @DeleteMapping("/{id}")
    public String removeFamilyMember(@PathVariable Long id) {
        return familyMemberService.removeFamilyMember(id);
    }

    @PostMapping("/edit")
    public ResponseEntity<FamilyMemberDto> editFamilyMember(@RequestBody FamilyMemberDto familyMemberDto) {
        return ResponseEntity.ok(familyMemberService.updateFamilyMember(familyMemberDto));
    }

    @PostMapping("/extended")
    public FamilyMemberDto getFullFamilyMember(@RequestBody SecurityDto securityDto) {
        log.info("request for extension {}", securityDto);
        FamilyMemberDto dto = familyMemberService.getExtendedInfoFamilyMember(securityDto);
        log.info("Result of extension: {}", dto);
        return dto;
    }
}
