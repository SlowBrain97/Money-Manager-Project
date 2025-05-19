package org.example.backend.Mapper;

import org.mapstruct.*;

import java.util.List;
@MapperConfig(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface  BaseMapper<E,D>{
   E mapToEntity(D dto);
   D mapToDto(E entity);
  void updateEnity(D dto, @MappingTarget E entity);
   List<E> mapToListEntity(List<D> dto);
  List<D> mapToListDto(List<E> entity);
}
