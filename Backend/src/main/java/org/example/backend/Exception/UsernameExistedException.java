package org.example.backend.Exception;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UsernameExistedException extends RuntimeException{
  private String path;
  public UsernameExistedException (String message, String path) {

    super(message);
    this.path = path;
  }
}
