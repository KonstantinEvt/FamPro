package com.example.mappers;

import com.example.dtos.AddressDto;
import com.example.dtos.BurialDto;
import com.example.entity.Address;
import com.example.entity.PlaceBurial;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BurialMapper extends AbstractMapper<BurialDto, PlaceBurial> {
}
