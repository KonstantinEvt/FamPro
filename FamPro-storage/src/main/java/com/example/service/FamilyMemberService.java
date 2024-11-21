package com.example.service;


import com.example.dtos.FamilyMemberDto;
import com.example.entity.FamilyMember;
import com.example.entity.OldFio;
import com.example.enums.CheckStatus;
import com.example.enums.Sex;
import com.example.enums.UserRoles;
import com.example.exceptions.Dublicate;
import com.example.exceptions.FamilyMemberNotFound;
import com.example.exceptions.ProblemWithId;
import com.example.exceptions.UncorrectedInformation;
import com.example.mappers.FamilyMemberMapper;
import com.example.mappers.FioMapper;
import com.example.repository.FamilyMemberRepo;
import com.example.utils.FamilyMemberUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public FamilyMemberService(FioMapper fioMapper,
                               FamilyMemberMapper familyMemberMapper,
                               FamilyMemberInfoService familyMemberInfoService,
                               LosingParentsService losingParentsService,
                               OldFioService oldFioService,
                               FamilyMemberRepo familyMemberRepo,
                               TokenService tokenService) {
        super(fioMapper);
        this.familyMemberMapper = familyMemberMapper;
        this.familyMemberInfoService = familyMemberInfoService;
        this.losingParentsService = losingParentsService;
        this.oldFioService = oldFioService;
        this.familyMemberRepo = familyMemberRepo;
        this.tokenService = tokenService;
    }

    @Transactional(readOnly = true)
    public FamilyMemberDto getFamilyMemberById(Long id) {
        FamilyMember familyMember = familyMemberRepo.findById(id)
                .orElseThrow(() -> new FamilyMemberNotFound("Человек с ID: ".concat(String.valueOf(id)).concat(" не найден")));
        FamilyMemberDto familyMemberDto = familyMemberMapper.entityToDto(familyMember);
        if (familyMember.getFamilyMemberInfo() != null) {
            familyMemberDto.setMemberInfo(familyMemberInfoService.getMemberInfo(familyMember));
        }
        return familyMemberDto;
    }

    @Transactional(readOnly = true)
    public FamilyMemberDto getFamilyMember(FamilyMemberDto familyMemberDto) {
        FamilyMember familyMember;
        if ((familyMemberDto.getFirstName() != null) &&
                (familyMemberDto.getMiddleName() != null) &&
                (familyMemberDto.getLastName() != null) &&
                familyMemberDto.getBirthday() != null) {
            UUID uuid = generateUUIDFromFio(familyMemberMapper.dtoToEntity(familyMemberDto));
            familyMember = familyMemberRepo.findFioByUuid(uuid)
                    .orElseThrow(() -> new FamilyMemberNotFound("Такой человек ".concat(" не найден")));
        } else familyMember = new FamilyMember();
        if (familyMember.getFamilyMemberInfo() != null) {
            familyMemberDto.setMemberInfo(familyMemberInfoService.getMemberInfo(familyMember));
        }
        System.out.println(familyMember);
        return familyMemberMapper.entityToDto(familyMember);
    }

    @Transactional
    public FamilyMemberDto addFamilyMember(FamilyMemberDto familyMemberDto) {
        log.info("--------ВНОСИМ НОВОГО ЧЕЛОВЕКА-------");
        if (familyMemberDto.getId() != null) throw new ProblemWithId("Удалите ID нового человека");
        if (familyMemberDto.getFirstName() == null
                || familyMemberDto.getMiddleName() == null
                || familyMemberDto.getLastName() == null
                || familyMemberDto.getBirthday() == null)
            throw new UncorrectedInformation("info not fully from creat person in base");
        FamilyMember familyMember = familyMemberMapper.dtoToEntity(familyMemberDto);
        familyMemberDto.setUuid(generateUUIDFromFio(familyMember));
        familyMember.setUuid(familyMemberDto.getUuid());
        Optional<FamilyMember> fm = familyMemberRepo.findFioByUuid(familyMemberDto.getUuid());
        if (fm.isPresent()) {
            throw new Dublicate("Такой человек уже есть в базе. Если Вы хотите его отредактировать - воспользуйтесь Patch-методом. ID человека: " + (fm.get().getId()));
        }
        log.info("Первичная информация установлена");

        extractExtensionOfFamilyMember(familyMemberDto, familyMember);
        familyMember.setCreator((String) tokenService.getTokenUser().getClaims().get("sub"));
        if (tokenService.getTokenUser().getRoles().contains(UserRoles.LINKED_USER.getNameSSO()))
            familyMember.setCheckStatus(CheckStatus.CHECKED);
        else familyMember.setCheckStatus(CheckStatus.UNCHECKED);
        familyMemberRepo.save(familyMember);
        addChangingToBase(familyMemberDto, familyMember);
        return familyMemberMapper.entityToDto(familyMemberRepo.save(familyMember));
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

        if (fm.getCheckStatus() == CheckStatus.CHECKED
                && !FamilyMemberUtils.checkRightsToEdit(fm, tokenService.getTokenUser()))
            throw new UncorrectedInformation("У Вас нет прав для изменения");
        if (fm.getCheckStatus() == CheckStatus.MODERATE
                && !FamilyMemberUtils.checkRightsToModerate(tokenService.getTokenUser()))
            throw new UncorrectedInformation("Находится на модерации");
        if (tokenService.getTokenUser().getRoles().contains(UserRoles.LINKED_USER.getNameSSO())
                && fm.getCheckStatus() == CheckStatus.UNCHECKED) fm.setCheckStatus(CheckStatus.CHECKED);

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
            throw new Dublicate("Информация в результате изменения совпадает с существующим человеком в базе. Его ID: " + existFM.get().getId());
        familyMemberDto.setUuid(freshUuid);
        log.info("Первичная информация установлена");

        extractExtensionOfFamilyMember(familyMemberDto, fm);

        fm.setUuid(familyMemberDto.getUuid());
        familyMemberRepo.save(fm);
        addChangingToBase(familyMemberDto, fm);
        if (fm.getChilds() != null) addChangesInParensInfo(fm.getChilds(), fm);
        familyMemberRepo.save(fm);
        return familyMemberMapper.entityToDto(fm);
    }


    public void checkOldNamesForAdditionalChilds(Set<FamilyMember> childrenOfFamilyMember, Set<OldFio> oldFios, FamilyMember familyMember) {
        if (childrenOfFamilyMember == null) childrenOfFamilyMember = new HashSet<>();
        for (OldFio oldFio : oldFios) {
            childrenOfFamilyMember.addAll(losingParentsService.checkForAdditionalChilds(oldFio.getUuid(), familyMember));
        }
    }

    public void addChangesInParensInfo(Set<FamilyMember> setOfChilds, FamilyMember fm) {

        if (fm.getSex() == Sex.MALE) {
            for (FamilyMember child : setOfChilds) {
                child.setFatherInfo(fm.getFullName());
                child.setFather(fm);
                familyMemberRepo.save(child);
            }
        } else for (FamilyMember child : setOfChilds) {
            child.setMotherInfo(fm.getFullName());
            child.setMother(fm);
            familyMemberRepo.save(child);
        }
    }

    @Transactional
    public void extractExtensionOfFamilyMember(FamilyMemberDto familyMemberDto, FamilyMember fm) {
        fm.setFullName(generateFioStringInfo(fm));
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
            throw new FamilyMemberNotFound("Человек с ID:".concat(String.valueOf(id)).concat(" не найден"));
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
}

