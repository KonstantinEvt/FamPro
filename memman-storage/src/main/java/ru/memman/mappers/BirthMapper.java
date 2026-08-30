package ru.memman.mappers;

import ru.memman.dtos.BirthDto;
import ru.memman.entity.PlaceBirth;
import ru.memman.mappers.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BirthMapper extends AbstractMapper<BirthDto, PlaceBirth> {
}
