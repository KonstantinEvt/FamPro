package com.example.mappers;

import com.example.dtos.FioDto;
import com.example.entity.Fio;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FioMapper extends AbstractMapper<FioDto, Fio>{

}
