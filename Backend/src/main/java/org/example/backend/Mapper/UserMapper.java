package org.example.backend.Mapper;

import org.example.backend.Entity.User;
import org.example.backend.Dto.Auth.Request.CreateNewUserRequest;
import org.example.backend.Dto.User.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(config = BaseMapper.class)
public interface UserMapper extends BaseMapper<User, UserDTO> {
  @Mapping(target = "roles", expression = "java(Set.of(Role.USER))")
  User mapToNewUser(CreateNewUserRequest createNewUserRequest);


  @Override
  @Mapping(target = "createdAt", ignore = true)
  User mapToEntity(UserDTO dto);
}
