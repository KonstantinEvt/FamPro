package ru.memman.mappers;

import ru.memman.dtos.DescriptionDto;
import ru.memman.entity.Description;
import ru.memman.mappers.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DescriptionMapper extends AbstractMapper<DescriptionDto, Description> {
}
