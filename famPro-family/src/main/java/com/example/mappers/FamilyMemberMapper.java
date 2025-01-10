package com.example.mappers;

import com.example.dtos.FamilyMemberDto;
import com.example.entity.ShortFamilyMember;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FamilyMemberMapper extends AbstractMapper<FamilyMemberDto, ShortFamilyMember> {
}
