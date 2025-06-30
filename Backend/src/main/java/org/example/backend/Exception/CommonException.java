package org.example.backend.Exception;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonException extends RuntimeException  {
  private String path;
  public CommonException(String message, String path) {
    super(message);
    this.path = path;
  }
}
