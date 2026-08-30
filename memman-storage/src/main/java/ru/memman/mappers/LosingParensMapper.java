package ru.memman.mappers;

import ru.memman.dtos.FioDto;
import ru.memman.entity.LosingParent;
import org.springframework.stereotype.Component;

@Component
public class LosingParensMapper {
    LosingParensMapper() {
    }

    public LosingParent fioDtoToLosingParent(FioDto fioDto) {
        if (fioDto == null) {
            return null;
        } else {
            LosingParent.LosingParentBuilder<?, ?> losingParent = LosingParent.builder();
            losingParent.firstName(fioDto.getFirstName());
            losingParent.middleName(fioDto.getMiddleName());
            losingParent.lastName(fioDto.getLastName());
            losingParent.birthday(fioDto.getBirthday());
            losingParent.uuid(fioDto.getUuid());
            losingParent.sex(fioDto.getSex());
            losingParent.fullName(fioDto.getFullName());
            return losingParent.build();

        }
    }
}

