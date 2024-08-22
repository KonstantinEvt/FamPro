package com.family.fampro.mapper;

import com.family.fampro.dto.FamilyMemberDto;
import com.family.fampro.entity.FamilyMember;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FamilyMemberMapper extends AbstractMapper<FamilyMemberDto, FamilyMember> {
}
