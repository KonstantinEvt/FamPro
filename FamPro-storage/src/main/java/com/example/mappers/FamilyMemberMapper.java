package com.example.mappers;

import com.example.dtos.FamilyMemberDto;
import com.example.entity.FamilyMember;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FamilyMemberMapper extends AbstractMapper<FamilyMemberDto, FamilyMember> {
}
