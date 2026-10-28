package ru.memman.mappers;

import ru.memman.dtos.FioDto;
import ru.memman.entity.Fio;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FioMapper extends AbstractMapper<FioDto, Fio> {

}
