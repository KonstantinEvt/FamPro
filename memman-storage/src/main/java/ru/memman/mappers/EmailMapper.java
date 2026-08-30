package ru.memman.mappers;

import ru.memman.dtos.EmailDto;
import ru.memman.entity.Email;
import ru.memman.mappers.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmailMapper extends AbstractMapper<EmailDto, Email> {
}
