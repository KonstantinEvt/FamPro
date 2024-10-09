package com.example.service;

import com.example.entity.Fio;
import com.example.mappers.FioMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Slf4j
public class FioServiceImp<T extends Fio> implements FioService<T> {
    protected FioMapper fioMapper;
    protected UUID generateUUIDFromFullName(String string){
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
}
