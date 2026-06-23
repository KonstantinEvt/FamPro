package com.example.service;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.FamilyMemberInfoDto;
import com.example.entity.*;
import com.example.enums.*;
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
    private final FamilyMemberMapper familyMemberMapper;
    private final FamilyMemberInfoMapper familyMemberInfoMapper;
    private final FamilyMemberLinkService familyMemberLinkService;
    private final MemberRepository memberRepository;
    private final SendAndFormService sendAndFormService;

    @Transactional
    public ShortFamilyMember addFamilyMember(FamilyMemberDto dto) {
        ShortFamilyMember familyMember = familyMemberMapper.dtoToEntity(dto);
        familyMember.setId(null);
        if (dto.getMemberInfo() != null) {
            familyMember.setShortFamilyMemberInfo(creatNewInfo(dto.getMemberInfo()));
            familyMember.setBirthExist(dto.getMemberInfo().getBirth() != null);
            familyMember.setBurialExist(dto.getMemberInfo().getBurial() != null);
        }
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
    public Optional<ShortFamilyMember> findMemberWithPrimeFamily(UUID uuid) {
        return memberRepository.findMemberWithPrimeFamily(uuid);
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

        if (familyMember.getFirstCreator() != null && !familyMember.getFirstCreator().equals(dto.getFirstCreator()))
            familyMember.setFirstCreator(dto.getFirstCreator());
        if (familyMember.getCreator() != null && !familyMember.getCreator().equals(dto.getCreator()))
            familyMember.setCreator(dto.getCreator());
        if (dto.getSecretLevelPhoto() != SecretLevel.CLOSE
                && dto.getSecretLevelPhoto() != SecretLevel.UNDEFINED
                && familyMember.isPrimePhoto() != dto.isPrimePhoto()) familyMember.setPrimePhoto(dto.isPrimePhoto());
//        if (familyMember.getCheckStatus() != dto.getCheckStatus()) familyMember.setCheckStatus(dto.getCheckStatus());
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
    public Family getPrimeFamily(ShortFamilyMember member) {
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
        if (findUuidInInfo(member.getActiveGuard(), directiveGuard)) return SecretLevel.ACTIVE_FAMILY;
        if (findUuidInInfo(member.getLogicGuard(), directiveGuard)) return SecretLevel.LOGIC_PRIMARY_FAMILY;
        if (findUuidInInfo(member.getAncestorsGuard(), directiveGuard)) return SecretLevel.ANCESTOR;
        if (findUuidInInfo(member.getDescendantsGuard(), directiveGuard))
            return SecretLevel.STRAIGHT_BLOOD;
        if (findUuidInInfo(member.getPrimaryGuard(), directiveGuard)) return SecretLevel.PRIMARY_FAMILY;
        if (geneticTreeCheck && treeGuards.contains(directiveGuard)) return SecretLevel.GENETIC_TREE;
        return SecretLevel.OPEN;
    }

    public SecretLevel getMaxSecretLevelForMember(ShortFamilyMember member, Set<UUID> treeGuards, boolean geneticTreeCheck) {
        if (member.getLinkGuard() != null && !member.getLinkGuard().isBlank()) return SecretLevel.CONFIDENTIAL;
        if (member.getActiveGuard() != null && !member.getActiveGuard().isBlank()) return SecretLevel.ACTIVE_FAMILY;
        if (member.getLogicGuard() != null && !member.getLogicGuard().isBlank())
            return SecretLevel.LOGIC_PRIMARY_FAMILY;
        if (member.getAncestorsGuard() != null && !member.getAncestorsGuard().isBlank()) return SecretLevel.ANCESTOR;
        if (member.getDescendantsGuard() != null && !member.getDescendantsGuard().isBlank())
            return SecretLevel.STRAIGHT_BLOOD;
        if (member.getPrimaryGuard() != null && !member.getPrimaryGuard().isBlank()) return SecretLevel.PRIMARY_FAMILY;
        if (geneticTreeCheck && treeGuards != null && !treeGuards.isEmpty()) return SecretLevel.GENETIC_TREE;
        return SecretLevel.OPEN;
    }

    /***
     return Set Top Ancestors, if Top Ancestors absent - member is top ancestor
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
     adding Set of TopAncestors by they brothers/sisters
     ***/
    @Transactional(readOnly = true)
    public boolean getExtendedTopAncestors(Set<ShortFamilyMember> topAncestors) {
        Set<String> topPrimary = new HashSet<>();
        for (ShortFamilyMember member :
                topAncestors) {
            if (member.getPrimaryMembers() != null && !member.getPrimaryMembers().isBlank())
                topPrimary.addAll(getAllStringUuidFromInfo(member.getPrimaryMembers()));
        }
        if (!topPrimary.isEmpty()) {
            topAncestors.addAll(memberRepository.getAllMembersByUuids(topPrimary.stream().map(UUID::fromString).collect(Collectors.toSet())));
            return true;
        } else return false;
    }

    @Transactional(readOnly = true)
    public Set<ShortFamilyMember> getBrothersOfTopAncestors(Set<ShortFamilyMember> topAncestors) {
        Set<String> topPrimary = new HashSet<>();
        for (ShortFamilyMember member :
                topAncestors) {
            if (member.getPrimaryMembers() != null && !member.getPrimaryMembers().isBlank())
                topPrimary.addAll(getAllStringUuidFromInfo(member.getPrimaryMembers()));
        }
        if (!topPrimary.isEmpty()) {
            return memberRepository.getAllMembersByUuids(topPrimary.stream().map(UUID::fromString).collect(Collectors.toSet()));
        } else return new HashSet<>();
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
    public Set<ShortFamilyMember> getAllPrimaryMembers(ShortFamilyMember member) {
        Set<ShortFamilyMember> result;
        if (member.getPrimaryMembers() == null || member.getPrimaryMembers().isBlank()) {
            result = new HashSet<>();
        } else {
            result = memberRepository.getAllMembersByUuids(getAllUuidFromInfo(member.getPrimaryMembers()));
            if (result.isEmpty()) {
                log.warn("Database is corrupt in: primaryMembers <-> uuid in base");
            }
        }
        return result;
    }

    @Transactional(readOnly = true)
    public Set<ShortFamilyMember> getAllMembersByUuids(Set<UUID> uuidSet) {
        return memberRepository.getAllMembersByUuids(uuidSet);
    }

    @Transactional(readOnly = true)
    public Set<ShortFamilyMember> getGeneticTreeMembers(Set<ShortFamilyMember> topAncestors, boolean includeTopBrothers) {
        if (includeTopBrothers) getExtendedTopAncestors(topAncestors);
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
    public Set<UUID> getGeneticTreeGuards(Set<ShortFamilyMember> topAncestors, boolean includeTopBrothers) {
        if (includeTopBrothers) getExtendedTopAncestors(topAncestors);
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
        if (directiveGuard.isPresent() && directiveGuard.get().getId() != null) {
            UUID uuid = UUID.fromString(directiveGuard.get().getTokenUser());
            Set<UUID> geneticTreeGuard = getGeneticTreeGuards(topAncestors, true);
            SecretLevel directiveGuardStatus = getSecretStatus(member, uuid, geneticTreeGuard, true);
            SecretLevel max = getMaxSecretLevelForMember(member, geneticTreeGuard, true);
            if (directiveGuardStatus == max) return true;
            else return secretLevel.ordinal() <= directiveGuardStatus.ordinal();
        } else {
            Set<UUID> geneticTreeGuard = getGeneticTreeGuards(topAncestors, true);
            SecretLevel max = getMaxSecretLevelForMember(member, geneticTreeGuard, true);
            return SecretLevel.OPEN == max;
        }
    }

    @Transactional
    public void removeLinkWithParent(ShortFamilyMember child, UUID parent1Uuid, UUID parent2Uuid) {
        Set<ShortFamilyMember> bloodKin = getAllKinMembersInMemberProfile(child);
        ShortFamilyMember parent = bloodKin.stream().filter(x -> Objects.equals(x.getUuid(), parent1Uuid)).findFirst().orElseThrow(() -> new RuntimeException("ancestors corrupt"));
        ShortFamilyMember parent2 = bloodKin.stream().filter(x -> Objects.equals(x.getUuid(), parent2Uuid)).findFirst().orElseThrow(() -> new RuntimeException("ancestors corrupt"));
        Set<String> ancUuid = getAllStringUuidFromInfo(parent.getAncestors());
        if (parent2.getAncestors() != null && !parent2.getAncestors().isBlank())
            ancUuid.removeAll(getAllStringUuidFromInfo(parent2.getAncestors()));
        ancUuid.add(parent1Uuid.toString());
        ancUuid.remove(parent2.getUuid().toString());
        Set<String> ancGuards = getAllStringUuidFromInfo(parent.getAncestorsGuard());
        if (parent.getLinkGuard() != null && !parent.getLinkGuard().isBlank()) ancGuards.add(parent.getLinkGuard());
        if (parent2.getAncestorsGuard() != null && !parent2.getAncestorsGuard().isBlank())
            ancGuards.removeAll(getAllStringUuidFromInfo(parent2.getAncestorsGuard()));
        if (parent2.getLinkGuard() != null && !parent2.getLinkGuard().isBlank())
            ancGuards.remove(parent2.getLinkGuard());
        child.setPrimaryMembers(null);
        child.setPrimaryGuard(null);
        child.setAncestors(removeAllUuidSFromInfo(child.getAncestors(), ancUuid).orElse(null));
        child.setAncestorsGuard(removeAllUuidSFromInfo(child.getAncestorsGuard(), ancGuards).orElse(null));
        child.setTopAncestors(removeAllUuidSFromInfo(child.getTopAncestors(), ancUuid).orElse(null));
        removeAncestorsDescendantsRelations(bloodKin, ancUuid, ancGuards, child);
        log.warn("Нужна проработка удаления удаления стражи и чекстатуса");
    }

    @Transactional
    public void mergePrimaryRelations(Set<ShortFamilyMember> set1, Set<ShortFamilyMember> set2) {
        Set<String> primaryMembers1 = set1.stream().map(x -> x.getUuid().toString()).collect(Collectors.toSet());
        Set<String> primaryMembers2 = set2.stream().map(x -> x.getUuid().toString()).collect(Collectors.toSet());
        for (ShortFamilyMember mem :
                set1) {
            mem.setPrimaryMembers(mergeInfo(primaryMembers2, mem.getPrimaryMembers()));
        }
        for (ShortFamilyMember mem :
                set2) {
            mem.setPrimaryMembers(mergeInfo(primaryMembers1, mem.getPrimaryMembers()));
        }
    }

    @Transactional
    public void removePrimeMembersRelation(ShortFamilyMember member, Set<ShortFamilyMember> primeMembers) {
        Set<ShortFamilyMember> topPrime = new HashSet<>();
        boolean absentAnc = false;
        for (ShortFamilyMember primeMem :
                primeMembers) {
            primeMem.setPrimaryMembers(removeUuidFromInfo(primeMem.getPrimaryMembers(), member.getUuid().toString()).orElse(null));
            if (member.getLinkGuard() != null && !member.getLinkGuard().isBlank())
                primeMem.setPrimaryGuard(removeUuidFromInfo(primeMem.getPrimaryGuard(), member.getLinkGuard()).orElse(null));
            if ((primeMem.getAncestors() == null || primeMem.getAncestors().isBlank()) && !Objects.equals(primeMem.getUuid(), member.getUuid())) {
                topPrime.add(primeMem);
                absentAnc = true;
            }
        }
        member.setPrimaryMembers(null);
        member.setPrimaryGuard(null);
        boolean repair = false;
        if (member.getDescendants() != null && !member.getDescendants().isBlank() && !topPrime.isEmpty())
            repair = repairGeneticTreeRelations(topPrime, getAllDescendants(member), member);
        if (absentAnc) repairCheckStatus(member, primeMembers, topPrime, repair);
        log.warn("Нужна проработка удаления удаления стражи и чекстатуса");
    }

    @Transactional
    public void removeAncestorsDescendantsRelations(Set<ShortFamilyMember> bloodKin,
                                                    Set<String> ancUuid,
                                                    Set<String> ancGuards,
                                                    ShortFamilyMember child) {

        Set<String> desUuid = getAllStringUuidFromInfo(child.getDescendants());
        desUuid.add(child.getUuid().toString());
        Set<String> desGuards = getAllStringUuidFromInfo(child.getDescendantsGuard());
        if (child.getLinkGuard() != null && !child.getLinkGuard().isBlank()) desGuards.add(child.getLinkGuard());
        Set<ShortFamilyMember> anc = new HashSet<>();
        Set<ShortFamilyMember> des = new HashSet<>();
        Set<ShortFamilyMember> topAnc = new HashSet<>();
        Set<ShortFamilyMember> topRemoveAnc = new HashSet<>();
        for (ShortFamilyMember member :
                bloodKin) {
            if (member.getAncestors() == null || member.getAncestors().isBlank()) topAnc.add(member);
            if (member.getPrimaryMembers() != null && member.getPrimaryMembers().contains(child.getUuid().toString())) {
                member.setPrimaryMembers(removeUuidFromInfo(member.getPrimaryMembers(), child.getUuid()).orElse(null));
                if (child.getLinkGuard() != null && !child.getLinkGuard().isBlank())
                    member.setPrimaryGuard(removeUuidFromInfo(member.getPrimaryGuard(), child.getLinkGuard()).orElse(null));
            } else if (ancUuid.contains(member.getUuid().toString())) {
                anc.add(member);
                if (member.getAncestors() == null || member.getAncestors().isBlank()) topRemoveAnc.add(member);
                member.setDescendants(removeAllUuidSFromInfo(member.getDescendants(), desUuid).orElse(null));
                if (!desGuards.isEmpty()) {
                    member.setDescendantsGuard(removeAllUuidSFromInfo(member.getDescendantsGuard(), desGuards).orElse(null));
                }
                if (Objects.equals(child.getMotherUuid(), member.getUuid()) || Objects.equals(child.getFatherUuid(), member.getUuid()))
                    familyMemberLinkService.removeAllFamilyLinksBetweenMembers(member, child);
            } else if (child.getDescendants() != null
                    && !child.getDescendants().isBlank()
                    && child.getDescendants().contains(member.getUuid().toString())) {
                des.add(member);
                if (child.getAncestors() == null || child.getAncestors().isBlank())
                    member.setTopAncestors(addUuidToInfo(member.getTopAncestors(), child.getUuid().toString()));
                member.setAncestors(removeAllUuidSFromInfo(member.getAncestors(), ancUuid).orElse(child.getUuid().toString()));
                member.setTopAncestors(removeAllUuidSFromInfo(member.getTopAncestors(), ancUuid).orElse(child.getUuid().toString()));
                if (!ancGuards.isEmpty())
                    member.setAncestorsGuard(removeAllUuidSFromInfo(member.getAncestorsGuard(), ancGuards).orElse(null));

            }
        }
        boolean repair = false;
        if (!anc.isEmpty() && !des.isEmpty())
//        {
//            if (child.getAncestors()!=null && !child.getAncestors().isBlank()) des.add(child);
            repair = repairGeneticTreeRelations(anc, des, child);
//        }
        if (child.getAncestors() == null || !child.getAncestors().isBlank()) topAnc.add(child);
        repairCheckStatus(child, topAnc, topRemoveAnc, repair);
    }

    public void repairCheckStatus(ShortFamilyMember child, Set<ShortFamilyMember> topAnc, Set<ShortFamilyMember> topRemoveAnc, boolean repair) {
        System.out.println(topAnc.stream().map(x -> x.getUuid().toString()).collect(Collectors.toSet()));
        System.out.println(topRemoveAnc.stream().map(x -> x.getUuid().toString()).collect(Collectors.toSet()));
        switch (child.getCheckStatus()) {
            case UNCHECKED -> log.info("repair CheckStatus is not doing");
            case LINKED -> {
                log.info("repair CheckStatus will be do for remove parent(s) and his(their) kins");
                Set<ShortFamilyMember> topWithoutGuard = new HashSet<>();
                Set<String> desRepair = findMembersToCheckCheckStatus(topRemoveAnc, topWithoutGuard);
                if (!desRepair.isEmpty()) {
                    if (repair) desRepair.removeAll(getAllStringUuidFromInfo(child.getDescendants()));
                    if (!desRepair.isEmpty() || !topWithoutGuard.isEmpty()) {
                        Set<ShortFamilyMember> result = checkMembersForCheckStatus(desRepair, topWithoutGuard);
                        if (!result.isEmpty()) {
                            sendAndFormService.formDirectiveToStorageForChangeStatus(null, null, null, KafkaOperation.RENAME, result.stream().map(x -> x.getUuid().toString()).collect(Collectors.toSet()), CheckStatus.UNCHECKED);
                        }
                    }
                }
            }
            case CHECKED -> {
                log.info("repair CheckStatus for all");
                Set<ShortFamilyMember> topWithoutGuard = new HashSet<>();
                Set<String> desRepair = findMembersToCheckCheckStatus(topAnc, topWithoutGuard);
                System.out.println(desRepair);
                if (!desRepair.isEmpty() || !topWithoutGuard.isEmpty()) {
                    Set<ShortFamilyMember> result = checkMembersForCheckStatus(desRepair, topWithoutGuard);
                    if (!result.isEmpty()) {
                        result.remove(child);
                        if (!result.isEmpty())
                            sendAndFormService.formDirectiveToStorageForChangeStatus(null, null, null, KafkaOperation.RENAME, result.stream().map(x -> x.getUuid().toString()).collect(Collectors.toSet()), CheckStatus.UNCHECKED);

                    }
                }
            }
            default -> log.warn("wrong CheckStatus");
        }
    }

    public Set<ShortFamilyMember> checkMembersForCheckStatus(Set<String> membersUuid, Set<ShortFamilyMember> topWithoutGuard) {
        Set<String> alreadyCheckTop = topWithoutGuard.stream().map(x -> x.getUuid().toString()).collect(Collectors.toSet());
        if (!membersUuid.isEmpty()) {
            Set<ShortFamilyMember> membersToCheck = memberRepository.getAllMembersByUuids(membersUuid.stream().map(UUID::fromString).collect(Collectors.toSet()));
            membersToCheck.removeAll(membersToCheck.stream().filter(x -> x.getCheckStatus() != CheckStatus.CHECKED).collect(Collectors.toSet()));
            if (!membersToCheck.isEmpty()) {
                Set<String> alterTopAncestorsUuids = membersToCheck.stream().flatMap(x -> getAllStringUuidFromInfo(x.getTopAncestors()).stream()).collect(Collectors.toSet());
                if (!alterTopAncestorsUuids.isEmpty()) {
                    alterTopAncestorsUuids.removeAll(alreadyCheckTop);
                    if (!alterTopAncestorsUuids.isEmpty()) {
                        Set<ShortFamilyMember> alterTopAncestors = memberRepository.getAllMembersByUuids(alterTopAncestorsUuids.stream().map(UUID::fromString).collect(Collectors.toSet()));
                        Set<ShortFamilyMember> checks = new HashSet<>();
                        for (ShortFamilyMember top :
                                alterTopAncestors) {
                            if (checkTopForStraightGuard(top))
                                for (ShortFamilyMember member :
                                        membersToCheck) {
                                    if (top.getDescendants().contains(member.getUuid().toString())) checks.add(member);
                                }
                            membersToCheck.removeAll(checks);
                            checks.clear();
                            if (membersToCheck.isEmpty()) break;
                        }
                        if (!membersToCheck.isEmpty()) {
                            Set<UUID> brotherTopsUuid = alterTopAncestors.stream().flatMap(x -> getAllStringUuidFromInfo(x.getPrimaryMembers()).stream()).map(UUID::fromString).collect(Collectors.toSet());
                            if (!brotherTopsUuid.isEmpty()) {
                                Set<ShortFamilyMember> brotherTops = memberRepository.getAllMembersByUuids(brotherTopsUuid);
                                for (ShortFamilyMember topBrother :
                                        brotherTops) {
                                    if (topBrother.getDescendantsGuard() != null && !topBrother.getDescendantsGuard().isBlank()) {
                                        ShortFamilyMember top = alterTopAncestors.stream()
                                                .filter(x -> x.getPrimaryMembers().contains(topBrother.getUuid().toString()))
                                                .findFirst()
                                                .orElseThrow(() -> new RuntimeException("broken note"));
                                        for (ShortFamilyMember member :
                                                membersToCheck) {
                                            if (top.getDescendants().contains(member.getUuid().toString()))
                                                checks.add(member);
                                            membersToCheck.removeAll(checks);
                                            checks.clear();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            membersToCheck.addAll(topWithoutGuard);
            for (ShortFamilyMember change :
                    membersToCheck) {
                change.setCheckStatus(CheckStatus.UNCHECKED);
                change.setCreator(change.getFirstCreator());

            }
            memberRepository.flush();
            log.warn("неприкаенные члены найдены. Счас отдирективим");
            return membersToCheck;
        } else for (ShortFamilyMember change :
                topWithoutGuard) {
            change.setCheckStatus(CheckStatus.UNCHECKED);
            change.setCreator(change.getFirstCreator());

        }
        memberRepository.flush();
        log.warn("неприкаенные топы ушли в uncheck.");
        return topWithoutGuard;
    }


    public boolean checkTopForStraightGuard(ShortFamilyMember top) {
        return (top.getLinkGuard() != null && !top.getLinkGuard().isBlank())
                || (top.getDescendantsGuard() != null && !top.getDescendantsGuard().isBlank())
                || (top.getPrimaryGuard() != null && !top.getPrimaryGuard().isBlank());
    }


    public Set<String> findMembersToCheckCheckStatus(Set<ShortFamilyMember> topAnc, Set<ShortFamilyMember> topWithoutGuard) {

        Set<String> membersUnderGuarding = new HashSet<>();
        Set<String> topBrothersUuids = new HashSet<>();
        for (ShortFamilyMember topMember :
                topAnc) {
            if (checkTopForStraightGuard(topMember))
                membersUnderGuarding.addAll(getAllStringUuidFromInfo(topMember.getDescendants()));

            else topWithoutGuard.add(topMember);
        }
        if (topWithoutGuard.isEmpty()) return new HashSet<>();
        for (ShortFamilyMember topMember :
                topWithoutGuard) {
            if (topMember.getPrimaryMembers() != null && !topMember.getPrimaryMembers().isBlank()) {
                topBrothersUuids.addAll(getAllStringUuidFromInfo(topMember.getPrimaryMembers()));
            }
        }
        if (!topBrothersUuids.isEmpty()) {
            Set<ShortFamilyMember> topBrothers = memberRepository.getAllMembersByUuids(topBrothersUuids.stream().map(UUID::fromString).collect(Collectors.toSet()));
            for (ShortFamilyMember brother :
                    topBrothers) {
                if (brother.getDescendantsGuard() != null && !brother.getDescendantsGuard().isBlank()) {
                    topWithoutGuard.remove(topWithoutGuard.stream().filter(x -> x.getPrimaryMembers().contains(brother.getUuid().toString())).findFirst().orElseThrow(() -> new RuntimeException("broken note")));
                } else topWithoutGuard.add(brother);
            }
        }
        Set<String> desRepair = topWithoutGuard.stream().flatMap(x -> getAllStringUuidFromInfo(x.getDescendants()).stream()).collect(Collectors.toSet());
        if (!desRepair.isEmpty()) desRepair.removeAll(membersUnderGuarding);
        return desRepair;
    }

    @Transactional
    public void removeBothParentLinks(ShortFamilyMember child) {
        Set<ShortFamilyMember> bloodKin = getAllKinMembersInMemberProfile(child);
        Set<String> ancUuid = getAllStringUuidFromInfo(child.getAncestors());
        Set<String> ancGuards = getAllStringUuidFromInfo(child.getAncestorsGuard());
        child.setPrimaryMembers(null);
        child.setPrimaryGuard(null);
        child.setAncestors(null);
        child.setAncestorsGuard(null);
        child.setTopAncestors(null);
        removeAncestorsDescendantsRelations(bloodKin, ancUuid, ancGuards, child);

        log.warn("Нужна проработка удаления стражи и чекстатуса");
    }

    @Transactional
    public boolean repairGeneticTreeRelations(Set<ShortFamilyMember> oldAnc, Set<ShortFamilyMember> descendants, ShortFamilyMember mainMember) {
        boolean repair = false;
        System.out.println("MASSIVE OF REPAIR ANCESTORS:");
        System.out.println(oldAnc.stream().map(x -> x.getUuid().toString()).collect(Collectors.toSet()));
        System.out.println("MASSIVE OF REPAIR DESCENDANTS:");
        System.out.println(descendants.stream().map(x -> x.getUuid().toString()).collect(Collectors.toSet()));
        for (ShortFamilyMember des : descendants) {
            String uuidCheck;
            if (des.getMotherUuid() != null && !Objects.equals(des.getMotherUuid(), mainMember.getUuid()) && !mainMember.getDescendants().contains(des.getMotherUuid().toString()))
                uuidCheck = des.getMotherUuid().toString();
            else if (des.getFatherUuid() != null && !Objects.equals(des.getFatherUuid(), mainMember.getUuid()) && !mainMember.getDescendants().contains(des.getFatherUuid().toString()))
                uuidCheck = des.getFatherUuid().toString();
            else uuidCheck = null;
            System.out.println(uuidCheck);
            if (uuidCheck != null) for (ShortFamilyMember anc :
                    oldAnc) {
                if (anc.getDescendants() != null && !anc.getDescendants().isBlank()
                        && anc.getDescendants().contains(uuidCheck) && !anc.getDescendants().contains(des.getUuid().toString())) {
                    Set<ShortFamilyMember> repairAnc = oldAnc.stream().filter(x -> x.getDescendants() != null).filter(x -> x.getDescendants().contains(anc.getUuid().toString())).collect(Collectors.toSet());
                    repairAnc.add(anc);
                    Set<ShortFamilyMember> repairDes = descendants.stream().filter(x -> x.getAncestors().contains(uuidCheck)).collect(Collectors.toSet());
                    repairDes.add(des);
                    for (ShortFamilyMember reAnc :
                            repairAnc) {
                        reAnc.setDescendants(mergeInfo(addUuidToInfo(des.getDescendants(), des.getUuid().toString()), reAnc.getDescendants()));
                        reAnc.setDescendantsGuard(mergeInfo(addUuidToInfo(des.getDescendantsGuard(), des.getLinkGuard()), reAnc.getDescendantsGuard()));
                    }

                    for (ShortFamilyMember reDes :
                            repairDes) {
                        reDes.setAncestors(mergeInfo(addUuidToInfo(anc.getAncestors(), anc.getUuid().toString()), reDes.getAncestors()));
                        reDes.setAncestorsGuard(mergeInfo(addUuidToInfo(anc.getAncestorsGuard(), anc.getLinkGuard()), reDes.getAncestorsGuard()));
                        if (anc.getTopAncestors() != null && !anc.getTopAncestors().isBlank())
                            reDes.setTopAncestors(mergeInfo(anc.getTopAncestors(), reDes.getTopAncestors()));
                        else reDes.setTopAncestors(mergeInfo(reDes.getTopAncestors(), anc.getUuid().toString()));
                    }
                    repair = true;
                }
            }
        }
        log.info((repair ? "починка после удаления произведена" : "починка послеудаления не нужна"));
        return repair;
    }

    @Transactional
    public void changeUuidInBloodTree(UUID oldUuid, ShortFamilyMember member, Set<ShortFamilyMember> ancestors) {
        Set<UUID> kinUuids = new HashSet<>();
        if (member.getPrimaryMembers() != null && !member.getPrimaryMembers().isBlank())
            kinUuids.addAll(getAllUuidFromInfo(member.getPrimaryMembers()));
        if (member.getDescendants() != null && !member.getDescendants().isBlank())
            kinUuids.addAll(getAllUuidFromInfo(member.getDescendants()));
        Set<ShortFamilyMember> bloodKin;
        if (!kinUuids.isEmpty()) bloodKin = memberRepository.getAllMembersByUuids(kinUuids);
        else bloodKin = new HashSet<>();
        bloodKin.addAll(ancestors);
        bloodKin.remove(member);
        for (ShortFamilyMember fm :
                bloodKin) {
            if (fm.getPrimaryMembers() != null && fm.getPrimaryMembers().contains(oldUuid.toString()))
                fm.setPrimaryMembers(changeUuidInInfo(fm.getPrimaryMembers(), oldUuid, member.getUuid()));
            else if (fm.getDescendants() != null && fm.getDescendants().contains(oldUuid.toString()))
                fm.setDescendants(changeUuidInInfo(fm.getDescendants(), oldUuid, member.getUuid()));
            else {
                if (fm.getAncestors() != null && fm.getAncestors().contains(oldUuid.toString()))
                    fm.setAncestors(changeUuidInInfo(fm.getAncestors(), oldUuid, member.getUuid()));
                if (fm.getTopAncestors() != null && fm.getTopAncestors().contains(oldUuid.toString()))
                    fm.setTopAncestors(changeUuidInInfo(fm.getTopAncestors(), oldUuid, member.getUuid()));
            }
        }
        log.warn("uuid in bloodKin is changed");
    }

    @Transactional
    public void changeParentInformation(ShortFamilyMember child, ShortFamilyMember parent, Sex parentSex) {
        if (parentSex == Sex.MALE) {
            child.setFatherUuid(parent.getUuid());
            child.setFatherInfo(parent.getFullName());
        } else if (parentSex == Sex.FEMALE) {
            child.setMotherUuid(parent.getUuid());
            child.setMotherInfo(parent.getFullName());
        }
    }

    @Transactional
    public void addChildToFamilyMember(ShortFamilyMember child, ShortFamilyMember parent, Sex parentSex) {
        changeParentInformation(child, parent, parentSex);
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
            desTopAnc.remove(parent.getUuid());
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
    public Set<String> repairGeneticTreeCheckStatus(Set<ShortFamilyMember> newTopAc, boolean includeTopBrothers) {
        Set<String> result = new HashSet<>();
        Set<ShortFamilyMember> members = getGeneticTreeMembers(newTopAc, includeTopBrothers);
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
        member.setCreator(guard.getTokenUser());
    }

    /***
     adding guard in kin of person
     @param member person who receive guard
     @param guard guard;
     ***/
    @Transactional
    public void addGuardToMemberKin(ShortFamilyMember member, Guard guard) {
        Set<ShortFamilyMember> memberKin = getAllKinMembersInMemberProfile(member);
        log.info("we are get profile kin");
        for (ShortFamilyMember mem :
                memberKin) {
            if (member.getPrimaryMembers() != null && member.getPrimaryMembers().contains(mem.getUuid().toString()))
                mem.setPrimaryGuard(addUuidToInfo(mem.getPrimaryGuard(), guard.getTokenUser()));
            if (member.getAncestors() != null && member.getAncestors().contains(mem.getUuid().toString()))
                mem.setDescendantsGuard(addUuidToInfo(mem.getDescendantsGuard(), guard.getTokenUser()));
            if (member.getDescendants() != null && member.getDescendants().contains(mem.getUuid().toString()))
                mem.setAncestorsGuard(addUuidToInfo(mem.getAncestorsGuard(), guard.getTokenUser()));
        }
        log.info("we are correct guards strings. prepare to flush");
        flush();
//        Set<ShortFamilyMember> memberAncestors = getAllAncestors(member);
//        Set<ShortFamilyMember> memberDescendants = getAllDescendants(member);
//        Set<ShortFamilyMember> primaryMembers = getAllPrimaryMembers(member);
//        memberAncestors.remove(member);
//        for (ShortFamilyMember anc :
//                memberAncestors) {
//            anc.setDescendantsGuard(addUuidToInfo(anc.getDescendantsGuard(), guard.getTokenUser()));
//            memberRepository.updateMember(anc);
//        }
//        for (ShortFamilyMember des :
//                memberDescendants) {
//            des.setAncestorsGuard(addUuidToInfo(des.getAncestorsGuard(), guard.getTokenUser()));
//            memberRepository.updateMember(des);
//        }
//        for (ShortFamilyMember prime :
//                primaryMembers) {
//            prime.setPrimaryGuard(addUuidToInfo(prime.getPrimaryGuard(), guard.getTokenUser()));
//            memberRepository.updateMember(prime);
    }


    @Transactional
    public Set<ShortFamilyMember> getAllKinMembersInMemberProfile(ShortFamilyMember member) {
        Set<UUID> kinUuids = new HashSet<>();
        if (member.getPrimaryMembers() != null && !member.getPrimaryMembers().isBlank())
            kinUuids.addAll(getAllUuidFromInfo(member.getPrimaryMembers()));
        if (member.getAncestors() != null && !member.getAncestors().isBlank())
            kinUuids.addAll(getAllUuidFromInfo(member.getAncestors()));
        if (member.getDescendants() != null && !member.getDescendants().isBlank())
            kinUuids.addAll(getAllUuidFromInfo(member.getDescendants()));
        log.info("we are find uuids of kin");
        return memberRepository.getAllMembersByUuids(kinUuids);

    }

    @Transactional
    public void flush() {
        memberRepository.flush();
    }

    @Transactional(readOnly = true)
    public CheckStatus getCheckStatus(ShortFamilyMember processMember, boolean fastCheck) {
        if (processMember.getLinkGuard() != null && !processMember.getLinkGuard().isBlank()) {
            return CheckStatus.LINKED;
        } else {
            if ((processMember.getDescendantsGuard() != null && !processMember.getDescendantsGuard().isBlank()) ||
                    (processMember.getAncestorsGuard() != null && !processMember.getAncestorsGuard().isBlank()) || (
                    processMember.getPrimaryGuard() != null && !processMember.getPrimaryGuard().isBlank())) {
                return CheckStatus.CHECKED;
            } else {
                if (!fastCheck && !getGeneticTreeGuards(getAllTopAncestors(processMember), true).isEmpty()) {
                    return CheckStatus.CHECKED;
                } else {
                    return CheckStatus.UNCHECKED;
                }
            }
        }
    }

    @Transactional
    public Collection<FamilyMemberDto> getMembersByFirstCreator(String guardUuid) {
        Collection<ShortFamilyMember> members = memberRepository.getAllMembersByFirstCreator(guardUuid);

        return familyMemberMapper.collectionEntityToCollectionDto(members);
    }

}