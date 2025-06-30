package org.example.backend.Dto.Auth.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.backend.Entity.Role;

@Data

public class CreateNewUserRequest {
  @NotBlank(message = "Username not be blank")
  private String email;
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @NotBlank
  private String password;
}
