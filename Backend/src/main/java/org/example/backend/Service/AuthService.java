package org.example.backend.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.Dto.User.UserDTO;
import org.example.backend.Entity.CustomUserDetails;
import org.example.backend.Dto.Auth.Request.LoginRequest;
import org.example.backend.Dto.Auth.Response.LoginResponse;
import org.example.backend.Mapper.UserMapper;
import org.example.backend.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final AuthenticationManager authenticationManager;
  private final UserDetailServiceImpl userDetailService;
  private final JwtUtils jwtUtils;
  private final UserMapper userMapper;


  public LoginResponse loginResolve(LoginRequest loginRequest){

    UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword());

    authenticationManager.authenticate(authenticationToken);
    CustomUserDetails user = (CustomUserDetails) userDetailService.loadUserByUsername(loginRequest.getUsername());
    UserDTO userDTO = userMapper.mapToDto(user.getUser());
    String token = jwtUtils.generateJwt(loginRequest.getUsername(), new HashMap<>());
    String refreshToken = jwtUtils.generateRefreshToken(loginRequest.getUsername(), new HashMap<>());
    ;
    return LoginResponse.builder().refreshToken(refreshToken).token(token).user(userDTO).build();
  }
}
