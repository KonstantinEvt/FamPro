package com.example.mappers;

import com.example.dtos.FioDto;
import com.example.entity.OldFio;
import org.springframework.stereotype.Component;

@Component
public class OldNamesMapper {
    OldNamesMapper(){}
    public OldFio fioDtoToOldFio(FioDto fioDto) {
        if (fioDto == null) {
            return null;
        } else {
            OldFio.OldFioBuilder<?, ?> oldFio = OldFio.builder();
            oldFio.firstName(fioDto.getFirstName());
            oldFio.middleName(fioDto.getMiddleName());
            oldFio.lastName(fioDto.getLastName());
            oldFio.birthday(fioDto.getBirthday());
            oldFio.uuid(fioDto.getUuid());
            oldFio.sex(fioDto.getSex());
            oldFio.fullName(fioDto.getFullName());
            return oldFio.build();

        }
    }}