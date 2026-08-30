package ru.memman.mappers;

import ru.memman.dtos.PhoneDto;
import ru.memman.entity.Phone;
import ru.memman.mappers.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhoneMapper extends AbstractMapper<PhoneDto, Phone> {
}
