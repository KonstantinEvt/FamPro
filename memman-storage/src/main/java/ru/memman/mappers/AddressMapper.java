package ru.memman.mappers;

import ru.memman.dtos.AddressDto;
import ru.memman.entity.Address;
import ru.memman.mappers.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper extends AbstractMapper<AddressDto, Address> {
}
