package com.example.mappers;

import org.mapstruct.MapperConfig;

import java.util.Collection;

@MapperConfig(componentModel = "spring")
public interface AbstractMapper<Dto, Entity> {
    Dto entityToDto(Entity entity);

    Entity dtoToEntity(Dto dto);

    Collection<Entity> collectionDtoToCollectionEntity(Collection<Dto> dtoCollection);
    Collection<Dto> collectionEntityToCollectionDto(Collection<Entity> dtoCollection);
}
