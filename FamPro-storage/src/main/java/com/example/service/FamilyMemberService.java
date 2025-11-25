package com.example.service;


import com.example.dtos.*;
import com.example.entity.*;
import com.example.enums.*;
import com.example.exceptions.*;
import com.example.feign.FamilyConnectionClient;
import com.example.mappers.*;
import com.example.repository.FamilyMemberRepo;
import com.example.repository.MainOtherFioRepository;
import com.example.repository.MainStorageRepository;
import com.example.utils.FamilyMemberUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

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
    private final LinkedList<Directive> directivePhotos;
    private final Map<String, String> tempPhotoAccept;
    private final MainStorageRepository mainStorageRepository;
    private final MainOtherFioRepository mainOtherFioRepository;
    private final OldNamesMapper oldNamesMapper;
    private final Map<String, FamilyMemberDto> tempGuardStatus;

    private final Map<String, FamilyMemberDto> tempExtendedDto;
    private final Map<Long, Timestamp> lastUpdateMap;
    private final Map<String, MainContact> tempMainContact;

    public FamilyMemberService(FioMapper fioMapper,
                               FamilyMemberMapper familyMemberMapper,
                               FamilyMemberInfoService familyMemberInfoService,
                               LosingParentsService losingParentsService,
                               OldFioService oldFioService,
                               FamilyMemberRepo familyMemberRepo,
                               TokenService tokenService,
                               LinkedList<FamilyDirective> directives,
                               FamilyConnectionClient familyConnectionClient,
                               LinkedList<DirectiveGuards> directiveGuardsList,
                               LinkedList<Directive> directivePhotos,
                               Map<String, String> tempPhotoAccept,
                               MainStorageRepository mainStorageRepository,
                               MainOtherFioRepository mainOtherFioRepository,
                               OldNamesMapper oldNamesMapper,
                               Map<String, FamilyMemberDto> tempGuardStatus,
                               Map<String, FamilyMemberDto> tempExtendedDto, Map<Long, Timestamp> lastUpdateMap, Map<String, MainContact> tempMainContact) {
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
        this.directivePhotos = directivePhotos;
        this.tempPhotoAccept = tempPhotoAccept;
        this.mainStorageRepository = mainStorageRepository;
        this.mainOtherFioRepository = mainOtherFioRepository;
        this.oldNamesMapper = oldNamesMapper;
        this.tempGuardStatus = tempGuardStatus;
        this.tempExtendedDto = tempExtendedDto;
        this.lastUpdateMap = lastUpdateMap;
        this.tempMainContact = tempMainContact;
    }

    @Transactional(readOnly = true)
    public FamilyMemberDto getFamilyMemberById(Long id) {
        FamilyMember familyMember = mainStorageRepository.findMemberWithInfoById(id)
                .orElseThrow(() -> new FamilyMemberNotFound(StringUtils.join("person with id:", id, "not found", ' ')));
        FamilyMemberDto familyMemberDto = familyMemberMapper.entityToDto(familyMember);

        lastUpdateMap.put(familyMember.getId(), familyMember.getLastUpdate());
        log.info("put last update {}", lastUpdateMap.get(familyMember.getId()));
        if (familyMember.getFamilyMemberInfo() != null) {
            familyMemberDto.setMemberInfo(familyMemberInfoService.getSimpleInfoDto(familyMember.getFamilyMemberInfo().get(0)));
        }
        getAndSetTempStatus(familyMemberDto);
        return familyMemberDto;
    }

    private void getAndSetTempStatus(FamilyMemberDto familyMemberDto) {
        String token = (String) tokenService.getTokenUser().getClaims().get("sub");
        if (familyMemberDto.getCheckStatus() != CheckStatus.UNCHECKED && !Objects.equals(familyMemberDto.getCreator(), token) && token != null)
            changeMemberDtoByGuardStatus(familyMemberDto);
        else {
            if (!Objects.equals(familyMemberDto.getCreator(), token) && token != null) {
                familyMemberDto.setSecretLevelRemove(SecretLevel.CLOSE);
                familyMemberDto.setSecretLevelMainInfo(SecretLevel.CLOSE);
                familyMemberDto.setSecretLevelBirthday(SecretLevel.CLOSE);
                if (familyMemberDto.getSecretLevelPhoto() != SecretLevel.OPEN)
                    familyMemberDto.setSecretLevelPhoto(SecretLevel.CLOSE);
            }
            if (familyMemberDto.getSecretLevelPhoto() != SecretLevel.CLOSE && familyMemberDto.isPrimePhoto())
                tempPhotoAccept.put(token.concat(String.valueOf(SwitchPosition.PRIME.ordinal())), String.valueOf(SwitchPosition.PRIME.ordinal()).concat(familyMemberDto.getUuid().toString()));
            if (familyMemberDto.getMemberInfo() != null && familyMemberDto.getMemberInfo().isPhotoBirthExist())
                tempPhotoAccept.put(token.concat(String.valueOf(SwitchPosition.BIRTH.ordinal())), String.valueOf(SwitchPosition.BIRTH.ordinal()).concat(familyMemberDto.getUuid().toString()));
            if (familyMemberDto.getMemberInfo() != null && familyMemberDto.getMemberInfo().isPhotoBurialExist())
                tempPhotoAccept.put(token.concat(String.valueOf(SwitchPosition.BURIAL.ordinal())), String.valueOf(SwitchPosition.BURIAL.ordinal()).concat(familyMemberDto.getUuid().toString()));

        }
        if (familyMemberDto.getCheckStatus() == CheckStatus.MODERATE) {
            familyMemberDto.setSecretLevelRemove(SecretLevel.CLOSE);
            familyMemberDto.setSecretLevelEdit(SecretLevel.CLOSE);
            familyMemberDto.setSecretLevelMainInfo(SecretLevel.CLOSE);
            familyMemberDto.setSecretLevelBirthday(SecretLevel.CLOSE);
        }
        tempGuardStatus.put(token, familyMemberDto);
        tempMainContact.put(token, new MainContact(familyMemberDto.getMemberInfo().getMainPhone(),
                familyMemberDto.getMemberInfo().getMainEmail(),
                familyMemberDto.getMemberInfo().getMainAddress()));
    }

    private void changeMemberDtoByGuardStatus(FamilyMemberDto familyMemberDto) {
        SecretLevel guardStatus;
        Set<String> roles = tokenService.getTokenUser().getRoles();
        if (roles.contains(UserRoles.ADMIN.getNameSSO()) || roles.contains(UserRoles.MANAGER.getNameSSO()))
            guardStatus = SecretLevel.CONFIDENTIAL;
        else if (roles.contains(UserRoles.LINKED_USER.getNameSSO()))
            guardStatus = familyConnectionClient.getGuardStatus(familyMemberDto.getUuid());
        else guardStatus = SecretLevel.OPEN;

        log.info(StringUtils.join("status user: ", guardStatus.name(), " photo status person: ", familyMemberDto.getSecretLevelPhoto().name(), ' '));
        if (familyMemberDto.isPrimePhoto() && guardStatus.ordinal() < familyMemberDto.getSecretLevelPhoto().ordinal())
            familyMemberDto.setSecretLevelPhoto(SecretLevel.CLOSE);
        else {
            if (familyMemberDto.isPrimePhoto())
                tempPhotoAccept.put(((String) tokenService.getTokenUser().getClaims().get("sub")).concat(String.valueOf(SwitchPosition.PRIME.ordinal())), String.valueOf(SwitchPosition.PRIME.ordinal()).concat(familyMemberDto.getUuid().toString()));
        }
        if (guardStatus.ordinal() < familyMemberDto.getSecretLevelMainInfo().ordinal())
            familyMemberDto.setSecretLevelMainInfo(SecretLevel.CLOSE);
        if (guardStatus.ordinal() < familyMemberDto.getSecretLevelEdit().ordinal())
            familyMemberDto.setSecretLevelEdit(SecretLevel.CLOSE);
        if (guardStatus.ordinal() < familyMemberDto.getSecretLevelRemove().ordinal())
            familyMemberDto.setSecretLevelRemove(SecretLevel.CLOSE);
        if (guardStatus.ordinal() < familyMemberDto.getSecretLevelBirthday().ordinal())
            familyMemberDto.setSecretLevelBirthday(SecretLevel.CLOSE);

        if (familyMemberDto.getMemberInfo() != null) {
            if (familyMemberDto.getMemberInfo().isPhotoBirthExist() && guardStatus.ordinal() < familyMemberDto.getMemberInfo().getSecretLevelBirth().ordinal()) {
                familyMemberDto.getMemberInfo().setSecretLevelBirth(SecretLevel.CLOSE);
            } else {
                if (familyMemberDto.getMemberInfo().isPhotoBirthExist())
                    tempPhotoAccept.put(((String) tokenService.getTokenUser().getClaims().get("sub")).concat(String.valueOf(SwitchPosition.BIRTH.ordinal())), String.valueOf(SwitchPosition.BIRTH.ordinal()).concat(familyMemberDto.getUuid().toString()));
            }
            if (familyMemberDto.getMemberInfo().isPhotoBurialExist() && guardStatus.ordinal() < familyMemberDto.getMemberInfo().getSecretLevelBurial().ordinal()) {
                familyMemberDto.getMemberInfo().setSecretLevelBurial(SecretLevel.CLOSE);
            } else {
                if (familyMemberDto.getMemberInfo().isPhotoBurialExist())
                    tempPhotoAccept.put(((String) tokenService.getTokenUser().getClaims().get("sub")).concat(String.valueOf(SwitchPosition.BURIAL.ordinal())), String.valueOf(SwitchPosition.BURIAL.ordinal()).concat(familyMemberDto.getUuid().toString()));
            }
            if (familyMemberDto.getMemberInfo().getMainEmail() != null
                    && guardStatus.ordinal() < familyMemberDto.getMemberInfo().getSecretLevelEmail().ordinal()) {
                familyMemberDto.getMemberInfo().setSecretLevelEmail(SecretLevel.CLOSE);
//                familyMemberDto.getMemberInfo().setEmails(null);
                familyMemberDto.getMemberInfo().setMainEmail(null);
            }
            if (familyMemberDto.getMemberInfo().getMainPhone() != null
                    && guardStatus.ordinal() < familyMemberDto.getMemberInfo().getSecretLevelPhone().ordinal()) {
                familyMemberDto.getMemberInfo().setSecretLevelPhone(SecretLevel.CLOSE);
//                familyMemberDto.getMemberInfo().setPhones(null);
                familyMemberDto.getMemberInfo().setMainPhone(null);
            }
            if (familyMemberDto.getMemberInfo().getMainAddress() != null
                    && guardStatus.ordinal() < familyMemberDto.getMemberInfo().getSecretLevelAddress().ordinal()) {
                familyMemberDto.getMemberInfo().setSecretLevelAddress(SecretLevel.CLOSE);
//                familyMemberDto.getMemberInfo().setAddresses(null);
                familyMemberDto.getMemberInfo().setMainAddress(null);
            }
            if (guardStatus.ordinal() < familyMemberDto.getMemberInfo().getSecretLevelBiometric().ordinal()) {
                familyMemberDto.getMemberInfo().setSecretLevelBiometric(SecretLevel.CLOSE);
//                familyMemberDto.getMemberInfo().setBiometric(null);
            }
            if (guardStatus.ordinal() < familyMemberDto.getMemberInfo().getSecretLevelDescription().ordinal()) {
                familyMemberDto.getMemberInfo().setSecretLevelDescription(SecretLevel.CLOSE);
//                familyMemberDto.getMemberInfo().setDescription(null);
            }
        }
    }

    @Transactional(readOnly = true)
    public FamilyMemberDto getExtendedInfoFamilyMember(SecurityDto securityDto) {
        if (!securityDto.getOwner().equals(tokenService.getTokenUser().getClaims().get("sub"))) {
            log.warn("попытка взлома");
            throw new RuntimeException("В доступе отказано. Не хорошо так делать. Мы же к Вам со свей душой, а Вы... ");
        }
//        check for change
        if (!Objects.equals(securityDto.getLastUpdate(), lastUpdateMap.get(securityDto.getPersonId()))) {
            throw new RuntimeException("Между обычным и расширенным запросом запись изменилась. Найдите персону заново");
        }
        String token = (String) tokenService.getTokenUser().getClaims().get("sub");
        FamilyMemberDto tempGuard = tempGuardStatus.get(token);
        FamilyMemberInfoDto infoDto = familyMemberInfoService.covertSecurityDtoToInfo(securityDto);
        if (!familyMemberInfoService.equalsSecurity(infoDto, tempGuard.getMemberInfo())) {
            throw new RuntimeException("Secret status is forged");
        }
        FamilyMemberDto familyMemberDto = new FamilyMemberDto();
        if (securityDto.isOtherNamesExist()) {
            familyMemberDto.setFioDtos(oldNamesMapper.oldFiosSetToFioDtoSet(oldFioService.getOtherNamesByInfoId(securityDto.getPersonId())));
            tempGuard.setFioDtos(familyMemberDto.getFioDtos());
        }
        if (securityDto.isInfoExist()) {
            familyMemberDto.setMemberInfo(familyMemberInfoService.getInfoDto(familyMemberInfoService.getSimpleInfo(infoDto)));
            tempGuard.setMemberInfo(familyMemberDto.getMemberInfo());
        }
        tempExtendedDto.put(token, tempGuard);
        return familyMemberDto;
    }

    @Transactional(readOnly = true)
    public FamilyMemberDto getFamilyMember(FamilyMemberDto familyMemberDto) {
        if ((familyMemberDto.getFirstName() != null) &&
                (familyMemberDto.getMiddleName() != null) &&
                (familyMemberDto.getLastName() != null) &&
                familyMemberDto.getBirthday() != null) {
            UUID uuid = generateUUIDFromFio(familyMemberMapper.dtoToEntity(familyMemberDto));
            Optional<FamilyMember> fm = mainStorageRepository.findMemberWithInfoByUUID(uuid);
            if (fm.isEmpty()) fm = mainStorageRepository.findMemberWithInfoByOldNameUUID(uuid);
            FamilyMember familyMember = fm.orElseThrow(() -> new FamilyMemberNotFound("Такой человек не найден"));
            FamilyMemberDto dto = familyMemberMapper.entityToDto(familyMember);
            lastUpdateMap.put(familyMember.getId(), familyMember.getLastUpdate());
            if (familyMember.getFamilyMemberInfo() != null) {
                dto.setMemberInfo(familyMemberInfoService.getSimpleInfoDto(familyMember.getFamilyMemberInfo().get(0)));
            }
            getAndSetTempStatus(dto);
            return dto;
        } else throw new RuntimeException("Info not fully");
    }

    @Transactional(readOnly = true)
    public FamilyMemberDto getYourself() {
        UUID uuid = UUID.fromString(familyConnectionClient.getGuardByLink());
        FamilyMember familyMember = mainStorageRepository.findMemberWithInfoByUUID(uuid)
                .orElseThrow(() -> new FamilyMemberNotFound("Странно... линк есть в фэмили, а человека в базе нет"));
        FamilyMemberDto dto = familyMemberMapper.entityToDto(familyMember);
        if (familyMember.isPrimePhoto())
            tempPhotoAccept.put(((String) tokenService.getTokenUser().getClaims().get("sub")).concat(String.valueOf(SwitchPosition.PRIME.ordinal())), String.valueOf(SwitchPosition.PRIME.ordinal()).concat(uuid.toString()));
        if (familyMember.isOtherNamesExist())
            dto.setFioDtos(oldNamesMapper.oldFiosSetToFioDtoSet(oldFioService.getOtherNamesByInfoId(familyMember.getId())));
        if (familyMember.getFamilyMemberInfo() != null && !familyMember.getFamilyMemberInfo().isEmpty()) {
            dto.setMemberInfo(familyMemberInfoService.getInfoDto(familyMember.getFamilyMemberInfo().get(0)));
            if (dto.getMemberInfo().isPhotoBurialExist())
                tempPhotoAccept.put(((String) tokenService.getTokenUser().getClaims().get("sub")).concat(String.valueOf(SwitchPosition.BURIAL.ordinal())), String.valueOf(SwitchPosition.PRIME.ordinal()).concat(uuid.toString()));
            if (dto.getMemberInfo().isPhotoBirthExist())
                tempPhotoAccept.put(((String) tokenService.getTokenUser().getClaims().get("sub")).concat(String.valueOf(SwitchPosition.BIRTH.ordinal())), String.valueOf(SwitchPosition.PRIME.ordinal()).concat(uuid.toString()));
        }
        return dto;
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
        String token = (String) tokenService.getTokenUser().getClaims().get("sub");
        familyMember.setCreator(token);
        familyMember.setCreateTime(new Timestamp(System.currentTimeMillis()));
        familyMember.setLastUpdate(new Timestamp(System.currentTimeMillis()));
//        FamilyMemberUtils.selectCheckStatus(familyMember, tokenService.getTokenUser().getRoles());
        familyMember.setCheckStatus(CheckStatus.MODERATE);
        if (familyMemberDto.getFioDtos() == null) familyMember.setOtherNamesExist(false);
        familyMember.setFullName(generateFioStringInfo(familyMember));
        log.info("Первичная информация установлена");
        Changing changing = new Changing();
        changing.setChangingMain(true);
        setUpParents(familyMemberDto, familyMember, "add", changing);

        FamilyMemberInfo familyMemberInfo = new FamilyMemberInfo();
        familyMember.setFamilyMemberInfo(List.of(familyMemberInfo));
        familyMemberInfoService.secretMerge(familyMemberDto.getMemberInfo(), familyMemberInfo);

        familyMemberInfoService.merge(familyMemberDto, familyMemberInfo, "add", true);
        log.info("Forming result and save familyMember.");

        mainStorageRepository.persistMember(familyMember);

        addChangingToBase(familyMemberDto, familyMember, changing, "add");
        if (familyMember.getChilds() != null)
            addChangesInParensInfo(familyMember.getChilds(), familyMember, familyMember.getUuid());

        FamilyMemberDto result = familyMemberMapper.entityToDto(familyMember);
        result.setMemberInfo(familyMemberInfoService.getInfoDto(familyMemberInfo));
//        добавить условие - другие имена
        mainStorageRepository.flushMember();

        directives.add(FamilyDirective.builder()
                .familyMemberDto(result)
                .tokenUser(token)
                .person(result.getUuid().toString())
                .switchPosition(SwitchPosition.MAIN)
                .operation(KafkaOperation.ADD).build());
        if (familyMember.isPrimePhoto())
            directivePhotos.add(new Directive(token, familyMember.getUuid().toString(), SwitchPosition.PRIME, KafkaOperation.ADD));
        if (familyMember.getFamilyMemberInfo().get(0).isPhotoBirthExist())
            directivePhotos.add(new Directive(token, familyMember.getUuid().toString(), SwitchPosition.BIRTH, KafkaOperation.ADD));
        if (familyMember.getFamilyMemberInfo().get(0).isPhotoBurialExist())
            directivePhotos.add(new Directive(token, familyMember.getUuid().toString(), SwitchPosition.BURIAL, KafkaOperation.ADD));
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
    public FamilyMemberDto updateFamilyMember(FamilyMemberDto familyMemberDto) {
        log.info("--------ИЗМЕНЯЕМ ЧЕЛОВЕКА-------");
        Long dtoId = familyMemberDto.getId();
        String token = (String) tokenService.getTokenUser().getClaims().get("sub");
        FamilyMemberDto fromTemp = tempGuardStatus.get(token);
        if (fromTemp.getSecretLevelEdit() == SecretLevel.CLOSE && familyMemberDto.getSecretLevelEdit() != SecretLevel.CLOSE)
            throw new RuntimeException("подделка прав");
        if (fromTemp.getSecretLevelEdit() == SecretLevel.CLOSE) {
            directiveGuardsList.add(DirectiveGuards.builder()
                    .created(new Timestamp(System.currentTimeMillis()))
                    .tokenUser(token)
                    .switchPosition(SwitchPosition.MAIN)
                    .info1("You are havent rights to change this person")
                    .info2(fromTemp.getFullName())
                    .build());
            throw new RightsIsAbsent("У Вас нет прав для изменения");
        }
        FamilyMember fm;
        Changing changing = new Changing();
        if (dtoId != null) {
            fm = mainStorageRepository.findFullFamilyMemberById(dtoId).orElseThrow(() ->
                    new FamilyMemberNotFound("Попытка изменить человека по Id, которого нет в базе"));
        } else if (familyMemberDto.getUuid() != null) {
            fm = mainStorageRepository.findMemberWithInfoByUUID(familyMemberDto.getUuid()).orElseThrow(() ->
                    new FamilyMemberNotFound("Попытка изменить человека по UUID, которого нет в базе"));
        } else throw new ProblemWithId("Ни Id, ни UUID не указан для поиска/изменения человека");


        if (fm.getCheckStatus() == CheckStatus.MODERATE
                && !FamilyMemberUtils.checkRightsToModerate(tokenService.getTokenUser())) {
            directiveGuardsList.add(DirectiveGuards.builder()
                    .created(new Timestamp(System.currentTimeMillis()))
                    .tokenUser(token)
                    .switchPosition(SwitchPosition.MAIN)
                    .info1("trying changing person under voting or moderating")
                    .info2(fm.getFullName())
                    .build());
            throw new ModeratingContent("Находится на модерцаии");
        }
        if (!Objects.equals(fm.getLastUpdate(), lastUpdateMap.get(fm.getId()))) {
            directiveGuardsList.add(DirectiveGuards.builder()
                    .created(new Timestamp(System.currentTimeMillis()))
                    .tokenUser(token)
                    .switchPosition(SwitchPosition.MAIN)
                    .info1("version of person is change between get and edit")
                    .info2(fm.getFullName())
                    .build());
            throw new ModeratingContent("Version change");
        }
//        System.out.println(!Objects.equals(fm.getFirstName(), familyMemberDto.getFirstName()));
//        System.out.println(!Objects.equals(fm.getMiddleName(), familyMemberDto.getMiddleName()));
//        System.out.println(!Objects.equals(fm.getLastName(), familyMemberDto.getLastName()));
//        System.out.println(fm.getSex() != familyMemberDto.getSex() );
//        System.out.println(!Objects.equals(fm.getBirthday(), familyMemberDto.getBirthday().toLocalDate()));

        if (fromTemp.getSecretLevelMainInfo() != SecretLevel.CLOSE && fromTemp.getSecretLevelBirthday() != SecretLevel.CLOSE &&
                (!Objects.equals(fm.getFirstName(), familyMemberDto.getFirstName()) ||
                        !Objects.equals(fm.getMiddleName(), familyMemberDto.getMiddleName()) ||
                        !Objects.equals(fm.getLastName(), familyMemberDto.getLastName()) ||
                        fm.getSex() != familyMemberDto.getSex() ||
                        !Objects.equals(fm.getBirthday().toLocalDate(), familyMemberDto.getBirthday().toLocalDate())
                )) {
            changing.setChangingMain(true);
            Set<FamilyMember> currentChildrenOfFamilyMember = fm.getChilds();
            if (familyMemberDto.getFirstName() != null) fm.setFirstName(familyMemberDto.getFirstName());
            if (familyMemberDto.getBirthday() != null && (currentChildrenOfFamilyMember == null || currentChildrenOfFamilyMember.isEmpty())) {
                fm.setBirthday(familyMemberDto.getBirthday());
                if (fm.getOtherNames() != null && !fm.getOtherNames().isEmpty())
                    oldFioService.changeOldFiosBirthday(fm);
            } else if (familyMemberDto.getBirthday() != null && !Objects.equals(familyMemberDto.getBirthday().toLocalDate(), fm.getBirthday().toLocalDate())) {
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
//            familyMemberDto.setUuid(freshUuid);
            fm.setUuid(freshUuid);
            fm.setFullName(generateFioStringInfo(fm));
        }
        familyMemberDto.setUuid(fm.getUuid());
        if (familyMemberDto.isPrimePhoto()) {
            directivePhotos.add(new Directive(token, fm.getUuid().toString(), SwitchPosition.PRIME, KafkaOperation.ADD));
            fm.setPrimePhoto(true);
        }


        if (fm.getFamilyMemberInfo() != null && !fm.getFamilyMemberInfo().isEmpty())
            familyMemberInfoService.merge(familyMemberDto, fm.getFamilyMemberInfo().get(0), token, changing.isChangingMain());
        else {
            FamilyMemberInfo familyMemberInfo = new FamilyMemberInfo();
            fm.setFamilyMemberInfo(List.of(familyMemberInfoService.merge(familyMemberDto, familyMemberInfo, token, changing.isChangingMain())));
        }
        log.info("Первичная информация установлена");
        familyMemberDto.getMemberInfo().setId(fm.getId());

        if (familyMemberDto.getSecretLevelMainInfo() != SecretLevel.CLOSE) {
            setMainSecurityOption(familyMemberDto, fm);

            familyMemberInfoService.secretMerge(familyMemberDto.getMemberInfo(), fm.getFamilyMemberInfo().get(0));
            setUpParents(familyMemberDto, fm, token, changing);
            addChangingToBase(familyMemberDto, fm, changing, token);
            if (changing.isChangingMain() && fm.getChilds() != null)
                addChangesInParensInfo(fm.getChilds(), fm, familyMemberDto.getUuid());
        }
        fm.setLastUpdate(new Timestamp(System.currentTimeMillis()));

        FamilyMemberDto result = familyMemberMapper.entityToDto(fm);
        result.setMemberInfo(familyMemberInfoService.getSimpleInfoDto(fm.getFamilyMemberInfo().get(0)));
        if (familyMemberDto.getMemberInfo().isPhotoBirthExist())
            directivePhotos.add(new Directive(token, fm.getUuid().toString(), SwitchPosition.BIRTH, KafkaOperation.ADD));
        if (familyMemberDto.getMemberInfo().isPhotoBurialExist())
            directivePhotos.add(new Directive(token, fm.getUuid().toString(), SwitchPosition.BURIAL, KafkaOperation.ADD));
        if (changing.isChangingMain()) {
            if (familyMemberDto.isPrimePhoto())
                directivePhotos.add(new Directive(token, fromTemp.getUuid().toString(), SwitchPosition.PRIME, KafkaOperation.REMOVE));
            else if (fm.isPrimePhoto())
                directivePhotos.add(new Directive(fromTemp.getUuid().toString(), fm.getUuid().toString(), SwitchPosition.PRIME, KafkaOperation.RENAME));
            if (familyMemberDto.getMemberInfo().isPhotoBirthExist())
                directivePhotos.add(new Directive(token, fromTemp.getUuid().toString(), SwitchPosition.BIRTH, KafkaOperation.REMOVE));
            else if (fm.getFamilyMemberInfo().get(0).isPhotoBirthExist())
                directivePhotos.add(new Directive(fromTemp.getUuid().toString(), fm.getUuid().toString(), SwitchPosition.BIRTH, KafkaOperation.RENAME));
            if (familyMemberDto.getMemberInfo().isPhotoBurialExist())
                directivePhotos.add(new Directive(token, fromTemp.getUuid().toString(), SwitchPosition.BURIAL, KafkaOperation.REMOVE));
            else if (fm.getFamilyMemberInfo().get(0).isPhotoBurialExist())
                directivePhotos.add(new Directive(fromTemp.getUuid().toString(), fm.getUuid().toString(), SwitchPosition.BURIAL, KafkaOperation.RENAME));
        }
// Если нужны старые имена и прозвища в модуле family
        //        result.setFioDtos(oldFioService.getOldNamesMapper().oldFiosSetToFioDtoSet(fm.getOtherNames()));
        directives.add(FamilyDirective.builder()
                .familyMemberDto(result)
                .tokenUser((String) tokenService.getTokenUser().getClaims().get("sub"))
                .person(fromTemp.getUuid().toString())
                .switchPosition(SwitchPosition.MAIN)
                .operation(KafkaOperation.RENAME).build());
        fm.setCheckStatus(CheckStatus.MODERATE);
        mainStorageRepository.flushMember();
        tempGuardStatus.remove(token);
        tempExtendedDto.remove(token);
        tempMainContact.remove(token);
        lastUpdateMap.put(fm.getId(), fm.getLastUpdate());
        return result;
    }

    public void setMainSecurityOption(FamilyMemberDto familyMemberDto, FamilyMember fm) {
        if (fm.getSecretLevelEdit() != SecretLevel.CLOSE && fm.getSecretLevelMainInfo() != SecretLevel.CLOSE) {
            if (familyMemberDto.getSecretLevelEdit() != null)
                fm.setSecretLevelEdit(familyMemberDto.getSecretLevelEdit());
            if (familyMemberDto.getSecretLevelPhoto() != null && familyMemberDto.getSecretLevelPhoto() != SecretLevel.CLOSE)
                fm.setSecretLevelPhoto(familyMemberDto.getSecretLevelPhoto());
            if (familyMemberDto.getSecretLevelMainInfo() != null)
                fm.setSecretLevelMainInfo(familyMemberDto.getSecretLevelMainInfo());
            if (familyMemberDto.getSecretLevelRemove() != null && familyMemberDto.getSecretLevelRemove() != SecretLevel.CLOSE)
                fm.setSecretLevelRemove(familyMemberDto.getSecretLevelRemove());
            if (familyMemberDto.getSecretLevelBirthday() != null && familyMemberDto.getSecretLevelBirthday() != SecretLevel.CLOSE)
                fm.setSecretLevelBirthday(familyMemberDto.getSecretLevelBirthday());
        }
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
                child.setLastUpdate(new Timestamp(System.currentTimeMillis()));
                familyMemberRepo.save(child);
                sendChildDirective(child, uuid);
            }
        } else for (FamilyMember child : setOfChilds) {
            child.setMotherInfo(fm.getFullName());
            child.setMother(fm);
            child.setLastUpdate(new Timestamp(System.currentTimeMillis()));
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
    public void setUpParents(FamilyMemberDto familyMemberDto, FamilyMember fm, String token, Changing changing) {

        if (familyMemberDto.getFatherFio() != null && familyMemberDto.getMotherFio() != null
                && Objects.equals(familyMemberDto.getFatherFio().getFirstName(), familyMemberDto.getMotherFio().getFirstName())
                && Objects.equals(familyMemberDto.getFatherFio().getMiddleName(), familyMemberDto.getMotherFio().getMiddleName())
                && Objects.equals(familyMemberDto.getFatherFio().getLastName(), familyMemberDto.getMotherFio().getLastName())
        ) throw new UncorrectedInformation("It's not funny. Mother and Father must be different people");

        if (familyMemberDto.getFatherFio() != null && (Objects.equals(token, "add") ||
                familyMemberDto.getFatherFio().getFirstName() == null ||
                familyMemberDto.getFatherFio().getMiddleName() == null ||
                familyMemberDto.getFatherFio().getLastName() == null ||
                familyMemberDto.getFatherFio().getBirthday() == null || fm.getFatherInfo() == null ||
                (tempGuardStatus.get(token) != null && fm.getFatherInfo().charAt(0) == '(' &&
                        !CheckStatus.ABSENT.getComment().concat(generateFioStringInfo(fioMapper.dtoToEntity(familyMemberDto.getFatherFio()))).equals(tempGuardStatus.get(token).getFatherInfo())))) {
            losingParentsService.setUpFather(familyMemberDto.getFatherFio(), fm);
            changing.setChangingFather(true);
        }
        if (familyMemberDto.getMotherFio() != null && (Objects.equals(token, "add") ||
                familyMemberDto.getMotherFio().getFirstName() == null ||
                familyMemberDto.getMotherFio().getMiddleName() == null ||
                familyMemberDto.getMotherFio().getLastName() == null ||
                familyMemberDto.getMotherFio().getBirthday() == null || fm.getMotherInfo() == null ||
                (tempGuardStatus.get(token) != null && fm.getMotherInfo().charAt(0) == '(' &&
                        !CheckStatus.ABSENT.getComment().concat(generateFioStringInfo(fioMapper.dtoToEntity(familyMemberDto.getMotherFio()))).equals(tempGuardStatus.get(token).getMotherInfo())))) {
            losingParentsService.setUpMother(familyMemberDto.getMotherFio(), fm);
            changing.setChangingMother(true);
        }

        log.info("Родители установлены");
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
    public void addChangingToBase(FamilyMemberDto familyMemberDto, FamilyMember familyMember, Changing changing, String token) {
        Set<UUID> fromDto = new HashSet<>();
        if (familyMemberDto.getFioDtos() != null) {
            for (FioDto old :
                    familyMemberDto.getFioDtos()) {
                old.setBirthday(familyMemberDto.getBirthday());
                fromDto.add(generateUUIDFromFio(fioMapper.dtoToEntity(old)));
            }
            if (tempExtendedDto.get(token) != null && tempExtendedDto.get(token).getFioDtos() != null)
                for (FioDto tempExtended :
                        tempExtendedDto.get(token).getFioDtos()) {
                    fromDto.remove(tempExtended.getUuid());
                }
        }
        if (changing.isChangingMain() || !fromDto.isEmpty()) {
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
        }
        if (changing.isChangingFather() && familyMember.getFatherInfo() != null &&
                familyMember.getFatherInfo().charAt(1) == 'A') {
            losingParentsService.addLosingParent(familyMemberDto.getFatherFio(), familyMember, Sex.MALE);
        }
        if (changing.isChangingMother() && familyMember.getMotherInfo() != null &&
                familyMember.getMotherInfo().charAt(1) == 'A') {
            losingParentsService.addLosingParent(familyMemberDto.getMotherFio(), familyMember, Sex.FEMALE);
        }
    }

    @Transactional
    public void changeParentsAfterVoting(DirectiveGuards directive) {
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
        familyMember.setLastUpdate(new Timestamp(System.currentTimeMillis()));
        familyMemberRepo.save(familyMember);
    }

    @Transactional
    public void changeCheckStatus(DirectiveGuards directive) {
        if (directive.getGuards() != null && !directive.getGuards().isEmpty()) {
            Set<FamilyMember> members = familyMemberRepo.findAllByUuidIn(directive.getGuards().stream().map(UUID::fromString).collect(Collectors.toSet()));
            for (FamilyMember fm :
                    members) {
                fm.setCheckStatus(directive.getCheckStatus());
                if (directive.getCheckStatus()==CheckStatus.CHECKED) fm.setCreator(null);
                fm.setLastUpdate(new Timestamp(System.currentTimeMillis()));

            }
            familyMemberRepo.saveAll(members);
        }
        if (directive.getSwitchPosition() != null || directive.getGuards() == null) {
            FamilyMember familyMember = familyMemberRepo.findFioByUuid(UUID.fromString(directive.getPerson())).orElseThrow(() -> new RuntimeException("family member not found"));
            if (directive.getSwitchPosition() == null) {
                switch (directive.getCheckStatus()) {
                    case MODERATE -> familyMember.setCheckStatus(CheckStatus.MODERATE);
                    case LINKED -> {
                        familyMember.setCheckStatus(CheckStatus.LINKED);
                        if (directive.getTokenUser() != null) {familyMember.setCreator(directive.getTokenUser());
                        clearTempGuardMaps(directive.getTokenUser());}
                    }
                    case CHECKED -> {
                        familyMember.setCheckStatus(CheckStatus.CHECKED);
                        familyMember.setCreator(null);
                        if (directive.getTokenUser() != null) clearTempGuardMaps(directive.getTokenUser());

                    }
                    case UNCHECKED -> familyMember.setCheckStatus(CheckStatus.UNCHECKED);
                    default -> log.warn("found unknown directive");
                }
            } else switch (directive.getSwitchPosition()) {
                case MAIN -> familyMember.setCheckStatus(CheckStatus.MODERATE);
                case FATHER -> {
                    familyMember.setCheckStatus(CheckStatus.LINKED);
                    if (directive.getTokenUser() != null) {familyMember.setCreator(directive.getTokenUser());
                    clearTempGuardMaps(directive.getTokenUser());}
                }
                case MOTHER -> {
                    familyMember.setCheckStatus(CheckStatus.CHECKED);
                    familyMember.setCreator(null);
                    if (directive.getTokenUser() != null) clearTempGuardMaps(directive.getTokenUser());
                }
                case CHILD -> familyMember.setCheckStatus(CheckStatus.UNCHECKED);
                default -> log.warn("found unknown directive");
            }
            familyMember.setLastUpdate(new Timestamp(System.currentTimeMillis()));
            familyMemberRepo.save(familyMember);
        }

    }

    void clearTempGuardMaps(String token) {
        tempGuardStatus.remove(token);
        tempPhotoAccept.remove(token.concat(String.valueOf(SwitchPosition.PRIME.ordinal())));
        tempPhotoAccept.remove(token.concat(String.valueOf(SwitchPosition.BIRTH.ordinal())));
        tempPhotoAccept.remove(token.concat(String.valueOf(SwitchPosition.BURIAL.ordinal())));
    }
}

