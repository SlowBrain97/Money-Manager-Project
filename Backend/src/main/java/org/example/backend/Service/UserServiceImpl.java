package org.example.backend.Service;

import lombok.RequiredArgsConstructor;
import org.example.backend.Entity.User;
import org.example.backend.Dto.Auth.Request.CreateNewUserRequest;
import org.example.backend.Exception.UsernameExistedException;
import org.example.backend.Mapper.UserMapper;
import org.example.backend.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements iUserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;


  public void createNewUser(CreateNewUserRequest createNewUserRequest){
    if (userRepository.existsByUsername(createNewUserRequest.getUsername())){
        throw new UsernameExistedException("Username have already exist", "/api/v1/user/create");
    }
    User newUser = userMapper.mapToNewUser(createNewUserRequest);
    newUser.setPassword(passwordEncoder.encode(createNewUserRequest.getPassword()));
    userRepository.save(newUser);
  }
}
