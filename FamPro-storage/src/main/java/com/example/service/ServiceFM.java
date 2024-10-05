package com.example.service;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.FioDto;
import com.example.entity.*;
import com.example.enums.Sex;
import com.example.exceptions.ProblemWithId;
import com.example.exceptions.FamilyMemberNotFound;
import com.example.exceptions.UncorrectedInformation;
import com.example.exceptions.UncorrectedOrNewInformation;
import com.example.mappers.*;
import com.example.repository.FamilyMemberRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ServiceFM {
    private final FamilyMemberMapper familyMemberMapper;
    private final FamilyMemberRepo familyMemberRepo;
    private final FamilyMemberInfoService familyMemberInfoService;

    @Transactional(readOnly = true)
    public FamilyMemberDto getFamilyMember(Long id) {

        FamilyMember familyMember = familyMemberRepo.findById(id)
                .orElseThrow(() -> new FamilyMemberNotFound("Человек с ID: ".concat(String.valueOf(id)).concat(" не найден")));
        FamilyMemberDto familyMemberDto = familyMemberMapper.entityToDto(familyMember);
        if (familyMember.getFamilyMemberInfo() != null) {
            familyMemberDto.setMemberInfo(familyMemberInfoService.getMemberInfo(familyMember));
        }
        return familyMemberDto;
    }

    @Transactional
    public FamilyMemberDto addFamilyMember(FamilyMemberDto familyMemberDto) {
        log.info("--------ВНОСИМ НОВОГО ЧЕЛОВЕКА-------");
        if (familyMemberDto.getId() != null) throw new ProblemWithId("Удалите ID нового человека");

        FamilyMember familyMember = familyMemberMapper.dtoToEntity(familyMemberDto);
        familyMemberDto.setUuid(generateUUID(familyMember));
        familyMember.setUuid(familyMemberDto.getUuid());
        Optional<FamilyMember> fm = familyMemberRepo.findFamilyMemberByUuid(familyMemberDto.getUuid());
        if (fm.isPresent()) {
            throw new ProblemWithId("Такой человек уже есть в базе. Если Вы хотите его отредактировать - воспользуйтесь Patch-методом. ID человека: ".concat(String.valueOf(fm.get().getId())));
        }
        log.info("Первичная информация установлена");
        extractExtensionOfFamilyMember(familyMemberDto, familyMember);
        familyMemberRepo.save(familyMember);
        String familyMemberStringInfo = generateFamilyMemberStringInfo(familyMember);
        Set<FamilyMember> childrenOfFamilyMember = checkForAdditionalChilds(familyMemberStringInfo
                .concat(" (Absent in base)"), familyMember.getSex());
        if (childrenOfFamilyMember != null && !childrenOfFamilyMember.isEmpty()) {
            addChangesInParensInfo(childrenOfFamilyMember, familyMemberStringInfo, familyMember);
            familyMember.setChilds(childrenOfFamilyMember);
        }
        return familyMemberMapper.entityToDto(familyMemberRepo.save(familyMember));
    }

    @Transactional(readOnly = true)
    public Collection<FamilyMemberDto> getAllFamilyMembers() {
        List<FamilyMember> familyMemberList = familyMemberRepo.findAll();
        List<FamilyMemberDto> familyMemberDtoList = new ArrayList<>();
        for (FamilyMember familyMember : familyMemberList) {
            familyMemberDtoList.add(familyMemberMapper.entityToDto(familyMember));
        }
        log.info("Коллекия всех людей из базы выдана");
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
            fm = familyMemberRepo.findFamilyMemberByUuid(familyMemberDto.getUuid()).orElseThrow(() ->
                    new FamilyMemberNotFound("Попытка изменить человека по UUID, которого нет в базе"));
        } else throw new ProblemWithId("Ни Id, ни UUID не указан для поиска/изменения человека");
        Set<FamilyMember> childrenOfFamilyMember = fm.getChilds();
        if (familyMemberDto.getFirstName() != null) fm.setFirstName(familyMemberDto.getFirstName());
        if (familyMemberDto.getBirthday() != null) fm.setBirthday(familyMemberDto.getBirthday());
        if (familyMemberDto.getLastName() != null) fm.setLastName(familyMemberDto.getLastName());
        if (familyMemberDto.getMiddleName() != null) fm.setMiddleName(familyMemberDto.getMiddleName());
        if (familyMemberDto.getSex() != null && (childrenOfFamilyMember == null || childrenOfFamilyMember.isEmpty()))
            fm.setSex(familyMemberDto.getSex());
        else if (familyMemberDto.getSex() != null && familyMemberDto.getSex() != fm.getSex()) {
            throw new UncorrectedInformation("Изменить пол человека, у которого в базе имеются подтвержденные дети, невозможно");
        }
        familyMemberDto.setUuid(generateUUID(fm));
        log.info("Первичная информация установлена");
        extractExtensionOfFamilyMember(familyMemberDto, fm);
        fm.setUuid(familyMemberDto.getUuid());
        String familyMemberStringInfo = generateFamilyMemberStringInfo(fm);
        Set<FamilyMember> additionalChilds = checkForAdditionalChilds(familyMemberStringInfo
                .concat(" (Absent in base)"), fm.getSex());
        familyMemberRepo.save(fm);
        if (additionalChilds != null && !additionalChilds.isEmpty()) {
            if (childrenOfFamilyMember != null) {
                childrenOfFamilyMember.addAll(additionalChilds);
                fm.setChilds(childrenOfFamilyMember);
            } else fm.setChilds(additionalChilds);
            addChangesInParensInfo(fm.getChilds(), familyMemberStringInfo, fm);
        }
        familyMemberRepo.save(fm);
        return familyMemberMapper.entityToDto(fm);
    }
