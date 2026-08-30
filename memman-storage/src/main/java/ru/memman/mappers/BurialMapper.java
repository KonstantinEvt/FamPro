package ru.memman.mappers;

import ru.memman.dtos.BurialDto;
import ru.memman.entity.PlaceBurial;
import ru.memman.mappers.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BurialMapper extends AbstractMapper<BurialDto, PlaceBurial> {
}
