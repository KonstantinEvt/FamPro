package com.family.fampro.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingTarget;

import java.util.Collection;

@MapperConfig(componentModel = "spring")
public interface AbstractMapper<Dto, Entity> {
    Dto entityToDto(Entity entity);

    Entity dtoToEntity(Dto dto);

    Entity updateEntity(Dto dto, @MappingTarget Entity entity);

    Collection<Entity> collectionDtoToCollectionEntity(Collection<Dto> dtoCollection);

    Collection<Dto> collectionEntityToCollectionDto(Collection<Entity> entityCollection);
}
