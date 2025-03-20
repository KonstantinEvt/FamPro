package com.example.service;


import com.example.dtos.DirectiveGuards;
import com.example.dtos.FamilyDirective;
import com.example.dtos.FamilyMemberDto;
import com.example.entity.FamilyMember;
import com.example.entity.OldFio;
import com.example.enums.*;
import com.example.exceptions.*;
import com.example.feign.FamilyConnectionClient;
import com.example.mappers.FamilyMemberMapper;
import com.example.mappers.FioMapper;
import com.example.repository.FamilyMemberRepo;
import com.example.utils.FamilyMemberUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
@Slf4j

public class FamilyMemberService extends FioServiceImp<FamilyMember> {
    private final FamilyMemberMapper familyMemberMapper;
    private final FamilyMemberInfoService familyMemberInfoService;
    private final LosingParentsService losingParentsService;
    private final OldFioService oldFioService;
    private final FamilyMemberRepo familyMemberRepo;
    private final TokenService tokenService;
    private final LinkedList<FamilyDirective> directives;
    private final FamilyConnectionClient familyConnectionClient;

    private final LinkedList<DirectiveGuards> directiveGuardsList;

    public FamilyMemberService(FioMapper fioMapper,
                               FamilyMemberMapper familyMemberMapper,
                               FamilyMemberInfoService familyMemberInfoService,
                               LosingParentsService losingParentsService,
                               OldFioService oldFioService,
                               FamilyMemberRepo familyMemberRepo,
                               TokenService tokenService,
                               LinkedList<FamilyDirective> directives,
                               FamilyConnectionClient familyConnectionClient,
                               LinkedList<DirectiveGuards> directiveGuardsList) {
        super(fioMapper);
        this.familyMemberMapper = familyMemberMapper;
        this.familyMemberInfoService = familyMemberInfoService;
        this.losingParentsService = losingParentsService;
        this.oldFioService = oldFioService;
        this.familyMemberRepo = familyMemberRepo;
        this.tokenService = tokenService;
        this.directives = directives;
        this.familyConnectionClient = familyConnectionClient;
        this.directiveGuardsList = directiveGuardsList;
    }

    @Transactional(readOnly = true)
    public FamilyMemberDto getFamilyMemberById(Long id) {
        FamilyMember familyMember = familyMemberRepo.findById(id)
                .orElseThrow(() -> new FamilyMemberNotFound("Человек с ID ".concat(String.valueOf(id)).concat(" не найден")));
        FamilyMemberDto familyMemberDto = familyMemberMapper.entityToDto(familyMember);
        if (familyMember.getFamilyMemberInfo() != null) {
            familyMemberDto.setMemberInfo(familyMemberInfoService.getMemberInfo(familyMember));
        }
        return familyMemberDto;
    }

    @Transactional(readOnly = true)
    public FamilyMemberDto getFamilyMember(FamilyMemberDto familyMemberDto) {
        if ((familyMemberDto.getFirstName() != null) &&
                (familyMemberDto.getMiddleName() != null) &&
                (familyMemberDto.getLastName() != null) &&
                familyMemberDto.getBirthday() != null) {
            UUID uuid = generateUUIDFromFio(familyMemberMapper.dtoToEntity(familyMemberDto));
            FamilyMember familyMember = familyMemberRepo.findFioByUuid(uuid)
                    .orElseThrow(() -> new FamilyMemberNotFound("Такой человек не найден"));
            FamilyMemberDto dto = familyMemberMapper.entityToDto(familyMember);
            if (familyMember.getFamilyMemberInfo() != null) {
                dto.setMemberInfo(familyMemberInfoService.getMemberInfo(familyMember));
            }
            return dto;
        } else throw new RuntimeException("Info not fully");
    }

