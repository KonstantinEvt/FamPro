package com.example.service;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.FamilyMemberInfoDto;
import com.example.entity.Family;
import com.example.entity.Guard;
import com.example.entity.ShortFamilyMember;
import com.example.entity.ShortFamilyMemberInfo;
import com.example.enums.CheckStatus;
import com.example.enums.SecretLevel;
import com.example.enums.Sex;
import com.example.enums.SwitchPosition;
import com.example.mappers.FamilyMemberInfoMapper;
import com.example.mappers.FamilyMemberMapper;
import com.example.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Getter
@Setter
@Log4j2
public class MemberService implements SimpleFamilyService {
    FamilyMemberMapper familyMemberMapper;
    FamilyMemberInfoMapper familyMemberInfoMapper;

    MemberRepository memberRepository;

    @Transactional
    public ShortFamilyMember addFamilyMember(FamilyMemberDto dto) {
        ShortFamilyMember familyMember = familyMemberMapper.dtoToEntity(dto);
        familyMember.setId(null);
        if (dto.getMemberInfo() != null) {
            familyMember.setShortFamilyMemberInfo(creatNewInfo(dto.getMemberInfo()));
            familyMember.setBirthExist(dto.getMemberInfo().getBirth() != null);
            familyMember.setBurialExist(dto.getMemberInfo().getBurial() != null);
        }
        familyMember.setFamilies(new HashSet<>());
        familyMember.setFamilyWhereChildInLow(new HashSet<>());
        familyMember.setFamilyWhereHalfChildByFather(new HashSet<>());
        familyMember.setFamilyWhereHalfChildByMother(new HashSet<>());
        memberRepository.persistMember(familyMember);
        return familyMember;
    }

    public List<ShortFamilyMemberInfo> creatNewInfo(FamilyMemberInfoDto infoDto) {
        List<ShortFamilyMemberInfo> result = new ArrayList<>();
        infoDto.setId(null);
        result.add(familyMemberInfoMapper.dtoToEntity(infoDto));
        return result;
    }

    @Transactional
    public void updateMember(ShortFamilyMember shortFamilyMember) {
        memberRepository.updateMember(shortFamilyMember);
    }

