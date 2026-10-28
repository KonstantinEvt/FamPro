package ru.memman.mappers;

import ru.memman.dtos.FamilyMemberInfoDto;
import ru.memman.entity.FamilyMemberInfo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FamilyMemberInfoMapper extends AbstractMapper<FamilyMemberInfoDto, FamilyMemberInfo> {
}