    @Transactional
    public FamilyMemberDto addFamilyMember(FamilyMemberDto familyMemberDto) {
        log.info("--------ВНОСИМ НОВОГО ЧЕЛОВЕКА-------");
        if (familyMemberDto.getId() != null) throw new ProblemWithId("Удалите ID нового человека");
        if (familyMemberDto.getFirstName() == null
                || familyMemberDto.getMiddleName() == null
                || familyMemberDto.getLastName() == null
                || familyMemberDto.getBirthday() == null)
            throw new UncorrectedInformation("info not fully for creat person in base");
        FamilyMember familyMember = familyMemberMapper.dtoToEntity(familyMemberDto);
        familyMemberDto.setUuid(generateUUIDFromFio(familyMember));
        familyMember.setUuid(familyMemberDto.getUuid());
        Optional<FamilyMember> fm = familyMemberRepo.findFioByUuid(familyMemberDto.getUuid());
        if (fm.isPresent()) {
            throw new Dublicate("Такой человек уже есть в базе. Если Вы хотите его отредактировать - воспользуйтесь Patch-методом. ID человека " + (fm.get().getId()));
        } else {
            Optional<OldFio> existOldFio = oldFioService.getFioRepo().findFioByUuid(familyMemberDto.getUuid());
            if (existOldFio.isPresent())
                throw new Dublicate("Такой человек уже есть в базе. Это его альтернативное имя. ID человека " + (existOldFio.get().getMember()).getId());
        }
        familyMember.setCreator((String) tokenService.getTokenUser().getClaims().get("sub"));
        familyMember.setCreateTime(new Timestamp(System.currentTimeMillis()));
        FamilyMemberUtils.selectCheckStatus(familyMember, tokenService.getTokenUser().getRoles());
        if (familyMemberDto.getBurial() != null) familyMember.getBurial().setUuid(familyMemberDto.getUuid());
        if (familyMemberDto.getBirth() != null) familyMember.getBirth().setUuid(familyMemberDto.getUuid());
        log.info("Первичная информация установлена");
        familyMemberRepo.save(familyMember);

        extractExtensionOfFamilyMember(familyMemberDto, familyMember);

        familyMemberRepo.save(familyMember);
        addChangingToBase(familyMemberDto, familyMember);
        if (familyMember.getChilds() != null)
            addChangesInParensInfo(familyMember.getChilds(), familyMember, familyMember.getUuid());

        FamilyMemberDto result = familyMemberMapper.entityToDto(familyMemberRepo.save(familyMember));
        result.setMemberInfo(familyMemberInfoService.getMemberInfo(familyMember));
        // Если нужны старые имена и прозвища в модуле family
//        result.setFioDtos(oldFioService.getOldNamesMapper().oldFiosSetToFioDtoSet(familyMember.getOtherNames()));
        directives.add(FamilyDirective.builder()
                .familyMemberDto(result)
                .tokenUser(familyMember.getCreator())
                .person(result.getUuid().toString())
                .switchPosition(SwitchPosition.MAIN)
                .operation(KafkaOperation.ADD).build());
        return result;
    }


    @Transactional(readOnly = true)
    public Collection<FamilyMemberDto> getAllFamilyMembers() {
        List<FamilyMember> familyMemberList = familyMemberRepo.findAll();
        List<FamilyMemberDto> familyMemberDtoList = new ArrayList<>();
        for (FamilyMember familyMember : familyMemberList) {
            familyMemberDtoList.add(familyMemberMapper.entityToDto(familyMember));
        }
        log.info("Коллекция всех людей из базы выдана");
        return familyMemberDtoList;
    }

    @Transactional
    public FamilyMemberDto linkFamilyMember(Long id) {
        FamilyMember fm = familyMemberRepo.findById(id).orElseThrow(() -> new FamilyMemberNotFound("Что-то пошло не так. Запись исчезла во время распределенной транзакции"));
        fm.setCheckStatus(CheckStatus.LINKED);
        fm.setCreator((String) tokenService.getTokenUser().getClaims().get("sub"));
        familyMemberRepo.save(fm);
        return familyMemberMapper.entityToDto(fm);
    }

