package com.example.mappers;

import com.example.dtos.AddressDto;
import com.example.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper extends AbstractMapper<AddressDto, Address> {
}