    @Transactional
    public void editFamilyMember(FamilyMemberDto dto, ShortFamilyMember familyMember) {
        Optional<ShortFamilyMemberInfo> info = memberRepository.getInfoByUuid(familyMember.getUuid());

        if (familyMember.getUuid() != dto.getUuid()) {
            if (!familyMember.getFirstName().equals(dto.getFirstName())) familyMember.setFirstName(dto.getFirstName());
            if (!familyMember.getMiddleName().equals(dto.getMiddleName()))
                familyMember.setMiddleName(dto.getMiddleName());
            if (!familyMember.getLastName().equals(dto.getLastName())) familyMember.setLastName(dto.getLastName());
            if (!familyMember.getBirthday().equals(dto.getBirthday())) familyMember.setBirthday(dto.getBirthday());
            familyMember.setFullName(dto.getFullName());
            info.ifPresent(shortFamilyMemberInfo -> shortFamilyMemberInfo.setUuid(dto.getUuid()));
            familyMember.setUuid(dto.getUuid());
        }
        if (familyMember.getSecretLevelEdit() != dto.getSecretLevelEdit())
            familyMember.setSecretLevelEdit(dto.getSecretLevelEdit());
        if (familyMember.getSecretLevelPhoto() != dto.getSecretLevelPhoto())
            familyMember.setSecretLevelPhoto(dto.getSecretLevelPhoto());
        if (familyMember.getSecretLevelRemove() != dto.getSecretLevelRemove())
            familyMember.setSecretLevelRemove(dto.getSecretLevelRemove());
        if (familyMember.getSecretLevelMainInfo() != dto.getSecretLevelMainInfo())
            familyMember.setSecretLevelMainInfo(dto.getSecretLevelMainInfo());
        if (familyMember.getSecretLevelBirthday() != dto.getSecretLevelBirthday())
            familyMember.setSecretLevelBirthday(dto.getSecretLevelBirthday());

        if (familyMember.getCreator() != null && !familyMember.getCreator().equals(dto.getCreator()))
            familyMember.setCreator(dto.getCreator());
        if (familyMember.getCheckStatus() != dto.getCheckStatus()) familyMember.setCheckStatus(dto.getCheckStatus());
        if (!Objects.equals(dto.getDeathday(), familyMember.getDeathday())) familyMember.setDeathday(dto.getDeathday());
        if (!Objects.equals(dto.getFatherInfo(), familyMember.getFatherInfo()))
            familyMember.setFatherInfo(dto.getFatherInfo());
        if (!Objects.equals(dto.getMotherInfo(), familyMember.getMotherInfo()))
            familyMember.setMotherInfo(dto.getMotherInfo());
        if (dto.getMemberInfo() != null) {
            if (info.isPresent()) {
                mergeInfo(info.get(), dto.getMemberInfo());
            } else {
                familyMember.setShortFamilyMemberInfo(creatNewInfo(dto.getMemberInfo()));
            }
            if (dto.getMemberInfo().getSecretLevelBirth() != SecretLevel.CLOSE && dto.getMemberInfo().getSecretLevelBirth() != SecretLevel.UNDEFINED)
                familyMember.setBirthExist(dto.getMemberInfo().isPhotoBirthExist());
            if (dto.getMemberInfo().getSecretLevelBurial() != SecretLevel.CLOSE && dto.getMemberInfo().getSecretLevelBurial() != SecretLevel.UNDEFINED)
                familyMember.setBurialExist(dto.getMemberInfo().isPhotoBurialExist());
        }
    }

    @Transactional
    public void uncheckedMergeInfo(UUID uuid, FamilyMemberInfoDto infoDto) {
        ShortFamilyMemberInfo oldInfo = memberRepository.getInfoByUuid(uuid).orElseThrow(() -> new RuntimeException("info not found"));

        if (infoDto.getMainAddress() != null) {
            oldInfo.setMainAddress(infoDto.getMainAddress());
            oldInfo.setSecretLevelAddress(infoDto.getSecretLevelAddress());
        }

        if (infoDto.getMainEmail() != null) {
            oldInfo.setMainEmail(infoDto.getMainEmail());
            oldInfo.setSecretLevelEmail(infoDto.getSecretLevelEmail());
        }

        if (infoDto.getMainPhone() != null) {
            oldInfo.setMainPhone(infoDto.getMainPhone());
            oldInfo.setSecretLevelPhone(infoDto.getSecretLevelPhone());
        }

        oldInfo.setSecretLevelBiometric(infoDto.getSecretLevelBiometric());

        if (infoDto.isPhotoBirthExist()) {
            oldInfo.setPhotoBirthExist(true);
            oldInfo.setSecretLevelBirth(infoDto.getSecretLevelBirth());
        }

        if (infoDto.isPhotoBurialExist()) {
            oldInfo.setPhotoBurialExist(true);
            oldInfo.setSecretLevelBurial(infoDto.getSecretLevelBurial());
        }

        memberRepository.updateInfo(oldInfo);

    }

    @Transactional
    public void clearParentInfo(ShortFamilyMember member, SwitchPosition position) {
        if (position == SwitchPosition.FATHER) {
            member.setFatherInfo(null);
            member.setFatherUuid(null);
        }
        if (position == SwitchPosition.MOTHER) {
            member.setMotherInfo(null);
            member.setMotherUuid(null);
        }
    }

