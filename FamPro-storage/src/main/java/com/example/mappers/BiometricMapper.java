package com.example.mappers;

import com.example.dtos.BiometricDto;
import com.example.entity.Biometric;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BiometricMapper extends AbstractMapper<BiometricDto, Biometric> {
}
