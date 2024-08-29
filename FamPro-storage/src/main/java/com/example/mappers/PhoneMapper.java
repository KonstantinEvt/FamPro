package com.example.mappers;

import com.example.dtos.PhoneDto;
import com.example.entity.Phone;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhoneMapper extends AbstractMapper<PhoneDto, Phone> {
}
