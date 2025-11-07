package com.example.mappers;

import com.example.dtos.DescriptionDto;
import com.example.entity.Description;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DescriptionMapper extends AbstractMapper<DescriptionDto, Description> {
}
