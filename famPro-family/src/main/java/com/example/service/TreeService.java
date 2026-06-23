package com.example.service;

import com.example.dtos.FamilyMemberDto;
import com.example.entity.Guard;
import com.example.entity.ShortFamilyMember;
import com.example.enums.*;
import com.example.mappers.FamilyMemberMapper;
import com.example.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class TreeService implements SimpleFamilyService {
    private MemberService memberService;
    private MemberRepository memberRepository;
    private GuardService guardService;
    private FamilyMemberMapper familyMemberMapper;
    private TokenService tokenService;

    @Transactional(readOnly = true)
    public Collection<FamilyMemberDto> getFamilyTreeOfMember(UUID memberUuid, SecretLevel choice) {
        String guardUuid = (String) tokenService.getTokenUser().getClaims().get("sub");
        ShortFamilyMember member = memberRepository.getMemberByUuid(memberUuid).orElseThrow(() -> new RuntimeException("person not found"));
        SecretLevel birth = member.getSecretLevelBirthday();
        SecretLevel guardStatus;
        Set<String> roles = tokenService.getTokenUser().getRoles();
        if (roles.contains(UserRoles.ADMIN.getNameSSO()) || roles.contains(UserRoles.MANAGER.getNameSSO())) {
            Set<ShortFamilyMember> topAnc = memberService.getAllTopAncestors(member);
            memberService.getExtendedTopAncestors(topAnc);
            return setRelationsAndMapToDto(member, memberService.getGeneticTreeMembers(topAnc, false), choice);
        }
        if (member.getCreator() != null && member.getLinkGuard() == null
                && (Objects.equals(member.getCreator(), guardUuid)
                || member.getSecretLevelBirthday() == SecretLevel.OPEN))
            guardStatus = SecretLevel.CLOSE;
        else if (member.getCreator() != null && member.getLinkGuard() == null)
            return new ArrayList<>();
        else if (roles.contains(UserRoles.LINKED_USER.getNameSSO())) {
            Optional<Guard> guardFromToken = guardService.findGuard(guardUuid);
            if (guardFromToken.isEmpty()) {
                log.warn("guard exception!");
                guardStatus = SecretLevel.OPEN;
            } else {
                SecretLevel max = memberService.getMaxSecretLevelForMember(member, null, false);
                if (birth.ordinal() > max.ordinal() && birth != SecretLevel.GENETIC_TREE) birth = max;
                guardStatus = memberService.getSecretStatus(member, UUID.fromString(guardUuid), null, false);
                if (guardStatus == SecretLevel.OPEN) guardStatus = SecretLevel.GENETIC_TREE;
            }
        } else guardStatus = SecretLevel.OPEN;
        log.info(guardStatus.name());
        log.info(birth.name());
        if (birth.ordinal() > guardStatus.ordinal()) return new ArrayList<>();

        Set<ShortFamilyMember> topAnc = memberService.getAllTopAncestors(member);
        memberService.getExtendedTopAncestors(topAnc);
        Set<UUID> treeGuards = memberService.getGeneticTreeGuards(topAnc, false);
        if (guardStatus == SecretLevel.GENETIC_TREE
                && !treeGuards.contains(UUID.fromString(guardUuid))) {
            if (birth != SecretLevel.OPEN) return new ArrayList<>();
            guardStatus = SecretLevel.OPEN;
        }
        Set<ShortFamilyMember> members;
        boolean cont;

        Set<ShortFamilyMember> filter = new HashSet<>();
        if (choice == SecretLevel.STRAIGHT_BLOOD) {
            members = memberService.getGeneticTreeMembers(Set.of(member), false);
            cont = false;
            filter.add(member);
        } else {
            members = memberService.getGeneticTreeMembers(topAnc, false);
            cont = true;
        }
        Map<UUID, ShortFamilyMember> map = new HashMap<>();
        for (ShortFamilyMember mem :
                members) {
            map.put(mem.getUuid(), mem);

        }


        Set<ShortFamilyMember> activeMembers = new HashSet<>();
        Set<ShortFamilyMember> temp = new HashSet<>();
        Set<ShortFamilyMember> temp2;
        SecretLevel tempStatus;
        SecretLevel tempMax;
        activeMembers.add(member);
        while (cont) {
            for (ShortFamilyMember activeMember :
                    activeMembers) {
                filter.add(activeMember);
                if (guardStatus != SecretLevel.CLOSE && guardStatus != SecretLevel.OPEN) {
                    treeGuards = memberService.getGeneticTreeGuards(selectTopsOfMember(topAnc, activeMember), false);
                    tempStatus = memberService.getSecretStatus(activeMember, UUID.fromString(guardUuid), treeGuards, true);
                    tempMax = memberService.getMaxSecretLevelForMember(activeMember, treeGuards, true);
                    if (tempMax.ordinal() > activeMember.getSecretLevelBirthday().ordinal())
                        tempMax = activeMember.getSecretLevelBirthday();
                } else {
                    tempStatus = guardStatus;
                    tempMax = activeMember.getSecretLevelBirthday();
                }
                if (tempMax.ordinal() <= tempStatus.ordinal()) {
                    if (activeMember.getMotherUuid() != null) temp.add(map.get(activeMember.getMotherUuid()));
                    if (activeMember.getFatherUuid() != null) temp.add(map.get(activeMember.getFatherUuid()));
                    if (activeMember.getMotherUuid() == null && activeMember.getFatherUuid() == null) {
                        if (activeMember.getPrimaryMembers() != null && !activeMember.getPrimaryMembers().isBlank()) {
                            Set<UUID> topBrothers = getAllUuidFromInfo(activeMember.getPrimaryMembers());
                            for (UUID brother :
                                    topBrothers) {
                                filter.add(map.get(brother));
                            }
                        }
                    }
                } else {
                    activeMember.setSecretLevelBirthday(SecretLevel.CLOSE);
                }
            }
            if (!temp.isEmpty()) {
                activeMembers.clear();
                temp2 = activeMembers;
                activeMembers = temp;
                temp = temp2;
            } else cont = false;
        }
        if (choice == SecretLevel.ANCESTOR) {
            filter.remove(member);
            return setRelationsAndMapToDto(member, filter, choice);
        }
        members.removeAll(filter);
        Set<UUID> filterUuids = filter.stream().map(ShortFamilyMember::getUuid).collect(Collectors.toSet());

        for (ShortFamilyMember mem :
                members) {
            tempMax = mem.getSecretLevelBirthday();
            if (mem.getCreator() == null || mem.getLinkGuard() != null) {
                treeGuards = memberService.getGeneticTreeGuards(selectTopsOfMember(topAnc, mem), false);
                tempStatus = memberService.getSecretStatus(mem, UUID.fromString(guardUuid), treeGuards, true);
                tempMax = memberService.getMaxSecretLevelForMember(mem, null, false);
                if (tempMax.ordinal() > mem.getSecretLevelBirthday().ordinal()) tempMax = mem.getSecretLevelBirthday();
            } else if (Objects.equals(mem.getCreator(), guardUuid)) tempStatus = SecretLevel.CLOSE;
            else tempStatus = SecretLevel.OPEN;
            if (tempMax.ordinal() > tempStatus.ordinal()
                    || (mem.getMotherUuid() == null && mem.getFatherUuid() == null))
                temp.add(mem);
        }
        Set<UUID> tempUuids = temp.stream().map(ShortFamilyMember::getUuid).collect(Collectors.toSet());
        members.removeAll(temp);
        Set<UUID> tempAllUuids = members.stream().map(ShortFamilyMember::getUuid).collect(Collectors.toSet());
        while (!members.isEmpty()) {
            for (ShortFamilyMember mem :
                    members) {
                UUID mother = mem.getMotherUuid();
                UUID farther = mem.getFatherUuid();
                if ((mother != null && filterUuids.contains(mother)) || (farther != null && filterUuids.contains(farther))) {
                    filterUuids.add(mem.getUuid());
                    filter.add(mem);
                    tempAllUuids.remove(mem.getUuid());
                } else {
                    if ((tempUuids.contains(mother) && (farther == null || !tempAllUuids.contains(farther)))
                            || (tempUuids.contains(farther) && (mother == null || !tempAllUuids.contains(mother)))) {
                        tempUuids.add(mem.getUuid());
                        temp.add(mem);
                        tempAllUuids.remove(mem.getUuid());
                    }
                }
            }

            members.removeAll(filter);
            members.removeAll(temp);
        }
        if (choice == SecretLevel.STRAIGHT_BLOOD) filter.remove(member);
        return setRelationsAndMapToDto(member, filter, choice);
    }

    private Set<ShortFamilyMember> selectTopsOfMember(Set<ShortFamilyMember> tops, ShortFamilyMember member) {
        Set<ShortFamilyMember> memberTops = new HashSet<>();
        if (member.getTopAncestors() == null || member.getTopAncestors().isBlank()) {
            if (!tops.contains(member)) return memberTops;
            memberTops.add(member);
            if (member.getPrimaryMembers() == null || member.getPrimaryMembers().isBlank()) return memberTops;
            Set<UUID> memberBrothers = getAllUuidFromInfo(member.getPrimaryMembers());
            for (ShortFamilyMember mem :
                    tops) {
                if (memberBrothers.contains(mem.getUuid())) memberTops.add(mem);
            }
            return memberTops;
        }
        Set<UUID> memberTopsUuids = getAllUuidFromInfo(member.getTopAncestors());

        for (ShortFamilyMember mem :
                tops) {
            if (memberTopsUuids.contains(mem.getUuid())) {
                memberTops.add(mem);
                if (mem.getPrimaryMembers() != null && !mem.getPrimaryMembers().isBlank()) {
                    Set<UUID> memberBrothers = getAllUuidFromInfo(mem.getPrimaryMembers());
                    for (ShortFamilyMember topBrother :
                            tops) {
                        if (memberBrothers.contains(topBrother.getUuid())) memberTops.add(mem);
                    }
                }
            }
        }
        return memberTops;
    }

    private Set<FamilyMemberDto> setRelationsAndMapToDto(ShortFamilyMember mainMember, Set<ShortFamilyMember> members, SecretLevel choice) {
        Set<FamilyMemberDto> result = new HashSet<>();
        UUID person = mainMember.getUuid();
        switch (choice) {
            case ANCESTOR -> {
                for (ShortFamilyMember member :
                        members) {
                    if (member.getFirstCreator() == null) continue;

                    if (mainMember.getMotherUuid() != null && Objects.equals(mainMember.getMotherUuid(), member.getUuid())) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation(Relation.MOTHER);
                        result.add(dto);
                        member.setFirstCreator("parent");
                        getSecondLevelRelations(member, members, result, person);

                    } else if (mainMember.getFatherUuid() != null && Objects.equals(mainMember.getFatherUuid(), member.getUuid())) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation(Relation.FATHER);
                        result.add(dto);
                        member.setFirstCreator("parent");
                        getSecondLevelRelations(member, members, result, person);
                    }
                }
                for (ShortFamilyMember member :
                        members) {
                    if (member.getFirstCreator() != null) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation(Relation.ANCESTOR);
                        result.add(dto);
                        member.setFirstCreator(null);
                    }
                }
            }
            case STRAIGHT_BLOOD -> {
                for (ShortFamilyMember member :
                        members) {
                    if (member.getFirstCreator() == null) continue;

                    if (member.getMotherUuid() != null && Objects.equals(member.getMotherUuid(), mainMember.getUuid())
                            || member.getFatherUuid() != null && Objects.equals(member.getFatherUuid(), mainMember.getUuid())) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation((member.getSex() == Sex.MALE) ? Relation.SON : Relation.DAUGHTER);
                        result.add(dto);
                        member.setFirstCreator("child");
                        getSecondLevelRelations(member, members, result, person);
                    }
                }
                for (ShortFamilyMember member :
                        members) {
                    if (member.getFirstCreator() != null) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation(Relation.DESCENDANT);
                        result.add(dto);
                        member.setFirstCreator(null);
                    }
                }
            }
            case GENETIC_TREE -> {

                for (ShortFamilyMember member :
                        members) {
                    if (member.getFirstCreator() == null) continue;

                    if (Objects.equals(member.getUuid(), person)) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation(Relation.PERSON);
                        result.add(dto);
                        member.setFirstCreator(null);
                    } else if (mainMember.getMotherUuid() != null && Objects.equals(mainMember.getMotherUuid(), member.getUuid())) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation(Relation.MOTHER);
                        result.add(dto);
                        member.setFirstCreator("parent");
                        getSecondLevelRelations(member, members, result, person);
                    } else if (mainMember.getFatherUuid() != null && Objects.equals(mainMember.getFatherUuid(), member.getUuid())) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation(Relation.FATHER);
                        result.add(dto);
                        member.setFirstCreator("parent");
                        getSecondLevelRelations(member, members, result, person);

                    } else if (member.getPrimaryMembers() != null && member.getPrimaryMembers().contains(person.toString())) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation((member.getSex() == Sex.MALE) ? Relation.BROTHER : Relation.SISTER);
                        result.add(dto);
                        member.setFirstCreator("brother");
                        getSecondLevelRelations(member, members, result, person);
                    } else if (member.getMotherUuid() != null && mainMember.getMotherUuid() != null && Objects.equals(member.getMotherUuid(), mainMember.getMotherUuid())) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation((member.getSex() == Sex.MALE) ? Relation.HALF_BROTHER_BY_MOTHER : Relation.HALF_SISTER_BY_MOTHER);
                        result.add(dto);
                        member.setFirstCreator("halfSister");
                        getSecondLevelRelations(member, members, result, person);
                    } else if (member.getFatherUuid() != null && mainMember.getFatherUuid() != null && Objects.equals(member.getFatherUuid(), mainMember.getFatherUuid())) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation((member.getSex() == Sex.MALE) ? Relation.HALF_BROTHER_BY_FATHER : Relation.HALF_SISTER_BY_FATHER);
                        result.add(dto);
                        member.setFirstCreator("halfBrother");
                        getSecondLevelRelations(member, members, result, person);
                    } else if (member.getMotherUuid() != null && Objects.equals(member.getMotherUuid(), person) || member.getFatherUuid() != null && Objects.equals(member.getFatherUuid(), person)) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation((member.getSex() == Sex.MALE) ? Relation.SON : Relation.DAUGHTER);
                        result.add(dto);
                        member.setFirstCreator("child");
                        getSecondLevelRelations(member, members, result, person);
                    }
                }
                for (ShortFamilyMember member :
                        members) {
                    if (member.getFirstCreator() == null) continue;
                    if (mainMember.getDescendants() != null && mainMember.getDescendants().contains(member.getUuid().toString())) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation(Relation.DESCENDANT);
                        result.add(dto);
                        member.setFirstCreator(null);
                    } else if (mainMember.getAncestors() != null && mainMember.getAncestors().contains(member.getUuid().toString())) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation(Relation.ANCESTOR);
                        result.add(dto);
                        member.setFirstCreator(null);
                    } else {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation(Relation.RELATIVE);
                        result.add(dto);
                        member.setFirstCreator(null);
                    }
                }
            }
            default -> log.warn("request not valid");
        }
        return result;
    }

    private void getSecondLevelRelations(ShortFamilyMember mainMember, Set<ShortFamilyMember> members, Set<FamilyMemberDto> result, UUID person) {
        for (ShortFamilyMember member :
                members) {
            if (member.getFirstCreator() == null
                    || Objects.equals(mainMember.getUuid(), member.getUuid())
                    || Objects.equals(person, member.getUuid()))
                continue;
            switch (mainMember.getFirstCreator()) {
                case "parent" -> {
                    if (mainMember.getMotherUuid() != null
                            && Objects.equals(mainMember.getMotherUuid(), member.getUuid())) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation(Relation.GRANDMOTHER);
                        result.add(dto);
                        member.setFirstCreator(null);
                    } else if (mainMember.getFatherUuid() != null && Objects.equals(mainMember.getFatherUuid(), member.getUuid())) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation(Relation.GRANDFATHER);
                        result.add(dto);
                        member.setFirstCreator(null);
                    } else if (mainMember.getPrimaryMembers() != null && mainMember.getPrimaryMembers().contains(member.getUuid().toString())) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation((member.getSex() == Sex.MALE) ? Relation.UNCLE : Relation.AUNT);
                        result.add(dto);
                        member.setFirstCreator(null);
                    } else if (member.getMotherUuid() != null && Objects.equals(member.getMotherUuid(), mainMember.getMotherUuid())) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation((member.getSex() == Sex.MALE) ? Relation.HALF_UNCLE_BY_MOTHER : Relation.HALF_AUNT_BY_MOTHER);
                        result.add(dto);
                        member.setFirstCreator(null);
                    } else if (member.getFatherUuid() != null && Objects.equals(member.getFatherUuid(), mainMember.getFatherUuid())) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation((member.getSex() == Sex.MALE) ? Relation.HALF_UNCLE_BY_FATHER : Relation.HALF_AUNT_BY_FATHER);
                        result.add(dto);
                        member.setFirstCreator(null);
                    }
                }
                case "child" -> {
                    if (member.getMotherUuid() != null && Objects.equals(member.getMotherUuid(), mainMember.getUuid())
                            || member.getFatherUuid() != null && Objects.equals(member.getFatherUuid(), mainMember.getUuid())) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation((member.getSex() == Sex.MALE) ? Relation.GRANDSON : Relation.GRANDDAUGHTER);
                        result.add(dto);
                        member.setFirstCreator(null);
                    }
                }
                case "brother" -> {
                    if (member.getMotherUuid() != null && Objects.equals(member.getMotherUuid(), mainMember.getUuid())
                            || member.getFatherUuid() != null && Objects.equals(member.getFatherUuid(), mainMember.getUuid())) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation((member.getSex() == Sex.MALE) ? Relation.NEPHEW : Relation.NIECE);
                        result.add(dto);
                        member.setFirstCreator(null);
                    }
                }
                case "halfSister" -> {
                    if ((member.getMotherUuid() != null && Objects.equals(member.getMotherUuid(), mainMember.getUuid())
                            || member.getFatherUuid() != null && Objects.equals(member.getFatherUuid(), mainMember.getUuid()))) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation((member.getSex() == Sex.MALE) ? Relation.HALF_NEPHEW_BY_MOTHER : Relation.HALF_NIECE_BY_MOTHER);
                        result.add(dto);
                        member.setFirstCreator(null);
                    }
                }
                case "halfBrother" -> {
                    if ((member.getMotherUuid() != null && Objects.equals(member.getMotherUuid(), mainMember.getUuid())
                            || member.getFatherUuid() != null && Objects.equals(member.getFatherUuid(), mainMember.getUuid()))) {
                        FamilyMemberDto dto = familyMemberMapper.entityToDto(member);
                        dto.setRelation((member.getSex() == Sex.MALE) ? Relation.HALF_NEPHEW_BY_FATHER : Relation.HALF_NIECE_BY_FATHER);
                        result.add(dto);
                        member.setFirstCreator(null);
                    }
                }
                default -> log.warn("unknown relation");

            }
        }
        mainMember.setFirstCreator(null);
    }
}

