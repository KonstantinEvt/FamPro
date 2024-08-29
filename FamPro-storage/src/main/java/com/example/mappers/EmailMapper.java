package com.example.mappers;

import com.example.dtos.EmailDto;
import com.example.entity.Email;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmailMapper extends AbstractMapper<EmailDto, Email> {
}
