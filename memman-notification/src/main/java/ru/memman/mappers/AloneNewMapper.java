package ru.memman.mappers;

import ru.memman.dtos.AloneNewDto;
import ru.memman.entity.AloneNew;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AloneNewMapper extends AbstractMapper<AloneNewDto, AloneNew>{

}
