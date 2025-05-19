package org.example.backend.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend.Dto.Auth.Request.CreateNewUserRequest;
import org.example.backend.Service.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
  private final UserServiceImpl userService;


  @PostMapping("/create")
  public ResponseEntity<?> createUser(@Valid  @RequestBody CreateNewUserRequest createNewUserRequest){
    userService.createNewUser(createNewUserRequest);
    return ResponseEntity.ok("Register successfully");
  }
}
