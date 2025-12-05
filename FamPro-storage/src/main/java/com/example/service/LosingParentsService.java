package com.example.service;

import com.example.dtos.FamilyDirective;
import com.example.dtos.FioDto;
import com.example.entity.*;
import com.example.enums.*;
import com.example.exceptions.*;
import com.example.mappers.FamilyMemberMapper;
import com.example.mappers.FioMapper;
import com.example.mappers.LosingParensMapper;
import com.example.repository.FamilyMemberRepo;
import com.example.repository.LosingParentsRepo;
import com.example.repository.LosingParentsRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Getter
@Setter
public class LosingParentsService extends FioServiceImp<LosingParent> {
    private final LosingParentsRepo losingParentsRepo;
    private final LosingParensMapper losingParensMapper;
    private final FamilyMemberRepo familyMemberRepo;
    private final OldFioService oldFioService;
    private final LinkedList<FamilyDirective> directives;
    private final TokenService tokenService;
    private final FamilyMemberMapper familyMemberMapper;
    private final FormAndSendService formAndSendService;
    private final LosingParentsRepository losingParentsRepository;

    public LosingParentsService(FioMapper fioMapper, LosingParentsRepo losingParentsRepo, LosingParensMapper losingParensMapper, FamilyMemberRepo familyMemberRepo, OldFioService oldFioService, LinkedList<FamilyDirective> directives, TokenService tokenService, FamilyMemberMapper familyMemberMapper, FormAndSendService formAndSendService, LosingParentsRepository losingParentsRepository) {
        super(fioMapper);
        this.losingParentsRepo = losingParentsRepo;
        this.losingParensMapper = losingParensMapper;
        this.familyMemberRepo = familyMemberRepo;
        this.oldFioService = oldFioService;
        this.directives = directives;
        this.tokenService = tokenService;
        this.familyMemberMapper = familyMemberMapper;
        this.formAndSendService = formAndSendService;
        this.losingParentsRepository = losingParentsRepository;
    }

    @Transactional
    public void addLosingParent(FioDto fioDto, FamilyMember familyMember, Sex sex) {
        LosingParent losingParent = losingParensMapper.fioDtoToLosingParent(fioDto);
        UUID uuid = generateUUIDFromFio(losingParent);
        if (losingParentsRepo.findFioByUuidAndMember(uuid, familyMember).isEmpty()) {
            if (sex == Sex.MALE) losingParent.setLosingUUID(generateUUIDFromFullName(familyMember.getFatherInfo()));
            else losingParent.setLosingUUID(generateUUIDFromFullName(familyMember.getMotherInfo()));
            losingParent.setMember(familyMember);
            losingParent.setUuid(uuid);
            losingParent.setSex(sex);
            losingParent.setFullName(generateFioStringInfo(losingParent));
            losingParentsRepo.save(losingParent);
        }
    }

    @Transactional
    public void removeParentByLosingUuid(UUID uuid, FamilyMember fm) {
        losingParentsRepo.deleteByLosingUUIDAndMember(uuid, fm);
    }

    @Transactional
    public Set<FamilyMember> findAdditionalChildren(Changing changing, FamilyMember familyMember, Set<OldFio> oldFios) {
        Set<FamilyMember> possibleChildrenOfFamilyMember = new HashSet<>();
        Set<UUID> uuids=new HashSet<>();
        if (changing.isChangingMain()) uuids.add(familyMember.getUuid());;
        if (oldFios!=null&&!oldFios.isEmpty()) uuids.addAll(oldFios.stream().map(OldFio::getUuid).collect(Collectors.toSet()));
        List<LosingParent> possibleLosingParent = losingParentsRepository.findLosingParentsWithFamilyMember(uuids);
        if (possibleLosingParent != null && !possibleLosingParent.isEmpty()) {
            boolean correctLosingSex = true;
            if (possibleLosingParent.size() != 1) {
                Sex choose = possibleLosingParent.get(0).getSex();
                for (LosingParent losingParentForFamilyMember : possibleLosingParent) {
                    correctLosingSex = choose == losingParentForFamilyMember.getSex();
                    if (!correctLosingSex) break;
                }
            }
            for (LosingParent losingParentForFamilyMember : possibleLosingParent) {
                if (losingParentForFamilyMember.getMember().getCheckStatus() == CheckStatus.MODERATE)
                    throw new ModeratingContent(losingParentForFamilyMember.getMember().getFullName());
                if (losingParentForFamilyMember.getSex() != familyMember.getSex() && correctLosingSex)
                    familyMember.setSex(losingParentForFamilyMember.getSex());
                else if (!correctLosingSex)
                    throw new UncorrectedInformationSex(losingParentForFamilyMember.getMember().getFullName());
                possibleChildrenOfFamilyMember.add(losingParentForFamilyMember.getMember());
            }
            losingParentsRepository.removeAllByUuids(possibleLosingParent.stream().map(LosingParent::getUuid).collect(Collectors.toSet()));
        }
        return possibleChildrenOfFamilyMember;
    }

