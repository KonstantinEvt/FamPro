package org.example.feign;

import com.example.dtos.TokenUser;
import org.example.config.FeignRequestIntercepter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "FAMPRO-KEY",configuration = FeignRequestIntercepter.class)
public interface KeyCloakManageClient {

    @PostMapping("/manage/add")
    TokenUser addUser(@RequestBody TokenUser tokenUser);
//
//    @GetMapping("/database")
//    Collection<FamilyMemberDto> getAllFamilyMembers();
//
//    @DeleteMapping("/database/{id}")
//    String removeFamilyMember(@PathVariable Long id);
//
//    @PatchMapping("/database")
//    FamilyMemberDto updateFamilyMember(@RequestBody FamilyMemberDto familyMemberDto);
//    @GetMapping("/database/save{filename}")
//    ResponseEntity<String> saveDataToFile(@PathVariable String filename);
//
//    @GetMapping("/database/recover{filename}")
//    ResponseEntity<String> recoverBaseFromFile(@PathVariable String filename);
}
