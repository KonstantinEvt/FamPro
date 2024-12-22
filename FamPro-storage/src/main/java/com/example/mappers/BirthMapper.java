package com.example.mappers;

import com.example.dtos.AddressDto;
import com.example.dtos.BirthDto;
import com.example.entity.Address;
import com.example.entity.PlaceBirth;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BirthMapper extends AbstractMapper<BirthDto, PlaceBirth> {
}