    @Transactional
    public Set<FamilyMember> checkForAdditionalChilds(UUID uuid, FamilyMember familyMember) {
        Set<FamilyMember> possibleChildrenOfFamilyMember = new HashSet<>();
        List<LosingParent> possibleLosingParent = losingParentsRepo.findAllByUuid(uuid);

        if (possibleLosingParent != null && !possibleLosingParent.isEmpty()) {
            boolean correctLosingSex = true;
            if (possibleLosingParent.size() != 1) {
                Sex choose = possibleLosingParent.get(0).getSex();
                for (LosingParent losingParentForFamilyMember : possibleLosingParent) {
                    correctLosingSex = choose == losingParentForFamilyMember.getSex();
                    if (!correctLosingSex) break;
                }
            }
            for (LosingParent losingParentForFamilyMember : possibleLosingParent) {
//тут возможны доп проверки на соответствие и право
                FamilyMember childWithLosingParent = losingParentForFamilyMember.getMember();
// если поменяли пол, а у найденных детей иное мнение
                if (losingParentForFamilyMember.getSex() != familyMember.getSex() && correctLosingSex)
                    familyMember.setSex(losingParentForFamilyMember.getSex());
// тут можно побаловаться с сохраняемым инфо (на данный моммент остается информация старой записи)
//                if (losingParentForFamilyMember.getSex() == Sex.MALE && familyMember.getSex() == Sex.MALE) {
//                    childWithLosingParent.setFather(familyMember);
//                    childWithLosingParent.setFatherInfo(losingParentForFamilyMember.getFullName());
//                } else if (losingParentForFamilyMember.getSex() == Sex.FEMALE && familyMember.getSex() == Sex.FEMALE){
//                    childWithLosingParent.setMother(familyMember);
//                    childWithLosingParent.setMotherInfo(losingParentForFamilyMember.getFullName());
//                }
                possibleChildrenOfFamilyMember.add(childWithLosingParent);
            }
            losingParentsRepo.deleteAll(possibleLosingParent);
        }
        return possibleChildrenOfFamilyMember;
    }

    @Transactional
    public void setUpFather(FioDto fioDto, FamilyMember fm, List<Notification> notifications, List<FamilyDirective> listToFamily) {
        if (fm.getFatherInfo() != null && fm.getFatherInfo().charAt(1) == 'A')
            removeParentByLosingUuid(generateUUIDFromFullName(fm.getFatherInfo()), fm);
        try {
            if (fioDto.getBirthday() != null && !checkDifBirthday(Sex.MALE, fioDto.getBirthday(), fm.getBirthday()))
                throw new UncorrectedInformationDate(CheckStatus.UNCORRECTED.getComment());
            FamilyMember father = getParentOfFamilyMember(fioDto);
            if (father.getCheckStatus() == CheckStatus.MODERATE)
                throw new ModeratingContent(father.getFullName());

            if (father.getSex() == Sex.FEMALE)
                throw new UncorrectedInformationSex("(Sex of Parent is wrong)");

            if (checkDifBirthday(Sex.MALE, father.getBirthday(), fm.getBirthday())) {
                fm.setFather(father);
                fm.setFatherInfo(father.getFullName());

                if (father.getChilds() == null) father.setChilds(new HashSet<>());

                father.getChilds().add(fm);
                listToFamily.add(FamilyDirective.builder()
                        .familyMemberDto(familyMemberMapper.entityToDto(father))
                        .person(String.valueOf(fm.getUuid()))
                        .switchPosition(SwitchPosition.FATHER)
                        .operation(KafkaOperation.EDIT).build());
            } else
                throw new UncorrectedInformationDate(CheckStatus.UNCORRECTED.getComment());

        } catch (FamilyMemberNotFound e) {
            log.warn(e.getMessage());
        } catch (UncorrectedOrNewInformation e) {
            fm.setFatherInfo(e.getMessage().concat(generateFioStringInfo(fioMapper.dtoToEntity(fioDto))));
        } catch (UncorrectedInformation e) {
            fm.setFatherInfo(e.getMessage());
            fm.setFather(null);
        } catch (UncorrectedInformationDate e) {
            notifications.add(Notification.builder().attention(Attention.NEGATIVE).subject(Subject.WRONG_INFO_DATE).build());
            fm.setFatherInfo(e.getMessage());
            fm.setFather(null);
        } catch (UncorrectedInformationSex e) {
            notifications.add(Notification.builder().attention(Attention.NEGATIVE).subject(Subject.WRONG_INFO_SEX).build());
            fm.setFatherInfo(e.getMessage());
            fm.setFather(null);
        } catch (ModeratingContent e) {
            notifications.add(Notification.builder().attention(Attention.NEGATIVE).person(e.getMessage()).subject(Subject.MODERATION_FATHER).build());
            fm.setFatherInfo(null);
            fm.setFather(null);
        }
        log.info("Информация об отце установлена");
    }

