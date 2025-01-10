package com.example.mappers;

import com.example.dtos.FamilyMemberInfoDto;
import com.example.entity.ShortFamilyMemberInfo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FamilyMemberInfoMapper extends AbstractMapper<FamilyMemberInfoDto, ShortFamilyMemberInfo> {
}
