package com.example.mappers;

import com.example.dtos.RecipientDto;
import com.example.entity.Recipient;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;
@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecipientMapper extends AbstractMapper<RecipientDto, Recipient>{

}