    @Transactional
    public Optional<ShortFamilyMember> getMemberByUuid(UUID uuid) {
        return memberRepository.getMemberByUuid(uuid);
    }
@Transactional
public Family getPrimeFamily(ShortFamilyMember member){
    return memberRepository.getPrimeFamily(member).orElse(null);
}
    @Transactional
    public void mergeInfo(ShortFamilyMemberInfo oldInfo, FamilyMemberInfoDto newInfo) {
        if (newInfo.getSecretLevelAddress() != SecretLevel.CLOSE && newInfo.getSecretLevelAddress() != SecretLevel.UNDEFINED) {
            oldInfo.setSecretLevelAddress(newInfo.getSecretLevelAddress());
            if (newInfo.getMainAddress() != null) oldInfo.setMainAddress(newInfo.getMainAddress());
        }
        if (newInfo.getSecretLevelEmail() != SecretLevel.CLOSE && newInfo.getSecretLevelEmail() != SecretLevel.UNDEFINED) {
            oldInfo.setSecretLevelEmail(newInfo.getSecretLevelEmail());
            if (newInfo.getMainEmail() != null) oldInfo.setMainEmail(newInfo.getMainEmail());
        }
        if (newInfo.getSecretLevelPhone() != SecretLevel.CLOSE && newInfo.getSecretLevelPhone() != SecretLevel.UNDEFINED) {
            oldInfo.setSecretLevelPhone(newInfo.getSecretLevelPhone());
            if (newInfo.getMainPhone() != null) oldInfo.setMainPhone(newInfo.getMainPhone());
        }
        if (newInfo.getSecretLevelBiometric() != SecretLevel.CLOSE && newInfo.getSecretLevelBiometric() != SecretLevel.UNDEFINED) {
            oldInfo.setSecretLevelBiometric(newInfo.getSecretLevelBiometric());
        }
        if (newInfo.getSecretLevelBirth() != SecretLevel.CLOSE && newInfo.getSecretLevelBirth() != SecretLevel.UNDEFINED) {
            oldInfo.setSecretLevelBirth(newInfo.getSecretLevelBirth());
            if (newInfo.isPhotoBirthExist()) oldInfo.setPhotoBirthExist(true);
        }
        if (newInfo.getSecretLevelBurial() != SecretLevel.CLOSE && newInfo.getSecretLevelBurial() != SecretLevel.UNDEFINED) {
            oldInfo.setSecretLevelBurial(newInfo.getSecretLevelBurial());
            if (newInfo.isPhotoBurialExist()) oldInfo.setPhotoBurialExist(true);
        }
        memberRepository.updateInfo(oldInfo);
    }

    public SecretLevel getSecretStatus(ShortFamilyMember member, UUID directiveGuard, Set<UUID> treeGuards, boolean geneticTreeCheck) {
        if (Objects.equals(member.getLinkGuard(), directiveGuard.toString())) return SecretLevel.CONFIDENTIAL;
        if (findUuidInInfo(member.getAncestorsGuard(), directiveGuard)) return SecretLevel.ANCESTOR;
        if (findUuidInInfo(member.getActiveGuard(), directiveGuard)) return SecretLevel.FAMILY;
        if (findUuidInInfo(member.getDescendantsGuard(), directiveGuard))
            return SecretLevel.STRAIGHT_BLOOD;
        if (geneticTreeCheck&&treeGuards.contains(directiveGuard)) return SecretLevel.GENETIC_TREE;
        return SecretLevel.OPEN;
    }

    public SecretLevel getMaxSecretLevelForMember(ShortFamilyMember member, Set<UUID> treeGuards, boolean geneticTreeCheck) {
        if (member.getLinkGuard() != null) return SecretLevel.CONFIDENTIAL;
        if (member.getAncestorsGuard() != null) return SecretLevel.ANCESTOR;
        if (member.getActiveGuard() != null) return SecretLevel.FAMILY;
        if (member.getDescendantsGuard() != null) return SecretLevel.STRAIGHT_BLOOD;
        if (geneticTreeCheck&&treeGuards != null && !treeGuards.isEmpty()) return SecretLevel.GENETIC_TREE;
        return SecretLevel.OPEN;
    }

