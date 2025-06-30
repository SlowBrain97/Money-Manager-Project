package org.example.backend.Controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.Dto.Auth.Request.LoginRequest;
import org.example.backend.Dto.Auth.Response.LoginResponse;
import org.example.backend.Service.AuthService;
import org.example.backend.Service.UserServiceImpl;
import org.example.backend.Dto.ApiResponse;
import org.hibernate.boot.MappingException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final UserServiceImpl userService;
  private final AuthService authService;
  private final RedisTemplate<String,String> redisTemplate;
  private final ObjectMapper objectMapper;
  @PostMapping("/login")
  public ResponseEntity<ApiResponse> login (@RequestBody LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request){
    LoginResponse loginResponse = authService.loginResolve(loginRequest, response,request);
    return ResponseEntity.ok(new ApiResponse(loginResponse,"Login successfully",200));
  }

  @PostMapping("/refresh")
  public ResponseEntity<ApiResponse> refreshToken (HttpServletResponse response, HttpServletRequest request){
    String newAccessToken = authService.refreshToken(response,request);
    return ResponseEntity.ok(new ApiResponse(newAccessToken,"Refresh Token Successfully",HttpStatus.OK.value()));
  }
  @PostMapping("/logout")
  public void logout (HttpServletRequest request , HttpServletResponse response, @RequestHeader("Authorization") String jwt){
    authService.logout(request,response, jwt.substring(7));
  }

}
