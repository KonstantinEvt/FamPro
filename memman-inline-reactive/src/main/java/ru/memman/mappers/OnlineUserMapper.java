package ru.memman.mappers;

import ru.memman.dtos.OnlineUserDto;
import ru.memman.entity.BaseUser;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OnlineUserMapper extends AbstractMapper<OnlineUserDto, BaseUser> {
}