    /***
     return Set Top Ancestors, if Set is empty - member is top ancestor
     ***/
    @Transactional(readOnly = true)
    public Set<ShortFamilyMember> getAllTopAncestors(ShortFamilyMember member) {
        Set<ShortFamilyMember> result;
        if (member.getTopAncestors() == null || member.getTopAncestors().isBlank()) {
            result = new HashSet<>();
            result.add(member);
        } else {
            result = memberRepository.getAllMembersByUuids(getAllUuidFromInfo(member.getTopAncestors()));
            if (result.isEmpty()) {
                log.warn("Database is corrupt in: memberTopAncestors <-> uuid in base");
                result.add(member);
            }
        }
        return result;
    }

    /***
     return Set of Ancestors, Member is one of ancestor
     ***/
    @Transactional(readOnly = true)
    public Set<ShortFamilyMember> getAllAncestors(ShortFamilyMember member) {
        Set<ShortFamilyMember> result;
        if (member.getAncestors() == null || member.getAncestors().isBlank()) {
            result = new HashSet<>();
        } else {
            result = memberRepository.getAllMembersByUuids(getAllUuidFromInfo(member.getAncestors()));
            if (result.isEmpty()) {
                log.warn("Database is corrupt in: memberAncestors <-> uuid in base");
            }
        }
        result.add(member);
        return result;
    }

    /***
     return Set Descendants. Set may be empty
     ***/
    @Transactional(readOnly = true)
    public Set<ShortFamilyMember> getAllDescendants(ShortFamilyMember member) {
        Set<ShortFamilyMember> result;
        if (member.getDescendants() == null || member.getDescendants().isBlank()) {
            result = new HashSet<>();
        } else {
            result = memberRepository.getAllMembersByUuids(getAllUuidFromInfo(member.getDescendants()));
            if (result.isEmpty()) {
                log.warn("Database is corrupt in: memberDescendants <-> uuid in base");
            }
        }
        return result;
    }

    @Transactional(readOnly = true)
    public Set<ShortFamilyMember> getAllMembersByUuids(Set<UUID> uuidSet) {
        return memberRepository.getAllMembersByUuids(uuidSet);
    }

    @Transactional(readOnly = true)
    public Set<ShortFamilyMember> getGeneticTreeMembers(Set<ShortFamilyMember> topAncestors) {
        Set<ShortFamilyMember> result = new HashSet<>(topAncestors);
        Set<UUID> treeMembersUuids = new HashSet<>();
        for (ShortFamilyMember fm :
                topAncestors) {
            if (fm.getDescendants() != null && !fm.getDescendants().isBlank())
                treeMembersUuids.addAll(Arrays.stream(fm.getDescendants().split(" ")).filter(Objects::nonNull).map(UUID::fromString).collect(Collectors.toSet()));
        }
        if (!treeMembersUuids.isEmpty()) result.addAll(memberRepository.getAllMembersByUuids(treeMembersUuids));
        return result;
    }

    @Transactional(readOnly = true)
    public Set<UUID> getGeneticTreeGuards(Set<ShortFamilyMember> topAncestors) {
        Set<UUID> result = new HashSet<>();
        for (ShortFamilyMember member :
                topAncestors) {
            if (member.getLinkGuard() != null) result.add(UUID.fromString(member.getLinkGuard()));
            if (member.getDescendantsGuard() != null)
                result.addAll(Arrays.stream(member.getDescendantsGuard().split(" ")).filter(Objects::nonNull).map(UUID::fromString).collect(Collectors.toSet()));
        }
        return result;
    }