// потом надо это изменить через таблиу потерянных родителей
    public Set<FamilyMember> checkForAdditionalChilds(String infoParent, Sex sex) {
        Set<FamilyMember> possibleChildrenOfFamilyMember;
        if (sex == Sex.MALE)
            possibleChildrenOfFamilyMember = familyMemberRepo.findAllByFatherInfo(infoParent);
        else
            possibleChildrenOfFamilyMember = familyMemberRepo.findAllByMotherInfo(infoParent);
        return possibleChildrenOfFamilyMember;
    }

    public void addChangesInParensInfo(Set<FamilyMember> setOfChilds, String infoParents,FamilyMember fm) {

        if (fm.getSex() == Sex.MALE) {
            for (FamilyMember child : setOfChilds) {child.setFatherInfo(infoParents); child.setFather(fm);familyMemberRepo.save(child);}
        } else for (FamilyMember child : setOfChilds) {child.setMotherInfo(infoParents); child.setMother(fm);familyMemberRepo.save(child);}
    }

    public FamilyMember generateFamilyMemberFromFio(FioDto fioDto) {
        FamilyMember familyMember = new FamilyMember();
        familyMember.setFirstName(fioDto.getFirstName());
        familyMember.setMiddleName(fioDto.getMiddleName());
        familyMember.setLastName(fioDto.getLastName());
        familyMember.setBirthday((fioDto.getBirthday()));
        return familyMember;
    }

    private UUID generateUUID(FamilyMember familyMember) {
        String str = familyMember.getFirstName()
                .concat(familyMember.getMiddleName())
                .concat(familyMember.getLastName())
                .concat(String.valueOf(familyMember.getBirthday().toLocalDate())).toLowerCase()
                .concat("Rainbow");
        log.info("новый UUID человека сгенерирован");
        return UUID.nameUUIDFromBytes(str.getBytes());
    }

    public String generateFamilyMemberStringInfo(FamilyMember familyMember) {
        return String.join(" ", familyMember.getFirstName(), familyMember.getMiddleName(), familyMember.getLastName(), ". Birthday: ", (familyMember.getBirthday()!=null)?String.valueOf(familyMember.getBirthday().toLocalDate()):null);
    }

    private void extractExtensionOfFamilyMember(FamilyMemberDto familyMemberDto, FamilyMember fm) {
        if (familyMemberDto.getFatherFio() != null) {
            try {
                FamilyMember father = getParentOfFamilyMember(familyMemberDto.getFatherFio());
                fm.setFather(father);
                fm.setFatherInfo(generateFamilyMemberStringInfo(father));
            } catch (FamilyMemberNotFound e) {
                log.warn(e.getMessage());
            } catch (UncorrectedOrNewInformation e) {
                fm.setFatherInfo(e.getMessage());
            } catch (UncorrectedInformation e) {
                fm.setFatherInfo(generateFamilyMemberStringInfo
                        (generateFamilyMemberFromFio
                                (familyMemberDto.getFatherFio())).concat(" (Info not fully)"));
            }
            log.info("Информация об отце установлена");
        }
        if (familyMemberDto.getMotherFio() != null) {
            try {
                FamilyMember mother = getParentOfFamilyMember(familyMemberDto.getMotherFio());
                fm.setMother(mother);
                fm.setMotherInfo(generateFamilyMemberStringInfo(mother));
            } catch (FamilyMemberNotFound e) {
                log.warn(e.getMessage());
            } catch (UncorrectedOrNewInformation e) {
                fm.setMotherInfo(e.getMessage());
            } catch (UncorrectedInformation e) {
                fm.setMotherInfo(generateFamilyMemberStringInfo
                        (generateFamilyMemberFromFio
                                (familyMemberDto.getMotherFio())).concat(" (Info not fully)"));
            }
            log.info("Информация о матери установлена");
        }
        if (familyMemberDto.getMemberInfo() != null) {
            familyMemberDto.getMemberInfo().setId(null);
            fm.setFamilyMemberInfo(familyMemberInfoService.merge(familyMemberDto, fm.getUuid()));
        }
        log.info("Расширенная информация проверена и установлена");
    }

    private FamilyMember getParentOfFamilyMember(FioDto parentDto) {
        if (parentDto.getId() != null)
            return familyMemberRepo.findById(parentDto.getId()).orElseThrow(() -> new FamilyMemberNotFound("Parent not found by Id"));
        if (parentDto.getUuid() != null)
            return familyMemberRepo.findFamilyMemberByUuid(parentDto.getUuid()).orElseThrow(() -> new FamilyMemberNotFound("Parent not found by UUID"));
        if (parentDto.getFirstName() != null
                && parentDto.getMiddleName() != null
                && parentDto.getLastName() != null
                && parentDto.getBirthday() != null) {
            FamilyMember familyMember = generateFamilyMemberFromFio(parentDto);
            parentDto.setUuid(generateUUID(familyMember));
            return familyMemberRepo.findFamilyMemberByUuid(parentDto.getUuid()).orElseThrow(() ->
                    new UncorrectedOrNewInformation(generateFamilyMemberStringInfo(familyMember).concat(" (Absent in base)")));
        } else
            throw new UncorrectedInformation("Информация о родителе не полны. Данные в дальнейшем будут игнорированы");
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
}

