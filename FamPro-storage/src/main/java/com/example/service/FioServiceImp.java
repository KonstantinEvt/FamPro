package com.example.service;

import com.example.entity.Fio;
import com.example.enums.Sex;
import com.example.mappers.FioMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.util.UUID;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Slf4j
public class FioServiceImp<T extends Fio> implements FioService<T> {
    protected FioMapper fioMapper;

    protected UUID generateUUIDFromFullName(String string) {
        System.out.println("tyt3");
        return UUID.nameUUIDFromBytes(string.getBytes());
    }

    protected UUID generateUUIDFromFio(Fio fio) {
        String str = fio.getFirstName()
                .concat(fio.getMiddleName())
                .concat(fio.getLastName())
                .concat(String.valueOf(fio.getBirthday().toLocalDate())).toLowerCase()
                .concat("Rainbow");
        System.out.println("tyt99");
        log.info("новый UUID человека сгенерирован");
        System.out.println("tyt100");
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
}