    @Transactional(readOnly = true)
    public boolean checkThemeSecretForSecretLevel(SecretLevel secretLevel, ShortFamilyMember member, Optional<Guard> directiveGuard, Set<ShortFamilyMember> topAncestors) {
        if (directiveGuard.isPresent()&&directiveGuard.get().getId()!=null) {
            UUID uuid = UUID.fromString(directiveGuard.get().getTokenUser());
            Set<UUID> geneticTreeGuard = getGeneticTreeGuards(topAncestors);
            SecretLevel directiveGuardStatus = getSecretStatus(member, uuid, geneticTreeGuard,true);
            SecretLevel max = getMaxSecretLevelForMember(member, geneticTreeGuard,true);
            if (directiveGuardStatus == max) return true;
            else return secretLevel.ordinal() < directiveGuardStatus.ordinal();
        } else {
            Set<UUID> geneticTreeGuard = getGeneticTreeGuards(topAncestors);
            SecretLevel max = getMaxSecretLevelForMember(member, geneticTreeGuard,true);
            return SecretLevel.OPEN == max;
        }
    }

    @Transactional
    public void removeLinkWithParent(Family primeFamily, ShortFamilyMember child, ShortFamilyMember parent) {
        log.warn("Нужна проработка удаления связи");
    }

    @Transactional
    public void changeUuidInBloodTree(UUID oldUuid, ShortFamilyMember member) {
        Set<ShortFamilyMember> bloodKin = getAllAncestors(member);
        bloodKin.addAll(getAllDescendants(member));
        bloodKin.remove(member);
        for (ShortFamilyMember fm :
                bloodKin) {
            if (fm.getDescendants() != null) fm.setDescendants(changeUuidInInfo(fm.getDescendants(), oldUuid, member.getUuid()));
            if (fm.getAncestors() != null) fm.setAncestors(changeUuidInInfo(fm.getAncestors(), oldUuid, member.getUuid()));
            if (fm.getTopAncestors() != null) fm.setTopAncestors(changeUuidInInfo(fm.getTopAncestors(), oldUuid, member.getUuid()));
        }
        log.warn("uuid in bloodKin is changed");
    }

