package com.example.service;

import com.example.dtos.FamilyMemberDto;
import com.example.dtos.FioDto;
import com.example.entity.FamilyMember;
import com.example.entity.Fio;
import com.example.enums.Sex;
import com.example.mappers.FioMapper;
import com.ibm.icu.text.SimpleDateFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Slf4j
public class FioServiceImp<T extends Fio> implements FioService<T> {
    protected FioMapper fioMapper;

    protected UUID generateUUIDFromFullName(String string) {
        return UUID.nameUUIDFromBytes(string.getBytes());
    }

    protected UUID generateUUIDFromFio(Fio fio) {
        String str = fio.getFirstName()
                .concat(fio.getMiddleName())
                .concat(fio.getLastName())
                .concat(String.valueOf(fio.getBirthday().toLocalDate())).toLowerCase()
                .concat("Rainbow");
        log.info("новый UUID человека сгенерирован");
        return UUID.nameUUIDFromBytes(str.getBytes());
    }

    protected String generateFioStringInfo(Fio fio) {
        return String.join(" ", fio.getFirstName(), fio.getMiddleName(), fio.getLastName(), ". Birthday: ", (fio.getBirthday() != null) ? String.valueOf(fio.getBirthday().toLocalDate()) : null);
    }

    protected boolean checkDifBirthday(Sex sexParent, Date parent, Date child) {
        int dif = child.toLocalDate().getYear() - parent.toLocalDate().getYear();
        return (sexParent == Sex.MALE && (dif > 10) && dif < 80)
                || (sexParent == Sex.FEMALE && dif > 12 && dif < 60);
    }

    protected boolean checkBirthdayToSet(FamilyMemberDto dto, Set<FamilyMember> children, String father, String mother) {
        if (children != null && !children.isEmpty()) for (FamilyMember child :
                children) {
            if (!checkDifBirthday(dto.getSex(), dto.getBirthday(), child.getBirthday())) return false;
        }
        Date birthday;
        if ((father != null && !father.isBlank()) || (dto.getFatherFio() != null && dto.getFatherFio().getBirthday() != null)) {
            if (father != null && !father.isBlank()) {
                birthday = getDateFromInfo(father);
                if (dto.getFatherFio() != null && dto.getFatherFio().getBirthday() != null && !Objects.equals(dto.getFatherFio().getBirthday().toLocalDate(), birthday.toLocalDate()))
                    birthday = dto.getFatherFio().getBirthday();
            } else birthday = dto.getFatherFio().getBirthday();
            if (birthday == null || !checkDifBirthday(Sex.MALE, birthday, dto.getBirthday())) return false;
        }
        if ((mother != null && !mother.isBlank()) || (dto.getMotherFio() != null && dto.getMotherFio().getBirthday() != null)) {
            if ((mother != null && !mother.isBlank())) {
                birthday = getDateFromInfo(mother);
                if (dto.getMotherFio() != null && dto.getMotherFio().getBirthday() != null && !Objects.equals(dto.getMotherFio().getBirthday().toLocalDate(), birthday.toLocalDate()))
                    birthday = dto.getMotherFio().getBirthday();
            } else birthday = dto.getMotherFio().getBirthday();
            return birthday != null && checkDifBirthday(Sex.FEMALE, birthday, dto.getBirthday());
        }
        return true;
    }

    public Date getDateFromInfo(String str) {
        Date birthday;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        if (str.charAt(0) == '(') {
            if (str.charAt(1) == 'A' || str.charAt(1) == 'I') {
                String str1 = str.substring(17);
                String[] strings = str1.split(" ");
                if (!strings[6].equals("null")) birthday = parseDate(strings[6]);
                else birthday = null;
            } else birthday = null;
        } else {
            String[] strings = str.split(" ");
            birthday = parseDate(strings[6]);
        }

        return birthday;
    }

    protected Date parseDate(String string) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(string, formatter);
        return Date.valueOf(localDate);
    }
}