    @Transactional
    public FamilyMemberDto updateFamilyMember(FamilyMemberDto familyMemberDto) {
        log.info("--------ИЗМЕНЯЕМ ЧЕЛОВЕКА-------");
        Long dtoId = familyMemberDto.getId();
        FamilyMember fm;
        if (dtoId != null) {
            fm = familyMemberRepo.findById(dtoId).orElseThrow(() ->
                    new FamilyMemberNotFound("Попытка изменить человека по Id, которого нет в базе"));
        } else if (familyMemberDto.getUuid() != null) {
            fm = familyMemberRepo.findFioByUuid(familyMemberDto.getUuid()).orElseThrow(() ->
                    new FamilyMemberNotFound("Попытка изменить человека по UUID, которого нет в базе"));
        } else throw new ProblemWithId("Ни Id, ни UUID не указан для поиска/изменения человека");

        if (!FamilyMemberUtils.checkRightsToEdit(fm, tokenService.getTokenUser())) {
            directiveGuardsList.add(DirectiveGuards.builder()
                    .created(new Timestamp(System.currentTimeMillis()))
                    .tokenUser((String) tokenService.getTokenUser().getClaims().get("sub"))
                    .switchPosition(SwitchPosition.MAIN)
                    .info1("You are havent rights to change this person")
                    .info2(fm.getFullName())
                    .build());
            throw new RightsIsAbsent("У Вас нет прав для изменения");
        }
        if (fm.getCheckStatus() == CheckStatus.CHECKED
                && !tokenService.getTokenUser().getRoles().contains(UserRoles.MANAGER.getNameSSO())
                && !tokenService.getTokenUser().getRoles().contains(UserRoles.ADMIN.getNameSSO())
                && !familyConnectionClient.checkRights((UUID) tokenService.getTokenUser().getClaims().get("sub"))){
            throw new RightsIsAbsent("У Вас нет прав для изменения");
        }
        if (fm.getCheckStatus() == CheckStatus.MODERATE
                && !FamilyMemberUtils.checkRightsToModerate(tokenService.getTokenUser())) {
            directiveGuardsList.add(DirectiveGuards.builder()
                    .created(new Timestamp(System.currentTimeMillis()))
                    .tokenUser((String) tokenService.getTokenUser().getClaims().get("sub"))
                    .switchPosition(SwitchPosition.MAIN)
                    .info1("trying changing person under voting or moderating")
                    .info2(fm.getFullName())
                    .build());
            throw new ModeratingContent("Находится на модерцаии");
        }
        Set<FamilyMember> currentChildrenOfFamilyMember = fm.getChilds();
        if (familyMemberDto.getFirstName() != null) fm.setFirstName(familyMemberDto.getFirstName());
        if (familyMemberDto.getBirthday() != null && (currentChildrenOfFamilyMember == null || currentChildrenOfFamilyMember.isEmpty())) {
            fm.setBirthday(familyMemberDto.getBirthday());
            if (fm.getOtherNames() != null && !fm.getOtherNames().isEmpty())
                oldFioService.changeOldFiosBirthday(fm);
        } else if (familyMemberDto.getBirthday() != null && familyMemberDto.getBirthday().toLocalDate() != fm.getBirthday().toLocalDate()) {
            throw new UncorrectedInformation("Изменять день рождения человека, у которого в базе имеются подтвержденные дети, невозможно");
        }
        if (familyMemberDto.getLastName() != null) fm.setLastName(familyMemberDto.getLastName());
        if (familyMemberDto.getMiddleName() != null) fm.setMiddleName(familyMemberDto.getMiddleName());
        if (familyMemberDto.getSex() != null && (currentChildrenOfFamilyMember == null || currentChildrenOfFamilyMember.isEmpty()))
            fm.setSex(familyMemberDto.getSex());
        else if (familyMemberDto.getSex() != null && familyMemberDto.getSex() != fm.getSex()) {
            throw new UncorrectedInformation("Изменять пол человека, у которого в базе имеются подтвержденные дети, невозможно");
        }

        UUID freshUuid = generateUUIDFromFio(fm);
        Optional<FamilyMember> existFM = familyMemberRepo.findFioByUuid(freshUuid);
        if (existFM.isPresent() && !existFM.get().getId().equals(fm.getId()))
            throw new Dublicate("Информация в результате изменения совпадает с существующим человеком в базе. Его ID " + existFM.get().getId());
        else {
            Optional<OldFio> existOldName = oldFioService.getFioRepo().findFioByUuid(freshUuid);
            if (existOldName.isPresent() && !existOldName.get().getId().equals(fm.getId()))
                throw new Dublicate("Информация в результате изменения совпадает с альтернативным именем человека в базе. Его ID " + existOldName.get().getMember().getId());
        }
        familyMemberDto.setUuid(freshUuid);
        log.info("Первичная информация установлена");

        extractExtensionOfFamilyMember(familyMemberDto, fm);

        familyMemberDto.setUuid(fm.getUuid());
        fm.setUuid(freshUuid);
        familyMemberRepo.save(fm);
        addChangingToBase(familyMemberDto, fm);
        if (fm.getChilds() != null) addChangesInParensInfo(fm.getChilds(), fm, familyMemberDto.getUuid());
        familyMemberRepo.save(fm);

        FamilyMemberDto result = familyMemberMapper.entityToDto(fm);
        result.setMemberInfo(familyMemberInfoService.getMemberInfo(fm));
// Если нужны старые имена и прозвища в модуле family
        //        result.setFioDtos(oldFioService.getOldNamesMapper().oldFiosSetToFioDtoSet(fm.getOtherNames()));
        directives.add(FamilyDirective.builder()
                .familyMemberDto(result)
                .tokenUser((String) tokenService.getTokenUser().getClaims().get("sub"))
                .person(familyMemberDto.getUuid().toString())
                .switchPosition(SwitchPosition.MAIN)
                .operation(KafkaOperation.RENAME).build());
        return result;
    }


