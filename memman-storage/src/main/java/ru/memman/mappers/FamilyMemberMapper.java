package ru.memman.mappers;

import ru.memman.dtos.FamilyMemberDto;
import ru.memman.entity.FamilyMember;
import ru.memman.mappers.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FamilyMemberMapper extends AbstractMapper<FamilyMemberDto, FamilyMember> {
}
