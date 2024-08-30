package com.example.service;

import com.example.dtos.FamilyMemberDto;
import com.example.entity.*;
import com.example.enums.Sex;
import com.example.exceptions.ProblemWithId;
import com.example.exceptions.FamilyMemberNotFound;
import com.example.mappers.*;
import com.example.repository.AddressRepo;
import com.example.repository.FamilyRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ServiceFM {
    private final FamilyMemberMapper familyMemberMapper;
    private final FamilyMemberInfoMapper familyMemberInfoMapper;
    private final FamilyRepo familyRepo;
    private final EmailService emailService;
    private final AddressRepo addressRepo;
    private final PhoneService phoneService;

    public FamilyMemberDto getFamilyMember(Long id) {

        Optional<FamilyMember> familyMember = familyRepo.findById(id);
        FamilyMemberDto familyMemberDto = familyMemberMapper.entityToDto(familyMember.orElseThrow(() -> new FamilyMemberNotFound("Человек с ID: ".concat(String.valueOf(id)).concat(" не найден"))));
        if ((familyMember.get().getFather() != null)) {
            familyMemberDto.setFatherId(familyMember.get().getFather().getId());
            log.info("Отец установлен");
        }

        if ((familyMember.get().getMother() != null)) {
            familyMemberDto.setMotherId(familyMember.get().getMother().getId());
            log.info("Мать установлена");
        }
        return familyMemberDto;
    }

    public FamilyMemberDto addFamilyMember(FamilyMemberDto familyMemberDto) {
        FamilyMember familyMember = familyMemberMapper.dtoToEntity(familyMemberDto);
        String familiya = familyMember.getLastname();
        List<FamilyMember> familyMemberList = familyRepo.findAllByLastname(familiya);
        for (FamilyMember fm : familyMemberList) {
            if (familyMember.getFirstname().equals(fm.getFirstname()) && familyMember.getMiddlename().equals(fm.getMiddlename()) && familyMember.getBirthday().toLocalDate().equals(fm.getBirthday().toLocalDate())) {
                throw new ProblemWithId("Такой человек уже есть в базе. Если Вы хотите его отредактировать - воспользуйтесь Patch-методом. ID человека: ".concat(String.valueOf(fm.getId())));
            }
        }
        log.info("Первичная информация установлена");
        extractExtensionOfFamilyMember(familyMemberDto, familyMember);
        return familyMemberMapper.entityToDto(familyRepo.save(familyMember));
    }

    public Collection<FamilyMemberDto> getAllFamilyMembers() {
        List<FamilyMember> familyMemberList = familyRepo.findAll();
        List<FamilyMemberDto> familyMemberDtoList = new ArrayList<>();
        for (FamilyMember familyMember : familyMemberList) {
            FamilyMemberDto familyMemberDto = familyMemberMapper.entityToDto(familyMember);
            if (familyMember.getFather() != null) familyMemberDto.setFatherId(familyMember.getFather().getId());
            if (familyMember.getMother() != null) familyMemberDto.setMotherId(familyMember.getMother().getId());
            familyMemberDtoList.add(familyMemberDto);
        }
        log.info("Коллекия всех людей из базы выдана");
        return familyMemberDtoList;
    }

    public FamilyMemberDto updateFamilyMember(FamilyMemberDto familyMemberDto) {
        Long dtoId = familyMemberDto.getId();
        if (dtoId == null) throw new ProblemWithId("Id не указан");
        Optional<FamilyMember> familyMember = familyRepo.findById(dtoId);
        FamilyMember fm = familyMember.orElseThrow(() -> new FamilyMemberNotFound("Попытка изменить человека, которого нет в базе"));
        if (familyMemberDto.getSex() != null) fm.setSex(familyMemberDto.getSex());
        if (familyMemberDto.getFirstname() != null) fm.setFirstname(familyMemberDto.getFirstname());
        if (familyMemberDto.getBirthday() != null) fm.setBirthday(familyMemberDto.getBirthday());
        if (familyMemberDto.getLastname() != null) fm.setLastname(familyMemberDto.getLastname());
        if (familyMemberDto.getMiddlename() != null) fm.setMiddlename(familyMemberDto.getMiddlename());
        extractExtensionOfFamilyMember(familyMemberDto, fm);
        familyRepo.save(fm);

        return familyMemberMapper.entityToDto(fm);
    }

    private void extractExtensionOfFamilyMember(FamilyMemberDto familyMemberDto, FamilyMember fm) {
        if (familyMemberDto.getFatherId() != null) {
            Optional<FamilyMember> father = familyRepo.findById(familyMemberDto.getFatherId());
            if (father.isPresent() && father.get().getSex() == Sex.MALE) fm.setFather(father.get());
            else log.warn("Предъявляенное fatherId не соответствует базе. Данная позиция игнорирована");
        }
        if (familyMemberDto.getMotherId() != null) {
            Optional<FamilyMember> mother = familyRepo.findById(familyMemberDto.getMotherId());
            if (mother.isPresent() && mother.get().getSex() == Sex.FEMALE) fm.setMother(mother.get());
            else log.warn("Предъявляенное motherId не соответствует базе. Данная позиция игнорирована");
        }
        if (familyMemberDto.getMemberInfo() != null) {
            FamilyMemberInfo fmi = familyMemberInfoMapper.dtoToEntity(familyMemberDto.getMemberInfo());
            Set<Email> emailSet= fmi.getEmails();
            String mainEmail = fmi.getMainEmail().getEmailName();
            if (mainEmail != null) {
                emailSet.add(fmi.getMainEmail());
                Email email = emailService.getEmailbyEmailName(mainEmail);
                if (email != null) fmi.setMainEmail(email);
            }

            Set<Email> resultEmails = new HashSet<>();
            if (emailSet != null) {
                for (Email mail : emailSet) {
                    Email oneEmail = emailService.getEmailbyEmailName(mail.getEmailName());
                    if (oneEmail != null) resultEmails.add(oneEmail);
                }
                fmi.setEmails(resultEmails);

            }

            String mainPhone = fmi.getMainPhone().getPhoneNumber();
            if (mainPhone != null) {
                Phone phone = phoneService.getPhoneByPhoneNumber(mainPhone);
                if (phone != null) fmi.setMainPhone(phone);
            }
            Address mainAddress = fmi.getMainAddress();
            fm.setFamilyMemberInfo(fmi);
        }
        log.info("Расширенная информация проверена и установлена");
    }

    public String removeFamilyMember(Long id) {
        Optional<FamilyMember> remFM = familyRepo.findById(id);
        if (remFM.isEmpty())
            throw new FamilyMemberNotFound("Человек с ID:".concat(String.valueOf(id)).concat(" не найден"));
        if (remFM.get().getSex() == Sex.MALE) {
            List<FamilyMember> link1 = familyRepo.findAllByFather_Id(id);
            for (FamilyMember fm : link1
            ) {
                fm.setFather(null);
            }
        }
        if (remFM.get().getSex() == Sex.FEMALE) {
            List<FamilyMember> link2 = familyRepo.findAllByMother_Id(id);
            for (FamilyMember fm : link2
            ) {
                fm.setMother(null);
            }
        }
        familyRepo.deleteById(id);
        return String.format("Человек: %s %s %s удален, из базы",
                remFM.get().getFirstname(),
                remFM.get().getMiddlename(),
                remFM.get().getLastname());

    }


}

