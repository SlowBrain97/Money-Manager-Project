package org.example.backend.Controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.Dto.Auth.Request.LoginRequest;
import org.example.backend.Dto.Auth.Response.LoginResponse;
import org.example.backend.Service.AuthService;
import org.example.backend.Service.UserServiceImpl;
import org.example.backend.Dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final UserServiceImpl userService;
  private final AuthService authService;
  @PostMapping("/login")
  public ResponseEntity<ApiResponse> login (@RequestBody LoginRequest loginRequest){
    LoginResponse loginResponse = authService.loginResolve(loginRequest);
    return ResponseEntity.ok(new ApiResponse(loginResponse,"Login successfully",201));
  }
}
