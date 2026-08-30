package ru.memman.mappers;

import ru.memman.dtos.RecipientDto;
import ru.memman.entity.Recipient;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;
@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecipientMapper extends AbstractMapper<RecipientDto, Recipient>{

}