    @Transactional
    public void addChildToFamilyMember(ShortFamilyMember child, ShortFamilyMember parent, Sex parentSex) {
        if (parentSex == Sex.MALE) {
            child.setFatherUuid(parent.getUuid());
            child.setFatherInfo(parent.getFullName());
        } else if (parentSex == Sex.FEMALE) {
            child.setMotherUuid(parent.getUuid());
            child.setMotherInfo(parent.getFullName());
        }
        Set<ShortFamilyMember> childDescendants;
        Set<UUID> childDescendantsUuids;
        Set<UUID> childDescendantsGuard;
        if (child.getDescendants() != null && !child.getDescendants().isBlank()) {
            childDescendants = getAllDescendants(child);
            childDescendantsUuids = getAllUuidFromInfo(child.getDescendants());
            childDescendantsGuard = getAllUuidFromInfo(child.getDescendantsGuard());
        } else {
            childDescendants = new HashSet<>();
            childDescendantsUuids = new HashSet<>();
            childDescendantsGuard = new HashSet<>();
        }
        childDescendants.add(child);
        childDescendantsUuids.add(child.getUuid());
        if (child.getLinkGuard() != null) childDescendantsGuard.add(UUID.fromString(child.getLinkGuard()));

        Set<ShortFamilyMember> parentAncestors = getAllAncestors(parent);
        Set<UUID> parentAncestorsUuids = getAllUuidFromInfo(parent.getAncestors());
        Set<UUID> parentTopAncestorsUuids = getAllUuidFromInfo(parent.getTopAncestors());
        Set<UUID> parentAncestorsGuards = getAllUuidFromInfo(parent.getAncestorsGuard());
        parentAncestorsUuids.add(parent.getUuid());
        if (parent.getLinkGuard() != null) parentAncestorsGuards.add(UUID.fromString(parent.getLinkGuard()));
        if (parentTopAncestorsUuids.isEmpty()) parentTopAncestorsUuids.add(parent.getUuid());
        for (ShortFamilyMember des :
                childDescendants) {
            Set<UUID> desAnc = getAllUuidFromInfo(des.getAncestors());
            Set<UUID> desTopAnc = getAllUuidFromInfo(des.getTopAncestors());
            desAnc.addAll(parentAncestorsUuids);
            desTopAnc.remove(child.getUuid());
            desTopAnc.addAll(parentTopAncestorsUuids);
            des.setAncestors(desAnc.stream().map(UUID::toString).filter(Objects::nonNull).reduce((x, y) -> x.concat(" ".concat(y))).orElse(null));
            des.setTopAncestors(desTopAnc.stream().map(UUID::toString).filter(Objects::nonNull).reduce((x, y) -> x.concat(" ".concat(y))).orElse(null));
            if (!parentAncestorsGuards.isEmpty()) {
                Set<UUID> desAncGuards = getAllUuidFromInfo(des.getAncestorsGuard());
                desAncGuards.addAll(parentAncestorsGuards);
                des.setAncestorsGuard(desAncGuards.stream().map(UUID::toString).filter(Objects::nonNull).reduce((x, y) -> x.concat(" ".concat(y))).orElse(null));

            }
        }
        for (ShortFamilyMember anc :
                parentAncestors) {
            Set<UUID> ancDes = getAllUuidFromInfo(anc.getDescendants());
            ancDes.addAll(childDescendantsUuids);
            anc.setDescendants(ancDes.stream().map(UUID::toString).filter(Objects::nonNull).reduce((x, y) -> x.concat(" ".concat(y))).orElse(null));
            if (!childDescendantsGuard.isEmpty()) {
                Set<UUID> ancDesGuards = getAllUuidFromInfo(anc.getDescendantsGuard());
                ancDesGuards.addAll(childDescendantsGuard);
                anc.setDescendantsGuard(ancDesGuards.stream().map(UUID::toString).filter(Objects::nonNull).reduce((x, y) -> x.concat(" ".concat(y))).orElse(null));
            }
        }

        log.info("link child-parent is added");
    }

    @Transactional
    public Set<String> repairGeneticTreeCheckStatus(Set<ShortFamilyMember> newTopAc) {
        Set<String> result = new HashSet<>();
        Set<ShortFamilyMember> members = getGeneticTreeMembers(newTopAc);
        for (ShortFamilyMember member :
                members) {
            if (member.getCheckStatus() == CheckStatus.UNCHECKED) {
                member.setCheckStatus(CheckStatus.CHECKED);
                member.setCreator(null);
                result.add(member.getUuid().toString());
                memberRepository.updateMember(member);
            }
        }
        return result;
    }

    @Transactional
    public void addGuardToMemberByLinking(ShortFamilyMember member, Guard guard) {
        member.setCheckStatus(CheckStatus.LINKED);
        member.setLinkGuard(guard.getTokenUser());
    }

    /***
     adding guard in kin of person
     @param member person who receive guard
     @param guard guard;
     ***/
    @Transactional
    public void addGuardToMemberKin(ShortFamilyMember member, Guard guard) {
        Set<ShortFamilyMember> memberAncestors = getAllAncestors(member);
        Set<ShortFamilyMember> memberDescendants = getAllDescendants(member);
        memberAncestors.remove(member);
        for (ShortFamilyMember anc :
                memberAncestors) {
            anc.setDescendantsGuard(addUuidToInfo(anc.getDescendantsGuard(), guard.getTokenUser()));
            memberRepository.updateMember(anc);
        }
        for (ShortFamilyMember des :
                memberDescendants) {
            des.setAncestorsGuard(addUuidToInfo(des.getAncestorsGuard(), guard.getTokenUser()));
            memberRepository.updateMember(des);
        }
    }
    @Transactional
    public void flush() {
        memberRepository.flush();
    }
}