    @Transactional
    public void setUpMother(FioDto fioDto, FamilyMember fm, List<Notification> notifications, List<FamilyDirective> listToFamily) {
        if (fm.getMotherInfo() != null && fm.getMotherInfo().charAt(1) == 'A')
            removeParentByLosingUuid(generateUUIDFromFullName(fm.getMotherInfo()), fm);
        try {
            if (fioDto.getBirthday() != null && !checkDifBirthday(Sex.FEMALE, fioDto.getBirthday(), fm.getBirthday()))
                throw new UncorrectedInformationDate(CheckStatus.UNCORRECTED.getComment());
            FamilyMember mother = getParentOfFamilyMember(fioDto);
            if (mother.getCheckStatus() == CheckStatus.MODERATE)
                throw new ModeratingContent(mother.getFullName());

            if (mother.getSex() == Sex.MALE)
                throw new UncorrectedInformationSex("(Sex of Parent is wrong)");

            if (checkDifBirthday(Sex.FEMALE, mother.getBirthday(), fm.getBirthday())) {
                fm.setMother(mother);
                fm.setMotherInfo(mother.getFullName());
                if (mother.getChilds() == null) mother.setChilds(new HashSet<>());
                mother.getChilds().add(fm);
                listToFamily.add(FamilyDirective.builder()
                        .familyMemberDto(familyMemberMapper.entityToDto(mother))
                        .person(String.valueOf(fm.getUuid()))
                        .switchPosition(SwitchPosition.MOTHER)
                        .operation(KafkaOperation.EDIT).build());
            } else
                throw new UncorrectedInformationDate(CheckStatus.UNCORRECTED.getComment());

        } catch (FamilyMemberNotFound e) {
            log.warn(e.getMessage());
        } catch (UncorrectedOrNewInformation e) {
            fm.setMotherInfo(e.getMessage().concat(generateFioStringInfo(fioMapper.dtoToEntity(fioDto))));
        } catch (UncorrectedInformation e) {
            fm.setMotherInfo(e.getMessage());
            fm.setMother(null);
        } catch (UncorrectedInformationDate e) {
            notifications.add(Notification.builder().attention(Attention.NEGATIVE).subject(Subject.WRONG_INFO_DATE).build());
            fm.setMotherInfo(e.getMessage());
            fm.setMother(null);
        } catch (UncorrectedInformationSex e) {
            notifications.add(Notification.builder().attention(Attention.NEGATIVE).subject(Subject.WRONG_INFO_SEX).build());
            fm.setMotherInfo(e.getMessage());
            fm.setMother(null);
        } catch (ModeratingContent e) {
            notifications.add(Notification.builder().attention(Attention.NEGATIVE).person(e.getMessage()).subject(Subject.MODERATION_MOTHER).build());
            fm.setMotherInfo(null);
            fm.setMother(null);
        }
        log.info("Информация о матери установлена");
    }

    @Transactional
    public FamilyMember getParentOfFamilyMember(FioDto fio) {
        if (fio.getId() != null)
            return familyMemberRepo.findById(fio.getId()).orElseThrow(() -> new FamilyMemberNotFound("Parent not found by Id"));
        if (fio.getUuid() != null)
            return familyMemberRepo.findFioByUuid(fio.getUuid()).orElseThrow(() -> new FamilyMemberNotFound("Parent not found by UUID"));
        if (fio.getFirstName() != null
                && fio.getMiddleName() != null
                && fio.getLastName() != null
                && fio.getBirthday() != null) {
            UUID uuid = generateUUIDFromFio(fioMapper.dtoToEntity(fio));
            Optional<FamilyMember> findFM = familyMemberRepo.findFioByUuid(uuid);
            if (findFM.isPresent()) return findFM.get();
            Optional<OldFio> findFio = oldFioService.getFioRepo().findFioByUuid(uuid);
            if (findFio.isPresent()) {
                return findFio.get().getMember();
            } else
                throw new UncorrectedOrNewInformation(CheckStatus.ABSENT.getComment());
        } else
            throw new UncorrectedOrNewInformation(CheckStatus.NOT_FULLY.getComment());
    }
}