    public void checkOldNamesForAdditionalChilds(Set<FamilyMember> childrenOfFamilyMember, Set<OldFio> oldFios, FamilyMember familyMember) {
        if (childrenOfFamilyMember == null) childrenOfFamilyMember = new HashSet<>();
        for (OldFio oldFio : oldFios) {
            childrenOfFamilyMember.addAll(losingParentsService.checkForAdditionalChilds(oldFio.getUuid(), familyMember));
        }
    }

    @Transactional
    public void addChangesInParensInfo(Set<FamilyMember> setOfChilds, FamilyMember fm, UUID uuid) {

        if (fm.getSex() == Sex.MALE) {
            for (FamilyMember child : setOfChilds) {
                child.setFatherInfo(fm.getFullName());
                child.setFather(fm);
                familyMemberRepo.save(child);
                sendChildDirective(child, uuid);
            }
        } else for (FamilyMember child : setOfChilds) {
            child.setMotherInfo(fm.getFullName());
            child.setMother(fm);
            familyMemberRepo.save(child);
            sendChildDirective(child, uuid);
        }
    }

    public void sendChildDirective(FamilyMember child, UUID uuid) {
        directives.add(FamilyDirective.builder()
                .familyMemberDto(familyMemberMapper.entityToDto(child))
                .tokenUser((String) tokenService.getTokenUser().getClaims().get("sub"))
                .person(uuid.toString())
                .switchPosition(SwitchPosition.CHILD)
                .operation(KafkaOperation.EDIT).build());
    }

    @Transactional
    public void extractExtensionOfFamilyMember(FamilyMemberDto familyMemberDto, FamilyMember fm) {
        fm.setFullName(generateFioStringInfo(fm));
        if (familyMemberDto.getFatherFio() != null && familyMemberDto.getMotherFio() != null
                && Objects.equals(familyMemberDto.getFatherFio().getFirstName(), familyMemberDto.getMotherFio().getFirstName())
                && Objects.equals(familyMemberDto.getFatherFio().getMiddleName(), familyMemberDto.getMotherFio().getMiddleName())
                && Objects.equals(familyMemberDto.getFatherFio().getLastName(), familyMemberDto.getMotherFio().getLastName())
        ) throw new UncorrectedInformation("It's not funny. Mother and Father must be different people");

        if (familyMemberDto.getFatherFio() != null) {
            losingParentsService.setUpFather(familyMemberDto.getFatherFio(), fm);
        }
        if (familyMemberDto.getMotherFio() != null) {
            losingParentsService.setUpMother(familyMemberDto.getMotherFio(), fm);
        }
        if (familyMemberDto.getMemberInfo() != null) {
            familyMemberDto.getMemberInfo().setId(null);
            fm.setFamilyMemberInfo(familyMemberInfoService.merge(familyMemberDto, fm.getUuid()));
        }
        log.info("Расширенная информация проверена и установлена");
    }


    //remove must be change
    public String removeFamilyMember(Long id) {
        Optional<FamilyMember> remFM = familyMemberRepo.findById(id);
        if (remFM.isEmpty())
            throw new FamilyMemberNotFound("Человек с ID ".concat(String.valueOf(id)).concat(" не найден"));
        if (remFM.get().getSex() == Sex.MALE) {
            List<FamilyMember> link1 = familyMemberRepo.findAllByFather_Id(id);
            for (FamilyMember fm : link1
            ) {
                fm.setFather(null);
            }
        }
        if (remFM.get().getSex() == Sex.FEMALE) {
            List<FamilyMember> link2 = familyMemberRepo.findAllByMother_Id(id);
            for (FamilyMember fm : link2
            ) {
                fm.setMother(null);
            }
        }
        familyMemberRepo.deleteById(id);
        return String.format("Человек: %s %s %s удален, из базы",
                remFM.get().getFirstName(),
                remFM.get().getMiddleName(),
                remFM.get().getLastName());
    }

