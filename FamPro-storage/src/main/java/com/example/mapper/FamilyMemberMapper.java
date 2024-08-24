package com.example.mapper;

import com.example.dto.FamilyMemberDto;
import com.example.entity.FamilyMember;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FamilyMemberMapper extends AbstractMapper<FamilyMemberDto, FamilyMember> {
}
