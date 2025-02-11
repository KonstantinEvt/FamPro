package com.example.mappers;

import com.example.dtos.FioDto;
import com.example.entity.OldFio;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class OldNamesMapper {
    OldNamesMapper() {
    }

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
    }

    public FioDto oldFioToFioDto(OldFio oldFio) {
        if (oldFio == null) {
            return null;
        } else {
            FioDto.FioDtoBuilder<?, ?> fioDto = FioDto.builder();
            fioDto.firstName(oldFio.getFirstName());
            fioDto.middleName(oldFio.getMiddleName());
            fioDto.lastName(oldFio.getLastName());
            fioDto.birthday(oldFio.getBirthday());
            fioDto.uuid(oldFio.getUuid());
            fioDto.sex(oldFio.getSex());
            fioDto.fullName(oldFio.getFullName());
            return fioDto.build();
        }
    }
    public Set<FioDto> oldFiosSetToFioDtoSet(Set<OldFio> set) {
        if (set == null) {
            return null;
        } else {
            Set<FioDto> set1 = new HashSet<>();
            for (OldFio oldFio : set) {
                set1.add(this.oldFioToFioDto(oldFio));
            }

            return set1;
        }
    }
}