    @Transactional
    public void addChangingToBase(FamilyMemberDto familyMemberDto, FamilyMember familyMember) {
        Set<FamilyMember> childrenOfFamilyMember = losingParentsService.checkForAdditionalChilds(familyMember.getUuid(), familyMember);
        if (familyMemberDto.getFioDtos() != null) {
            Set<OldFio> oldFios = oldFioService.addAllNewOldNames(familyMemberDto.getFioDtos(), familyMember);
            if (oldFios != null) {
                checkOldNamesForAdditionalChilds(childrenOfFamilyMember, oldFios, familyMember);
                if (familyMember.getOtherNames() != null) familyMember.getOtherNames().addAll(oldFios);
                else familyMember.setOtherNames(oldFios);
            }
        }
        if (childrenOfFamilyMember != null)
            if (familyMember.getChilds() != null && !familyMember.getChilds().isEmpty()) {
                familyMember.getChilds().addAll(childrenOfFamilyMember);
            } else familyMember.setChilds(childrenOfFamilyMember);

        if (familyMember.getFather() == null &&
                familyMemberDto.getFatherFio() != null && familyMember.getFatherInfo() != null &&
                familyMember.getFatherInfo().charAt(1) == 'A') {
            losingParentsService.addLosingParent(familyMemberDto.getFatherFio(), familyMember, Sex.MALE);
        }
        if (familyMember.getMother() == null &&
                familyMemberDto.getMotherFio() != null && familyMember.getMotherInfo() != null &&
                familyMember.getMotherInfo().charAt(1) == 'A') {
            losingParentsService.addLosingParent(familyMemberDto.getMotherFio(), familyMember, Sex.FEMALE);
        }
    }

    @Transactional
    public void changeParentsAfterVoting(FamilyDirective directive) {
        FamilyMember familyMember = familyMemberRepo.findFioByUuid(UUID.fromString(directive.getPerson())).orElseThrow(() -> new RuntimeException("family member not found"));
        switch (directive.getSwitchPosition()) {
            case MAIN -> {
                if (familyMember.getFather() != null) familyMember.setFather(null);
                else
                    losingParentsService.removeParentByLosingUuid(losingParentsService.generateUUIDFromFullName(familyMember.getFatherInfo()), familyMember);
                if (familyMember.getMother() != null) familyMember.setMother(null);
                else
                    losingParentsService.removeParentByLosingUuid(losingParentsService.generateUUIDFromFullName(familyMember.getMotherInfo()), familyMember);
                familyMember.setFatherInfo(null);
                familyMember.setMotherInfo(null);
            }
            case FATHER -> {
                if (familyMember.getFather() != null) familyMember.setFather(null);
                else
                    losingParentsService.removeParentByLosingUuid(losingParentsService.generateUUIDFromFullName(familyMember.getFatherInfo()), familyMember);
                familyMember.setFatherInfo(null);
            }
            case MOTHER -> {
                if (familyMember.getMother() != null) familyMember.setMother(null);
                else
                    losingParentsService.removeParentByLosingUuid(losingParentsService.generateUUIDFromFullName(familyMember.getMotherInfo()), familyMember);
                familyMember.setMotherInfo(null);
            }
            default -> log.warn("found unknown directive");
        }
        familyMemberRepo.save(familyMember);
    }

    @Transactional
    public void changeCheckStatus(FamilyDirective directive) {
        FamilyMember familyMember = familyMemberRepo.findFioByUuid(UUID.fromString(directive.getPerson())).orElseThrow(() -> new RuntimeException("family member not found"));
        switch (directive.getSwitchPosition()) {
            case MAIN -> familyMember.setCheckStatus(CheckStatus.MODERATE);
            case FATHER -> {
                familyMember.setCheckStatus(CheckStatus.LINKED);
                if (directive.getTokenUser() != null) familyMember.setCreator(directive.getTokenUser());
            }
            case MOTHER -> {
                familyMember.setCheckStatus(CheckStatus.CHECKED);
                if (directive.getTokenUser() != null) familyMember.setCreator(directive.getTokenUser());
            }
            case CHILD -> familyMember.setCheckStatus(CheckStatus.UNCHECKED);
            default -> log.warn("found unknown directive");
        }
        familyMemberRepo.save(familyMember);
    }
}

