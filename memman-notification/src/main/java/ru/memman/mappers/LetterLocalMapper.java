package ru.memman.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;
import ru.memman.dtos.LetterLocalDto;
import ru.memman.entity.LetterLocal;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LetterLocalMapper extends AbstractMapper<LetterLocalDto, LetterLocal> {
}
