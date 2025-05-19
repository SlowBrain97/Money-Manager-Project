package org.example.backend.Dto.User;


import lombok.Data;
import org.example.backend.Entity.Role;


import java.util.List;

@Data
public class UserDTO {
  private long id;
  private String username;
  private String firstName;
  private String lastName;
  private List<Role> roles;
}
