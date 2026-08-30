package ru.memman.mappers;

import ru.memman.dtos.BiometricDto;
import ru.memman.entity.Biometric;
import ru.memman.mappers.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BiometricMapper extends AbstractMapper<BiometricDto, Biometric> {
}
